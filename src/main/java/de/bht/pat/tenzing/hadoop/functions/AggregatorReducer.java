package de.bht.pat.tenzing.hadoop.functions;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import de.bht.pat.tenzing.hadoop.SideData;
import de.bht.pat.tenzing.hadoop.jobs.Functions;
import de.bht.pat.tenzing.hadoop.jobs.Input;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public final class AggregatorReducer extends Reducer<Text, Text, NullWritable, Text> {

    // column index to aggregator
    private final Map<Integer, Aggregator> aggregators = Maps.newHashMap();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        final Injector injector = Guice.createInjector(new FunctionsModule());

        // function name to aggregator
        final Map<String, Aggregator> map = injector.getInstance(
            Key.get(new TypeLiteral<Map<String, Aggregator>>() {}, Functions.class));

        final Configuration config = context.getConfiguration();
        final String string = config.get(SideData.FUNCTION_INDICES);
        if (string == null) return;

        final Splitter.MapSplitter splitter = Splitter.on(",").withKeyValueSeparator("=");

        // column index to function name
        for (Map.Entry<String, String> entry : splitter.split(string).entrySet()) {
            final int index = Integer.parseInt(entry.getKey());
            final String functionName = entry.getValue();

            aggregators.put(index, map.get(functionName));
        }
    }

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Text last = null;

        for (Text text : values) {
            final List<String> cells = Input.split(text);
            for (Map.Entry<Integer, Aggregator> entry : aggregators.entrySet()) {
                final int index = entry.getKey();
                final Aggregator aggregator = entry.getValue();
                final String value = cells.get(index);
                aggregator.update(value);
            }

            last = text;
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
