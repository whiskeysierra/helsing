package org.whiskeysierra.helsing.hadoop;

import com.google.common.base.Splitter;
import com.google.common.base.Splitter.MapSplitter;
import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import org.whiskeysierra.helsing.hadoop.functions.Aggregator;
import org.whiskeysierra.helsing.hadoop.functions.FunctionsModule;
import org.whiskeysierra.helsing.inject.Functions;
import org.whiskeysierra.helsing.inject.MoreProviders;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class AggregatorReducer extends Reducer<Text, Text, NullWritable, Text> {

    private final Map<Integer, Provider<Aggregator>> aggregators = Maps.newHashMap();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        final Injector injector = Guice.createInjector(new FunctionsModule());

        final Map<String, Provider<Aggregator>> functions = injector.getInstance(
            Key.get(new TypeLiteral<Map<String, Provider<Aggregator>>>() {
            }, Functions.class)
        );

        final String string = context.getConfiguration().get(SideData.FUNCTIONS, "");

        final MapSplitter splitter = Splitter.on(",").withKeyValueSeparator("=");

        for (Map.Entry<String, String> entry : splitter.split(string).entrySet()) {
            final int index = Integer.parseInt(entry.getKey());
            final String functionName = entry.getValue();

            aggregators.put(index, functions.get(functionName));
        }
    }

    private Map<Integer, Aggregator> getAggregators() {
        return MoreProviders.get(aggregators);
    }

    @Override
    protected void reduce(Text ignored, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        final Map<Integer, Aggregator> aggregators = getAggregators();

        Text last = null;

        for (Text value : values) {
            final List<String> cells = Input.split(value);
            for (Entry<Integer, Aggregator> entry : aggregators.entrySet()) {
                final int index = entry.getKey();
                final Aggregator aggregator = entry.getValue();
                aggregator.update(cells.get(index));
            }

            last = value;
        }

        final List<String> cells = Input.split(last);

        for (Entry<Integer, Aggregator> entry : aggregators.entrySet()) {
            final int index = entry.getKey();
            final Aggregator aggregator = entry.getValue();
            cells.set(index, aggregator.getResult());
        }

        context.write(NullWritable.get(), new Text(Input.join(cells)));
    }

}
