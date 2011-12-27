package org.whiskeysierra.helsing.util.format;

import org.apache.hadoop.io.Text;

import java.util.List;

public interface Line extends List<String> {

    Line select(Integer... indices);

    Line select(Iterable<Integer> indices);

    Text toText();

    String toString();

}
