package de.bht.pat.tenzing.cli;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public final class DefaultTablePrinterTest {

    private final String expected =
        "+--+----+--------+\n" +
        "|DE|1900|56400000|\n" +
        "+--+----+--------+\n" +
        "|UK|1900|41600000|\n" +
        "+--+----+--------+\n" +
        "|IT|1900|32400000|\n" +
        "+--+----+--------+\n" +
        "|DE|1950|50800000|\n" +
        "+--+----+--------+\n" +
        "|UK|1950|50200000|\n" +
        "+--+----+--------+\n" +
        "|IT|1950|46800000|\n" +
        "+--+----+--------+\n";

    @Test
    public void print() throws IOException {
        final Writer writer = new StringWriter();
        final TablePrinter printer = new DefaultTablePrinter(writer);
        final File file = new File("src/test/resources/result.csv");
        printer.print(file);
        writer.flush();

        Assert.assertEquals(expected, writer.toString());
    }

}
