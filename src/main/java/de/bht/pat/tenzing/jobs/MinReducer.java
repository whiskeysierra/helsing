package de.bht.pat.tenzing.jobs;

import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public final class MinReducer extends Reducer<String, Long, String, Long> {

    @Override
    protected void reduce(String key, Iterable<Long> values, Context context) throws IOException, InterruptedException {
        long min = Integer.MAX_VALUE;

        for (Long value : values) {
            min = Math.min(min, value);
        }

        context.write(key, min);
    }

}
