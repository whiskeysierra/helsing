package org.whiskeysierra.helsing.hadoop;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.whiskeysierra.helsing.hadoop.functions.Aggregator;
import org.whiskeysierra.helsing.util.inject.MoreProviders;
import org.whiskeysierra.helsing.util.io.FileFormat;
import org.whiskeysierra.helsing.util.io.Line;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class AggregatorReducer extends DependencyInjectionReducer<Writable, Text, NullWritable, Text> {

    private FileFormat format;
    private Map<Integer, Provider<Aggregator>> aggregators;
    private List<Integer> groups = Collections.emptyList();

    @Inject
    public void setFormat(FileFormat format) {
        this.format = format;
    }

    @Inject
    public void setAggregators(Map<Integer, Provider<Aggregator>> aggregators) {
        this.aggregators = aggregators;
    }

    @Inject(optional = true)
    public void setGroups(@Named(SideData.GROUPS) List<Integer> groups) {
        this.groups = groups;
    }

    private Map<Integer, Aggregator> getAggregators() {
        return MoreProviders.get(aggregators);
    }

    @Override
    protected void reduce(Writable ignored, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        if (groups.isEmpty() && aggregators.isEmpty()) {
            identity(values, context);
        } else {
            aggregate(values, context);
        }
    }

    private void identity(Iterable<Text> values, Context context) throws IOException, InterruptedException {
        for (Text value : values) {
            context.write(NullWritable.get(), value);
        }
    }

    private void aggregate(Iterable<Text> values, Context context) throws IOException, InterruptedException {
        final Map<Integer, Aggregator> aggregators = getAggregators();

        Line last = null;

        for (Text value : values) {
            final Line line = format.lineOf(value);
            for (Entry<Integer, Aggregator> entry : aggregators.entrySet()) {
                final int index = entry.getKey();
                final Aggregator aggregator = entry.getValue();
                aggregator.update(line.get(index));
            }

            last = Objects.firstNonNull(last, line);
        }

        assert last != null;

        for (Entry<Integer, Aggregator> entry : aggregators.entrySet()) {
            final int index = entry.getKey();
            final Aggregator aggregator = entry.getValue();
            last.set(index, aggregator.getResult());
        }

        context.write(NullWritable.get(), last.toText());
    }

}
