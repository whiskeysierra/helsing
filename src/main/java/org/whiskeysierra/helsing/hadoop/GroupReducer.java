package org.whiskeysierra.helsing.hadoop;

import com.google.common.collect.Iterables;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * This reducer performs grouping with applying any aggregate function.
 */
final class GroupReducer extends Reducer<Text, Text, NullWritable, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        context.write(NullWritable.get(), Iterables.getFirst(values, key));
    }

}
