package de.bht.pat.tenzing.mapreduce;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.bht.pat.tenzing.events.ExecutionError;
import de.bht.pat.tenzing.events.QueryEvent;
import de.bht.pat.tenzing.events.ResultEvent;
import de.bht.pat.tenzing.util.concurrent.ProcessService;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

final class QueryProcessor {

    private final EventBus bus;
    private final ProcessService service;

    private final File hadoop = new File("hadoop/bin/hadoop");

    @Inject
    public QueryProcessor(EventBus bus, ProcessService service) {
        this.bus = bus;
        this.service = service;

        bus.register(this);
    }

    @Subscribe
    public void onStatement(QueryEvent event) {
        final String query = event.getQuery();

        // TODO set to real values
        final File jar = new File("tenzing.jar");
        final File input = new File("input");
        final File output = new File("output");
        // TODO comma separated list of libraries
        final String libraries = "";

        // TODO call hadoop, wait for response, fire either success/result or error

        try {
            service.prepare(hadoop, Arrays.asList(
                hadoop.getAbsolutePath(),
                "jar", jar.getAbsolutePath(),
                "-libjars", libraries,
                "--query", query,
                "--input", input.getAbsolutePath(),
                "--output", output.getAbsolutePath()
            )).call().await();
            bus.post(new ResultEvent(output));
        } catch (IOException e) {
            bus.post(new ExecutionError(e));
        }
    }

}
