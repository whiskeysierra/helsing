package de.bht.pat.tenzing.ui;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.base.Throwables;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.inamik.utils.SimpleTableFormatter;
import com.inamik.utils.TableFormatter;
import de.bht.pat.tenzing.events.BootEvent;
import de.bht.pat.tenzing.events.ExecutionError;
import de.bht.pat.tenzing.events.InputEvent;
import de.bht.pat.tenzing.events.PromptEvent;
import de.bht.pat.tenzing.events.QueryEvent;
import de.bht.pat.tenzing.events.QuitEvent;
import de.bht.pat.tenzing.events.ResultEvent;
import de.bht.pat.tenzing.events.SqlEvent;
import de.bht.pat.tenzing.events.SyntaxError;
import de.bht.pat.tenzing.events.FeatureError;
import jline.ConsoleReader;
import jline.ConsoleReaderInputStream;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

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

    @Subscribe
    public void onResult(ResultEvent event) throws IOException {
        final File file = event.getFile();
        // TODO print stream-like, without pushing everything in memory
        final CSVReader reader = new CSVReader(new FileReader(file), '\t');

        final TableFormatter formatter = new SimpleTableFormatter(true);

        // TODO add select clause to result event

        while (true) {
            final String[] line = reader.readNext();
            if (line == null) break;

            formatter.nextRow();
            for (String cell : line) {
                formatter.nextCell(TableFormatter.ALIGN_RIGHT, TableFormatter.VALIGN_CENTER);
                formatter.addLine(" " + cell + " ");
            }
        }

        final String[] lines = formatter.getFormattedTable();

        for (String line : lines) {
            console.printString(line);
            console.printNewline();
        }
        console.flushConsole();

        bus.post(new PromptEvent());
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
