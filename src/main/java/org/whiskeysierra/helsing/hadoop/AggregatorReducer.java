package org.whiskeysierra.helsing.hadoop;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.whiskeysierra.helsing.api.Functions;
import org.whiskeysierra.helsing.hadoop.functions.Aggregator;
import org.whiskeysierra.helsing.util.inject.MoreProviders;
import org.whiskeysierra.helsing.util.io.FileFormat;
import org.whiskeysierra.helsing.util.io.Line;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class AggregatorReducer extends DependencyInjectionReducer<Writable, Text, NullWritable, Text> {

    private FileFormat format;
    private Map<String, Provider<Aggregator>> functions;

    private final Map<Integer, Provider<Aggregator>> aggregators = Maps.newHashMap();
    private List<Integer> groups;

    @Inject
    public void setFormat(FileFormat format) {
        this.format = format;
    }

    @Inject
    public void setFunctions(@Functions Map<String, Provider<Aggregator>> functions) {
        this.functions = functions;
    }

    @Override
    protected void configure(Context context) throws IOException, InterruptedException {
        final Configuration config = context.getConfiguration();
        this.groups = SideData.deserializeList(config.get(SideData.GROUPS));

        final Map<Integer, String> indices = SideData.deserializeMap(config.get(SideData.FUNCTIONS));

        for (Map.Entry<Integer, String> entry : indices.entrySet()) {
            final int index = entry.getKey();
            final String functionName = entry.getValue();

            aggregators.put(index, functions.get(functionName));
        }
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
                aggregator.update(line.select(index));
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
