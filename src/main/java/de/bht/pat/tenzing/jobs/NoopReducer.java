package de.bht.pat.tenzing.jobs;

import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public final class NoopReducer extends Reducer<String, Void, String, Void> {

    @Override
    protected void reduce(String key, Iterable<Void> values, Context context) throws IOException, InterruptedException {
        context.write(key, null);
    }

}