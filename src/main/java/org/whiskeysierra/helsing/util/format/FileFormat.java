package org.whiskeysierra.helsing.util.format;

import org.apache.hadoop.io.Text;

public interface FileFormat {

    Line lineOf();

    Line lineOf(Text value);

    Line lineOf(String value);

    Line lineOf(Iterable<String> values);
}
