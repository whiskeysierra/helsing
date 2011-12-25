package org.whiskeysierra.helsing.hadoop;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.whiskeysierra.helsing.hadoop.functions.Aggregator;
import org.whiskeysierra.helsing.util.inject.MoreProviders;
import org.whiskeysierra.helsing.util.io.FileFormat;
import org.whiskeysierra.helsing.util.io.Line;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

final class AggregatorReducer extends DependencyInjectionReducer<Text, Text, NullWritable, Text> {

    private FileFormat format;
    private Map<Integer, Provider<Aggregator>> aggregators;

    @Inject
    public void setFormat(FileFormat format) {
        this.format = format;
    }

    @Inject
    public void setAggregators(Map<Integer, Provider<Aggregator>> aggregators) {
        this.aggregators = aggregators;
    }

    private Map<Integer, Aggregator> getAggregators() {
        return MoreProviders.get(aggregators);
    }

    @Override
    protected void reduce(Text ignored, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        final Map<Integer, Aggregator> aggregators = getAggregators();

        Line last = null;

        for (Text value : values) {
            final Line line = format.lineOf(value);
            for (Entry<Integer, Aggregator> entry : aggregators.entrySet()) {
                final int index = entry.getKey();
                final Aggregator aggregator = entry.getValue();
                aggregator.update(line.get(index));
            }

            // or last = Objects.firstNonNull(line, last);
            last = line;
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
