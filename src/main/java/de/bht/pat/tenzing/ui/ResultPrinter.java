package de.bht.pat.tenzing.ui;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.google.inject.Inject;
import de.bht.pat.tenzing.bean.Duration;
import de.bht.pat.tenzing.events.PrintEvent;
import de.bht.pat.tenzing.events.PrintLineEvent;
import de.bht.pat.tenzing.events.ResultEvent;
import de.bht.pat.tenzing.events.ResultPrintedEvent;
import de.bht.pat.tenzing.util.Formatting;
import de.bht.pat.tenzing.util.text.TimeFormatter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

final class ResultPrinter {

    private final EventBus bus;
    private final TimeFormatter formatter;

    @Inject
    public ResultPrinter(EventBus bus, TimeFormatter formatter) {
        this.bus = bus;
        this.formatter = formatter;

        bus.register(this);
    }

    @Subscribe
    public void onResult(ResultEvent event) throws IOException {

        final WidthCalculator calculator = new WidthCalculator();

        for (File file : event.getFiles()) {
            Files.readLines(file, Charsets.UTF_8, calculator);
        }

        final Map<Integer, Integer> widths = calculator.getResult();

        // TODO add select clause to result event

        printTableSeparator(widths);
        // TODO make dynamic
        // TODO consider width of header in Width calculator
        // e.g. calculator.processLine(Formatting.JOINER.join(header))
        printTableLine(widths, "year", "population");
        printTableSeparator(widths);

        long count = 0;

        for (File file : event.getFiles()) {
            count += Files.readLines(file, Charsets.UTF_8, new LineProcessor<Long>() {

                private long count;

                @Override
                public boolean processLine(String line) throws IOException {
                    printTableLine(widths, Formatting.SPLITTER.split(line));
                    count++;
                    return true;
                }

                @Override
                public Long getResult() {
                    return count;
                }

            });
        }

        printTableSeparator(widths);

        final Duration duration = event.getDuration();
        println(count + " rows in set (" + formatter.format(duration) + ")");

        bus.post(new ResultPrintedEvent());
    }

    private void printTableLine(Map<Integer, Integer> widths, String... line) throws IOException {
        printTableLine(widths, Arrays.asList(line));
    }

    private void printTableLine(Map<Integer, Integer> widths, Iterable<String> line) throws IOException {
        print("|");

        int i = 0;
        for (String cell : line) {
            print(" " + Strings.padStart(cell, widths.get(i), ' ') + " |");
            i++;
        }

        println();
    }

    private void printTableSeparator(Map<Integer, Integer> widths) throws IOException {
        print("+");
        for (int width : widths.values()) {
            print(Strings.repeat("-", width + 2) + "+");
        }
        println();
    }

    private void print(String s) {
        bus.post(new PrintEvent(s));
    }

    private void println() {
        bus.post(new PrintLineEvent());
    }

    private void println(String s) {
        bus.post(new PrintLineEvent(s));
    }

}
