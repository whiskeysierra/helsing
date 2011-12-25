package org.whiskeysierra.helsing.ui.cli;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.google.inject.Inject;
import org.whiskeysierra.helsing.api.sql.SelectStatement;
import org.whiskeysierra.helsing.api.sql.SqlProjection;
import org.whiskeysierra.helsing.events.PrintEvent;
import org.whiskeysierra.helsing.events.PrintLineEvent;
import org.whiskeysierra.helsing.events.ResultEvent;
import org.whiskeysierra.helsing.events.ResultEvent.Duration;
import org.whiskeysierra.helsing.events.ResultPrintedEvent;
import org.whiskeysierra.helsing.util.io.FileFormat;
import org.whiskeysierra.helsing.util.io.Line;
import org.whiskeysierra.helsing.util.text.TimeFormatter;

import javax.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.util.Map;

final class ResultPrinter {

    private final EventBus bus;
    private final FileFormat format;
    private final Provider<WidthCalculator> newCalculator;
    private final TimeFormatter formatter;

    @Inject
    public ResultPrinter(EventBus bus, FileFormat format, Provider<WidthCalculator> newCalculator, TimeFormatter formatter) {
        this.bus = bus;
        this.format = format;
        this.formatter = formatter;
        this.newCalculator = newCalculator;

        bus.register(this);
    }

    @Subscribe
    public void onResult(ResultEvent event) throws IOException {
        final SelectStatement statement = event.getStatement();
        final SqlProjection projection = statement.projection();
        final Iterable<String> header = projection.toStrings();

        final WidthCalculator calculator = newCalculator.get();

        // treat header as any other line
        calculator.processLine(format.lineOf(header).toString());

        for (File file : event.getFiles()) {
            Files.readLines(file, Charsets.UTF_8, calculator);
        }

        final Map<Integer, Integer> widths = calculator.getResult();

        printTableSeparator(widths);
        printTableLine(widths, header);
        printTableSeparator(widths);

        long count = 0;

        for (File file : event.getFiles()) {
            count += Files.readLines(file, Charsets.UTF_8, new LineProcessor<Long>() {

                private long count;

                @Override
                public boolean processLine(String value) throws IOException {
                    final Line line = format.lineOf(value);
                    printTableLine(widths, line);
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
