package de.bht.pat.tenzing.jobs;

import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public final class MaxReducer extends Reducer<String, Long, String, Long> {

    @Override
    protected void reduce(String key, Iterable<Long> values, Context context) throws IOException, InterruptedException {
        long max = Integer.MIN_VALUE;

        for (Long value : values) {
            max = Math.max(max, value);
        }

        context.write(key, max);
    }

}
