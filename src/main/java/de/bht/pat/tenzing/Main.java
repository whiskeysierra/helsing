package de.bht.pat.tenzing;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.inamik.utils.SimpleTableFormatter;
import com.inamik.utils.TableFormatter;
import de.bht.pat.tenzing.sql.DefaultSqlValidator;
import de.bht.pat.tenzing.sql.SqlValidator;
import jline.ArgumentCompletor;
import jline.Completor;
import jline.ConsoleReader;
import jline.SimpleCompletor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public final class Main {

    public static void main(String[] args) throws IOException {
        final ConsoleReader reader = new ConsoleReader();

        final List<Completor> completors = ImmutableList.<Completor>of(
                new SimpleCompletor(new String[]{
                        "SELECT", "FROM", "GROUP BY"
                })
        );

        reader.addCompletor(new ArgumentCompletor(completors));

        final SqlValidator validator = new DefaultSqlValidator();

        while (true) {
            final String line = reader.readLine("$ ");

            if ("quit".equalsIgnoreCase(line)) {
                break;
            } else {
                try {
                    validator.validate(line);

                    final TableFormatter formatter = new SimpleTableFormatter(true);
                    formatter.nextRow();
                    formatter.nextCell().addLine(".");

                    String[] table = formatter.getFormattedTable();
                    for (String l : table) {
                        reader.printString(l);
                    }

                    reader.printNewline();
                    reader.flushConsole();
                } catch (SQLException e) {
                    reader.printString("You have an error in your SQL syntax.");
                    reader.printNewline();
                    reader.flushConsole();
                }
            }
        }

        reader.printString("Good bye.");
        reader.flushConsole();
    }

}
