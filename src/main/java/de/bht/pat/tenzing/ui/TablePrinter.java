package de.bht.pat.tenzing.ui;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.inamik.utils.SimpleTableFormatter;
import com.inamik.utils.TableFormatter;
import de.bht.pat.tenzing.events.ResultEvent;
import jline.ConsoleReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

final class TablePrinter {

    private final ConsoleReader console;

    @Inject
    public TablePrinter(ConsoleReader console) {
        this.console = console;
    }

    @Subscribe
    public void onResult(ResultEvent event) throws IOException {
        final File file = event.getFile();
        // TODO print stream-like, without pushing everything in memory
        final CSVReader reader = new CSVReader(new FileReader(file), ' ');

        final TableFormatter formatter = new SimpleTableFormatter(true);

        while (true) {
            final String[] line = reader.readNext();
            if (line == null) break;

            formatter.nextRow();
            for (String cell : line) {
                formatter.nextCell().addLine(cell);
            }
        }

        final String[] lines = formatter.getFormattedTable();

        for (String line : lines) {
            console.printString(line);
            console.printNewline();
        }
        console.flushConsole();
    }

}
