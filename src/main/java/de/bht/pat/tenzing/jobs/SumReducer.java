package de.bht.pat.tenzing.jobs;

import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public final class SumReducer extends Reducer<String, Long, String, Long> {

    @Override
    protected void reduce(String key, Iterable<Long> values, Context context) throws IOException, InterruptedException {
        long sum = 0;

        for (Long value : values) {
            sum += value;
        }

        context.write(key, sum);
    }

}
