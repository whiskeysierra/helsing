package de.bht.pat.tenzing.hadoop;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import de.bht.pat.tenzing.hadoop.functions.Aggregator;
import de.bht.pat.tenzing.hadoop.functions.FunctionsModule;
import de.bht.pat.tenzing.inject.Functions;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

final class AggregatorReducer extends Reducer<Text, Text, NullWritable, Text> {

    private final Map<Integer, String> indices = Maps.newHashMap();
    private final Map<String, Provider<Aggregator>> functions = Maps.newHashMap();

    @Inject
    public void setFunctions(@Functions Map<String, Provider<Aggregator>> functions) {
        this.functions.putAll(functions);
    }

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        final Injector injector = Guice.createInjector(new FunctionsModule());
        injector.getMembersInjector(AggregatorReducer.class).injectMembers(this);

        final Configuration config = context.getConfiguration();
        final String string = config.get(SideData.FUNCTIONS, "");

        final Splitter.MapSplitter splitter = Splitter.on(",").withKeyValueSeparator("=");

        for (Map.Entry<String, String> entry : splitter.split(string).entrySet()) {
            final int index = Integer.parseInt(entry.getKey());
            final String functionName = entry.getValue();
            indices.put(index, functionName);
        }
    }

    private Map<Integer, Aggregator> getAggregators() {
        final Map<Integer, Aggregator> aggregators = Maps.newHashMap();

        for (Map.Entry<Integer, String> entry : indices.entrySet()) {
            aggregators.put(entry.getKey(), functions.get(entry.getValue()).get());
        }

        return aggregators;
    }

    @Override
    protected void reduce(Text ignored, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        final Map<Integer, Aggregator> aggregators = getAggregators();

        Text last = null;

        for (Text value : values) {
            final List<String> cells = Input.split(value);
            for (Map.Entry<Integer, Aggregator> entry : aggregators.entrySet()) {
                final int index = entry.getKey();
                final Aggregator aggregator = entry.getValue();
                aggregator.update(cells.get(index));
            }

            last = value;
        }

        final List<String> cells = Input.split(last);

        for (Map.Entry<Integer, Aggregator> entry : aggregators.entrySet()) {
            final int index = entry.getKey();
            final Aggregator aggregator = entry.getValue();
            cells.set(index, aggregator.getResult());
        }

        context.write(NullWritable.get(), new Text(Input.join(cells)));
    }

}
