package org.whiskeysierra.helsing.hadoop;

import com.google.inject.Inject;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Stringifier;
import org.apache.hadoop.io.Text;
import org.whiskeysierra.helsing.util.io.FileFormat;
import org.whiskeysierra.helsing.util.io.Line;

import java.io.IOException;
import java.util.List;

final class GroupByMapper extends DependencyInjectionMapper<LongWritable, Text, Text, Text> {

    private FileFormat format;
    private Stringifier<List<Integer>> stringifier;

    private List<Integer> indices;
    private List<Integer> groups;

    @Inject
    public void setFormat(FileFormat format) {
        this.format = format;
    }

    @Inject
    public void setStringifier(Stringifier<List<Integer>> stringifier) {
        this.stringifier = stringifier;
    }

    @Override
    protected void configure(Context context) throws IOException, InterruptedException {
        this.indices = stringifier.fromString(context.getConfiguration().get(SideData.PROJECTION));
        this.groups = stringifier.fromString(context.getConfiguration().get(SideData.GROUPS));
    }

    @Override
    protected void map(LongWritable ignored, Text value, Context context) throws IOException, InterruptedException {
        final Line line = format.lineOf(value);

        final Text group = line.select(groups).toText();
        final Text projection = line.select(indices).toText();

        context.write(group, projection);
    }
}
