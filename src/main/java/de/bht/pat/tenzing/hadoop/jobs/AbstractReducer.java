package de.bht.pat.tenzing.hadoop.jobs;

import de.bht.pat.tenzing.hadoop.SideData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

abstract class AbstractReducer extends Reducer<Text, Text, NullWritable, Text> {

    private int index = 0;

    @Override
    protected void setup(Reducer.Context context) throws IOException, InterruptedException {
        final Configuration config = context.getConfiguration();
        final String string = config.get(SideData.FUNCTION_INDEX);
        if (string == null) return;
        this.index = Integer.parseInt(string);
    }

    public int index() {
        return index;
    }

}
