package org.whiskeysierra.helsing.hadoop;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.whiskeysierra.helsing.util.Formatting;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.List;

final class SelectMapper extends Mapper<LongWritable, Text, Text, Text> {

    private final Text same = new Text("");
    private List<Integer> indices = Lists.newLinkedList();

    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        final String string = context.getConfiguration().get(SideData.PROJECTION, "");

        for (String index : Splitter.on(',').split(string)) {
            indices.add(Integer.valueOf(index));
        }
    }

    @Override
    protected void map(LongWritable ignored, Text value, Context context) throws IOException, InterruptedException {
        final List<String> cells = Input.split(value);
        final List<String> output = Lists.newLinkedList();

        for (Integer index : indices) {
            output.add(cells.get(index));
        }

        context.write(same, new Text(Formatting.JOINER.join(output)));
    }

}
