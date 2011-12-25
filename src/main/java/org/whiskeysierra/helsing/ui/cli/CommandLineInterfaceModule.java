package org.whiskeysierra.helsing.ui.cli;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import jline.ArgumentCompletor;
import jline.Completor;
import jline.ConsoleReader;
import jline.History;
import jline.SimpleCompletor;

import java.io.File;
import java.io.IOException;
import java.util.List;

final class CommandLineInterfaceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CommandLine.class).asEagerSingleton();
        bind(ResultPrinter.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    public ConsoleReader provideConsole(SchemaCompletor schema, DataCompletor data) throws IOException {
        final ConsoleReader console = new ConsoleReader();

        console.setBellEnabled(false);
        console.setHistory(new History(new File(".history")));

        final List<Completor> completors = Lists.newLinkedList();
        completors.add(new SimpleCompletor("SELECT"));
        // TODO support comma and space between columns
        // TODO support aggregate function
        completors.add(schema);
        completors.add(new SimpleCompletor("FROM"));
        completors.add(data);
        completors.add(new SimpleCompletor("GROUP"));
        completors.add(new SimpleCompletor("BY"));
        completors.add(schema);
        console.addCompletor(new ArgumentCompletor(completors));

        console.setDefaultPrompt("sqlm> ");

        return console;
    }

}
