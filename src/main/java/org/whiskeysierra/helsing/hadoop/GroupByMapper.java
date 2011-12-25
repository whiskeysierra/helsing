package org.whiskeysierra.helsing.hadoop;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.whiskeysierra.helsing.util.Formatting;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.List;

final class GroupByMapper extends Mapper<LongWritable, Text, Text, Text> {

    private List<Integer> indices = Lists.newLinkedList();
    private Integer groupIndex;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        setupIndices(context);
        setupGroupIndex(context);
    }

    private void setupIndices(Context context) {
        final String string = context.getConfiguration().get(SideData.PROJECTION, "");

        for (String index : Splitter.on(',').split(string)) {
            indices.add(Integer.valueOf(index));
        }
    }

    private void setupGroupIndex(Context context) {
        this.groupIndex = context.getConfiguration().getInt(SideData.GROUP, -1);
    }

    @Override
    protected void map(LongWritable ignored, Text line, Context context) throws IOException, InterruptedException {
        final List<String> cells = Input.split(line);
        final List<String> output = Lists.newLinkedList();

        for (Integer index : indices) {
            output.add(cells.get(index));
        }

        final Text key = new Text(cells.get(groupIndex));
        final Text value = new Text(Formatting.JOINER.join(output));
        context.write(key, value);
    }

}
