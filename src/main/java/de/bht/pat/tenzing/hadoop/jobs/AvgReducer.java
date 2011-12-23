package de.bht.pat.tenzing.hadoop.jobs;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public final class AvgReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        long sum = 0;
        long count = 0;

        for (LongWritable value : values) {
            sum += value.get();
            count++;
        }

        context.write(key, new LongWritable(sum / count));
    }

}
