package org.whiskeysierra.helsing.hadoop;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.whiskeysierra.helsing.hadoop.functions.Aggregator;
import org.whiskeysierra.helsing.hadoop.io.Serializer;
import org.whiskeysierra.helsing.hadoop.io.Types;
import org.whiskeysierra.helsing.hadoop.io.Types.FunctionDefinition;
import org.whiskeysierra.helsing.hadoop.io.Types.Functions;
import org.whiskeysierra.helsing.hadoop.io.Types.Indices;
import org.whiskeysierra.helsing.util.format.FileFormat;
import org.whiskeysierra.helsing.util.format.Line;
import org.whiskeysierra.helsing.util.inject.MoreProviders;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class AggregatorReducer extends DependencyInjectionReducer<Writable, Text, NullWritable, Text> {

    private FileFormat format;
    private Serializer serializer;
    private Map<String, Provider<Aggregator>> functions;

    private final Map<Integer, Provider<Aggregator>> aggregators = Maps.newHashMap();
    private List<Integer> groups;
    private Functions indices;

    @Inject
    public void setFormat(FileFormat format) {
        this.format = format;
    }

    @Inject
    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    @Inject
    public void setFunctions(@org.whiskeysierra.helsing.api.Functions Map<String, Provider<Aggregator>> functions) {
        this.functions = functions;
    }

    @Override
    protected void configure(Context context) throws IOException, InterruptedException {
        final Configuration config = context.getConfiguration();
        this.groups = serializer.deserialize(config.get(SideData.GROUPS), Indices.class);

        this.indices = serializer.deserialize(config.get(SideData.FUNCTIONS), Types.Functions.class);

        for (int index : indices.keySet()) {
            final FunctionDefinition definition = indices.get(index);
            aggregators.put(index, functions.get(definition.getName()));
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

                aggregator.update(line.select(indices.get(index).getColumns()));
            }

            last = Objects.firstNonNull(last, line);
        }

        assert last != null;

        for (Entry<Integer, Aggregator> entry : aggregators.entrySet()) {
            final int index = entry.getKey();
            final Aggregator aggregator = entry.getValue();
            last.set(index, String.valueOf(aggregator.getResult()));
        }

        context.write(NullWritable.get(), last.toText());
    }
}
