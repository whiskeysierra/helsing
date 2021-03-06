package org.whiskeysierra.helsing.ui.cli;

import com.google.common.base.Throwables;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import jline.ConsoleReader;
import org.apache.commons.lang.StringUtils;
import org.whiskeysierra.helsing.events.BootEvent;
import org.whiskeysierra.helsing.events.ExecutionError;
import org.whiskeysierra.helsing.events.FeatureError;
import org.whiskeysierra.helsing.events.InputEvent;
import org.whiskeysierra.helsing.events.PrintEvent;
import org.whiskeysierra.helsing.events.PrintLineEvent;
import org.whiskeysierra.helsing.events.PromptEvent;
import org.whiskeysierra.helsing.events.QuitEvent;
import org.whiskeysierra.helsing.events.ResultPrintedEvent;
import org.whiskeysierra.helsing.events.SqlEvent;
import org.whiskeysierra.helsing.events.SyntaxError;

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
    public void onPrintEvent(PrintEvent event) throws IOException {
        print(event.getLine());
    }

    @Subscribe
    public void onPrintLineEvent(PrintLineEvent event) throws IOException {
        println(event.getLine());
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
    public void onResultPrinted(ResultPrintedEvent event) {
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
