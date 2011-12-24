package de.bht.pat.tenzing.hadoop.jobs;

import com.google.common.collect.Iterables;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public final class GroupOnlyReducer extends Reducer<Text, Text, NullWritable, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        context.write(NullWritable.get(), Iterables.getFirst(values, key));
    }

}
