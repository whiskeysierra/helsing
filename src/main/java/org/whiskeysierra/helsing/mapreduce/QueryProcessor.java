package org.whiskeysierra.helsing.mapreduce;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.PatternFilenameFilter;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.commons.io.FileUtils;
import org.whiskeysierra.helsing.events.ExecutionError;
import org.whiskeysierra.helsing.events.QueryEvent;
import org.whiskeysierra.helsing.events.ResultEvent;
import org.whiskeysierra.helsing.events.ResultEvent.Duration;
import org.whiskeysierra.helsing.events.ResultPrintedEvent;
import org.whiskeysierra.helsing.util.concurrent.process.ProcessService;
import org.whiskeysierra.helsing.util.concurrent.process.RunningProcess;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

final class QueryProcessor {

    private final EventBus bus;
    private final ProcessService service;
    private final File data;
    private final File output;

    private final File hadoop = new File("hadoop/bin/hadoop");
    private final PatternFilenameFilter filter = new PatternFilenameFilter("^part-r-[0-9]{5}$");

    @Inject
    public QueryProcessor(EventBus bus, ProcessService service,
        @Named("data.directory") File data, @Named("output.directory") File output) {

        this.bus = bus;
        this.service = service;
        this.data = data;
        this.output = output;

        bus.register(this);
    }

    @Subscribe
    public void onStatement(QueryEvent event) {
        final String query = event.getQuery();

        // TODO make dynamic
        final File jar = new File("target/helsing-0.1-job.jar");

        final String table = event.getStatement().from().name();
        final File input = new File(data, table);

        try {
            deleteOutput();

            final RunningProcess process = service.prepare(hadoop,
                "jar", jar.getAbsolutePath(),
                "--input", input.getAbsolutePath(),
                "--output", output.getAbsolutePath(),
                query
            ).call();

            final long start = System.currentTimeMillis();
            process.await();
            final long end = System.currentTimeMillis();

            final List<File> files = Arrays.asList(output.listFiles(filter));
            final Duration duration = new Duration(end - start, TimeUnit.MILLISECONDS);

            bus.post(new ResultEvent(files, event.getStatement(), duration));
        } catch (IOException e) {
            bus.post(new ExecutionError(e));
        }
    }

    @Subscribe
    public void onResultPrinted(ResultPrintedEvent event) throws IOException {
        deleteOutput();
    }

    private void deleteOutput() throws IOException {
        FileUtils.deleteDirectory(output);
    }

}
