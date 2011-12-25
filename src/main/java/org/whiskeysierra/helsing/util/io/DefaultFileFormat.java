package org.whiskeysierra.helsing.util.io;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

final class DefaultFileFormat implements FileFormat {

    private final Provider<Line> newLine;
    private final Splitter splitter;
    private final Joiner joiner;

    @Inject
    public DefaultFileFormat(Provider<Line> newLine,
        @Named("data.separator") Splitter splitter,
        @Named("data.separator") Joiner joiner) {

        this.newLine = newLine;
        this.splitter = splitter;
        this.joiner = joiner;
    }

    @Override
    public String toString(Iterable<String> parts) {
        return joiner.join(parts);
    }

    @Override
    public Line lineOf(String value) {
        final Line line = newLine.get();
        final Iterable<String> parts = splitter.split(value);
        Iterables.addAll(line, parts);
        return line;
    }

}
