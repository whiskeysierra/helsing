package de.bht.pat.tenzing.hadoop.jobs;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public final class MinReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        long min = Integer.MAX_VALUE;

        for (LongWritable value : values) {
            min = Math.min(min, value.get());
        }

        context.write(key, new LongWritable(min));
    }

}
