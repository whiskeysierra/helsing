package org.whiskeysierra.helsing.util.io;

import org.apache.hadoop.io.Text;

import java.util.List;

public interface Line extends List<String> {

    Line select(Iterable<Integer> indices);

    Text toText();

    String toString();

}
