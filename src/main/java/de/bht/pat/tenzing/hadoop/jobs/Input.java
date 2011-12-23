package de.bht.pat.tenzing.hadoop.jobs;

import com.google.common.collect.Lists;
import de.bht.pat.tenzing.util.Formatting;
import org.apache.hadoop.io.Text;

import java.util.List;

final class Input {

    public static List<String> split(Text value) {
        return Lists.newLinkedList(Formatting.SPLITTER.split(value.toString()));
    }


}
