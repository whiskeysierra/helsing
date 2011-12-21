package de.bht.pat.tenzing.cli;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.Table;
import com.google.inject.Inject;
import com.inamik.utils.SimpleTableFormatter;
import com.inamik.utils.TableFormatter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

final class DefaultTablePrinter implements TablePrinter {

    private final Writer writer;

    @Inject
    public DefaultTablePrinter(@Console Writer writer) {
        this.writer = writer;
    }

    @Override
    public void print(File file) throws IOException {
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
            writer.write(line);
            writer.write("\n");
        }
    }

}
