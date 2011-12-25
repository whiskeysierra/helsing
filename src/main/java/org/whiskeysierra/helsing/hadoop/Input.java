package org.whiskeysierra.helsing.hadoop;

import com.google.common.collect.Lists;
import org.whiskeysierra.helsing.util.io.Formatting;
import org.apache.hadoop.io.Text;

import java.util.List;

final class Input {

    public static List<String> split(Text value) {
        return Lists.newLinkedList(Formatting.SPLITTER.split(value.toString()));
    }

    public static String join(Iterable<String> values) {
        return Formatting.JOINER.join(values);
    }

}
