package de.bht.pat.tenzing.jobs;

import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public final class CountReducer extends Reducer<String, Long, String, Long> {

    @Override
    protected void reduce(String key, Iterable<Long> values, Context context) throws IOException, InterruptedException {
        long count = 0;

        for (Long value : values) {
            count++;
        }

        context.write(key, count);
    }

}
