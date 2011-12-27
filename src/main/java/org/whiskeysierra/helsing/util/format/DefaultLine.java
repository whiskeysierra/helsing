package org.whiskeysierra.helsing.util.format;

import com.google.common.base.Joiner;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.hadoop.io.Text;

import java.util.Arrays;
import java.util.List;

final class DefaultLine extends ForwardingList<String> implements Line {

    private final List<String> values = Lists.newArrayList();
    private final Joiner joiner;
    private final FileFormat format;

    @Inject
    public DefaultLine(@Named("data.separator") Joiner joiner, FileFormat format) {
        this.joiner = joiner;
        this.format = format;
    }

    @Override
    protected List<String> delegate() {
        return values;
    }

    @Override
    public Line select(Integer... indices) {
        return select(Arrays.asList(indices));
    }

    @Override
    public Line select(Iterable<Integer> indices) {
        final Line line = format.lineOf();

        for (int index : indices) {
            line.add(get(index));
        }

        return line;
    }

    @Override
    public Text toText() {
        return new Text(toString());
    }

    @Override
    public String toString() {
        return joiner.join(values);
    }

}
