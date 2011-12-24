package de.bht.pat.tenzing.hadoop.jobs;

import com.google.common.collect.Lists;
import de.bht.pat.tenzing.util.Formatting;
import org.apache.hadoop.io.Text;

import java.util.List;

public final class Input {

    public static List<String> split(Text value) {
        return Lists.newLinkedList(Formatting.SPLITTER.split(value.toString()));
    }

    public static String join(Iterable<String> values) {
        return Formatting.JOINER.join(values);
    }

}
