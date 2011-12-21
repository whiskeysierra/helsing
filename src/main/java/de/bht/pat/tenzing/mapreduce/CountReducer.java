package de.bht.pat.tenzing.mapreduce;

import com.google.common.collect.Iterables;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public final class CountReducer extends Reducer<String, Long, String, Long> {

    @Override
    protected void reduce(String key, Iterable<Long> values, Context context) throws IOException, InterruptedException {
        long count = Iterables.size(values);
        context.write(key, count);
    }

}
