package de.bht.pat.tenzing.hadoop.jobs;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import de.bht.pat.tenzing.hadoop.SideData;
import de.bht.pat.tenzing.util.Formatting;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.List;

public final class SelectMapper extends Mapper<LongWritable, Text, Text, Text> {

    private final Text same = new Text("");
    private List<Integer> indices = Lists.newLinkedList();

    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        final Configuration config = context.getConfiguration();
        final String string = config.get(SideData.PROJECTION);
        if (string == null) return;

        for (String index : Splitter.on(',').split(string)) {
            indices.add(Integer.valueOf(index));
        }
    }

    @Override
    protected void map(LongWritable ignored, Text value, Context context) throws IOException, InterruptedException {
        final List<String> cells = Input.split(value);
        final List<String> output = Lists.newLinkedList();

        for (int index : indices) {
            output.add(cells.get(index));
        }

        context.write(same, new Text(Formatting.JOINER.join(output)));
    }

}
