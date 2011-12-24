package de.bht.pat.tenzing.hadoop.jobs;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.bht.pat.tenzing.hadoop.SideData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.List;

public final class GroupOnlyReducer extends Reducer<Text, Text, NullWritable, Text> {

    private List<Integer> indices = Lists.newLinkedList();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        setupIndices(context);
    }

    private void setupIndices(Context context) {
        final Configuration config = context.getConfiguration();
        final String string = config.get(SideData.PROJECTION);
        if (string == null) return;

        for (String index : Splitter.on(',').split(string)) {
            indices.add(Integer.valueOf(index));
        }
    }

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        final Text line = Iterables.getFirst(values, key);
        context.write(NullWritable.get(), line);
    }

}
