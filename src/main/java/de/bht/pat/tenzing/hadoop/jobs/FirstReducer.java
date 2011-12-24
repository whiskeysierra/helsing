package de.bht.pat.tenzing.hadoop.jobs;

import com.google.common.collect.Iterables;
import de.bht.pat.tenzing.util.Formatting;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.List;

public final class FirstReducer extends Reducer<Text, Text, NullWritable, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        final Text line = Iterables.getFirst(values, null);
        assert line != null;
        context.write(NullWritable.get(), line);
    }

}
