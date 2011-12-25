package org.whiskeysierra.helsing.util.io;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.hadoop.io.Text;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

final class DefaultLine extends ForwardingList<String> implements Line {

    private final List<String> parts = Lists.newArrayList();
    private final Joiner joiner;
    private final Splitter splitter;

    @Inject
    public DefaultLine(@Named("data.separator") Joiner joiner, @Named("data.separator") Splitter splitter) {
        this.joiner = joiner;
        this.splitter = splitter;
    }

    @Override
    protected List<String> delegate() {
        return parts;
    }

    @Override
    public void write(DataOutput output) throws IOException {
        final String value = joiner.join(parts);
        final Text text = new Text(value);
        text.write(output);
    }

    @Override
    public void readFields(DataInput input) throws IOException {
        final Text text = new Text();
        text.readFields(input);
        final String value = text.toString();
        final Iterable<String> values = splitter.split(value);
        Iterables.addAll(parts, values);
    }

}
