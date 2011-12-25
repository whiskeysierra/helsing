package org.whiskeysierra.helsing.util.io;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import org.apache.hadoop.io.Text;

final class DefaultFileFormat implements FileFormat {

    private final Provider<Line> provider;
    private final Splitter splitter;

    @Inject
    public DefaultFileFormat(Provider<Line> provider, @Named("data.separator") Splitter splitter) {
        this.provider = provider;
        this.splitter = splitter;
    }

    @Override
    public Line lineOf() {
        return provider.get();
    }

    @Override
    public Line lineOf(Text text) {
        return lineOf(text.toString());
    }

    @Override
    public Line lineOf(String value) {
        return lineOf(splitter.split(value));
    }

    @Override
    public Line lineOf(Iterable<String> values) {
        final Line line = provider.get();
        Iterables.addAll(line, values);
        return line;
    }


}
