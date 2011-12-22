package de.bht.pat.tenzing.ui;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.bht.pat.tenzing.events.BootEvent;
import de.bht.pat.tenzing.events.InputEvent;
import de.bht.pat.tenzing.events.PromptEvent;
import de.bht.pat.tenzing.events.QueryEvent;
import de.bht.pat.tenzing.events.QuitEvent;
import de.bht.pat.tenzing.events.SqlEvent;
import de.bht.pat.tenzing.events.SyntaxError;
import de.bht.pat.tenzing.events.UnsupportedFeatureEvent;
import jline.ConsoleReader;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

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
    public void onQuery(QueryEvent event) throws IOException {
        println("Perfoming mapreduce job execution");
        bus.post(new PromptEvent());
    }

    @Subscribe
    public void onSyntaxError(SyntaxError error) throws IOException {
        println("You have an error in your SQL syntax.");
        bus.post(new PromptEvent());
    }

    @Subscribe
    public void onUnsupportedFeature(UnsupportedFeatureEvent event) throws IOException {
        println("%s is not supported", event.getFeature());
        bus.post(new PromptEvent());
    }

    @Subscribe
    public void onUndeliverable(DeadEvent event) throws IOException {
        println("Unhandled event: %s", event.getEvent());
        quit();
    }

    @Subscribe
    public void onQuit(QuitEvent event) throws IOException {
        println("Bye");
    }

    private void println(String s, Object... arguments) throws IOException {
        println(String.format(s, arguments));
    }

    private void println(String str) throws IOException {
        console.printString(str);
        console.printNewline();
        console.flushConsole();
    }

}
