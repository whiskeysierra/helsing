package de.bht.pat.tenzing.mapreduce;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.bht.pat.tenzing.Data;
import de.bht.pat.tenzing.events.ExecutionError;
import de.bht.pat.tenzing.events.QueryEvent;
import de.bht.pat.tenzing.events.ResultEvent;
import de.bht.pat.tenzing.util.concurrent.ProcessService;
import de.bht.pat.tenzing.util.concurrent.RunningProcess;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

final class QueryProcessor {

    private final EventBus bus;
    private final ProcessService service;
    private final File data;

    private final File hadoop = new File("hadoop/bin/hadoop");

    @Inject
    public QueryProcessor(EventBus bus, ProcessService service, @Data File data) {
        this.bus = bus;
        this.service = service;
        this.data = data;

        bus.register(this);
    }

    @Subscribe
    public void onStatement(QueryEvent event) {
        final String query = event.getQuery();

        // TODO set to real values
        final File jar = new File("target/tenzing-0.1-job.jar");

        // TODO fill from query
        final String table = "countries.csv";
        final File input = new File(data, table);

        // TODO make configurable
        final File output = new File("output");

        // TODO call hadoop, wait for response, fire either success/result or error
        try {
            FileUtils.deleteDirectory(output);

            final RunningProcess process = service.prepare(hadoop, Arrays.asList(
                "jar", jar.getAbsolutePath(),
                "--input", input.getAbsolutePath(),
                "--output", output.getAbsolutePath(),
                query
            )).call();

            final long start = System.currentTimeMillis();
            process.await();
            final long end = System.currentTimeMillis();

            // TODO make dynamic, use all part-* files in output directory
            bus.post(new ResultEvent(new File(output, "part-r-00000"), end - start, TimeUnit.MILLISECONDS));
        } catch (IOException e) {
            bus.post(new ExecutionError(e));
        }
    }

}
