package de.bht.pat.tenzing.jobs;

import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public final class NoopReducer extends Reducer<String, String, String, String> {

    @Override
    protected void reduce(String key, Iterable<String> values, Context context) throws IOException, InterruptedException {
        for (String value : values) {
            context.write(key, value);
        }
    }

}
