package de.bht.pat.tenzing.mapreduce;

import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public final class GroupByMapper extends Mapper<String, String, String, String> {

    @Override
    protected void map(String key, String value, Context context) throws IOException, InterruptedException {
        // TODO use group by key
    }

}
