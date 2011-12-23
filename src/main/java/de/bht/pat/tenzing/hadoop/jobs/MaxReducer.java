package de.bht.pat.tenzing.hadoop.jobs;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public final class MaxReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        long max = Integer.MIN_VALUE;

        for (LongWritable value : values) {
            max = Math.max(max, value.get());
        }

        context.write(key, new LongWritable(max));
    }

}
