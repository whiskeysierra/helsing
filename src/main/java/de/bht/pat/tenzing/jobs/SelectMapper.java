package de.bht.pat.tenzing.jobs;

import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public final class SelectMapper extends Mapper<String, String, String, String> {

    @Override
    protected void map(String key, String value, Context context) throws IOException, InterruptedException {
        // TODO use key, but select only requested columns
    }

}
