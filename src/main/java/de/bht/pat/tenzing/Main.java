package de.bht.pat.tenzing;

import com.google.common.collect.ImmutableList;
import de.bht.pat.tenzing.mapreduce.AvgReducer;
import jline.ArgumentCompletor;
import jline.Completor;
import jline.ConsoleReader;
import jline.SimpleCompletor;

import java.io.IOException;
import java.util.List;

public final class Main {

    public static void main(String[] args) throws IOException {
        final ConsoleReader reader = new ConsoleReader();

        final List<Completor> completors = ImmutableList.<Completor>of(
                new SimpleCompletor(new String[] {
                        "SELECT", "FROM", "GROUP BY"
                })
        );

        reader.addCompletor(new ArgumentCompletor(completors));

        while (true) {
            final String line = reader.readLine("$ ");

            if ("quit".equalsIgnoreCase(line)) {
                break;
            }
        }

        System.out.println(AvgReducer.class.getSuperclass());
    }

}
