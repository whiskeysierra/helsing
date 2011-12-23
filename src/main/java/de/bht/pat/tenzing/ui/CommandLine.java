package de.bht.pat.tenzing.ui;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.Files;
import com.google.inject.Inject;
import de.bht.pat.tenzing.events.BootEvent;
import de.bht.pat.tenzing.events.ExecutionError;
import de.bht.pat.tenzing.events.FeatureError;
import de.bht.pat.tenzing.events.InputEvent;
import de.bht.pat.tenzing.events.PromptEvent;
import de.bht.pat.tenzing.events.QuitEvent;
import de.bht.pat.tenzing.events.ResultEvent;
import de.bht.pat.tenzing.events.SqlEvent;
import de.bht.pat.tenzing.events.SyntaxError;
import jline.ConsoleReader;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

final class CommandLine {

    private final ConsoleReader console;
    private final EventBus bus;

    @Inject
    public CommandLine(ConsoleReader console, EventBus bus) {
        this.console = console;
        this.bus = bus;

        bus.register(this);
    }

    @Subscribe
    public void onBoot(BootEvent event) {
        bus.post(new PromptEvent());
    }

    @Subscribe
    public void onPrompt(PromptEvent event) throws IOException {
        // TODO support CTRL-C
        final String line = console.readLine();
        bus.post(new InputEvent(line));
    }

    @Subscribe
    public void onInput(InputEvent event) {
        switch (StringUtils.trimToEmpty(event.getLine())) {
            case "": {
                bus.post(new PromptEvent());
                return;
            }
            case "quit": {
                quit();
                return;
            }
            case "exit": {
                quit();
                return;
            }
            default: {
                bus.post(new SqlEvent(event.getLine()));
            }
        }
    }

    private void quit() {
        bus.post(new QuitEvent());
    }

    @Subscribe
    public void onSyntaxError(SyntaxError error) throws IOException {
        print("You have an error in your SQL syntax. ");
        final Throwable exception = Throwables.getRootCause(error.getException());
        final String message = StringUtils.trimToEmpty(exception.getMessage());
        println(message.replace(" ...", ",").replace("\n    ", " "));
        bus.post(new PromptEvent());
    }

    @Subscribe
    public void onUnsupportedFeature(FeatureError error) throws IOException {
        println("%s not supported", error.getFeature());
        bus.post(new PromptEvent());
    }

    private int sum(Iterable<Integer> values) {
        int sum = 0;
        for (Integer value : values) {
            sum += value;
        }
        return sum;
    }

    @Subscribe
    public void onResult(ResultEvent event) throws IOException {
        final File file = event.getFile();
        final CSVReader reader = new CSVReader(new FileReader(file), '\t');

        final WidthCalculator calculator = new WidthCalculator();
        Files.readLines(file, Charsets.UTF_8, calculator);
        final Map<Integer, Integer> widths = calculator.getResult();

        // TODO add select clause to result event

        printTableSeparator(widths);
        // TODO make dynamic
        printTableLine(widths, "year", "population");
        printTableSeparator(widths);

        while (true) {
            final String[] line = reader.readNext();
            if (line == null) break;
            printTableLine(widths, line);
        }

        printTableSeparator(widths);

        bus.post(new PromptEvent());
    }

    private void printTableLine(Map<Integer, Integer> widths, String... line) throws IOException {
        console.printString("|");
        for (int i = 0; i < line.length; i++) {
            console.printString(" ");
            console.printString(Strings.padStart(line[i], widths.get(i), ' '));
            console.printString(" |");
        }
        console.printNewline();
        console.flushConsole();
    }

    private void printTableSeparator(Map<Integer, Integer> widths) throws IOException {
        console.printString("+");
        for (int width : widths.values()) {
            console.printString(Strings.repeat("-", width + 2));
            console.printString("+");
        }
        console.printNewline();
        console.flushConsole();
    }

    @Subscribe
    public void onExecutionError(ExecutionError error) throws IOException {
        println("Execution failed");
        error.getException().printStackTrace();
        bus.post(new PromptEvent());
    }

    @Subscribe
    public void onQuit(QuitEvent event) throws IOException {
        println("Bye");
    }

    @Subscribe
    public void onUndeliverable(DeadEvent event) throws IOException {
        println("Unhandled event: %s", event.getEvent());
        quit();
    }

    private void println(String s, Object... arguments) throws IOException {
        println(String.format(s, arguments));
    }

    private void print(String s) throws IOException {
        console.printString(s);
        console.flushConsole();
    }

    private void println(String s) throws IOException {
        console.printString(s);
        console.printNewline();
        console.flushConsole();
    }

}
