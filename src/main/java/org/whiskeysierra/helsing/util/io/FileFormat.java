package org.whiskeysierra.helsing.util.io;

public interface FileFormat {

    String toString(Iterable<String> line);

    Line lineOf(String value);

}
