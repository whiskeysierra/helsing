package org.whiskeysierra.helsing.hadoop;

import com.google.inject.Inject;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.whiskeysierra.helsing.hadoop.io.Serializer;
import org.whiskeysierra.helsing.hadoop.io.Types;
import org.whiskeysierra.helsing.util.format.FileFormat;
import org.whiskeysierra.helsing.util.format.Line;

import java.io.IOException;
import java.util.List;

final class GroupByMapper extends DependencyInjectionMapper<LongWritable, Text, Text, Text> {

    private FileFormat format;
    private Serializer serializer;

    private List<Integer> indices;
    private List<Integer> groups;

    @Inject
    public void setFormat(FileFormat format) {
        this.format = format;
    }

    @Inject
    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void configure(Context context) throws IOException, InterruptedException {
        this.indices = serializer.deserialize(context.getConfiguration().get(SideData.PROJECTION), Types.Indices.class);
        this.groups = serializer.deserialize(context.getConfiguration().get(SideData.GROUPS), Types.Indices.class);
    }

    @Override
    protected void map(LongWritable ignored, Text value, Context context) throws IOException, InterruptedException {
        final Line line = format.lineOf(value);

        final Text group = line.select(groups).toText();
        final Text projection = line.select(indices).toText();

        context.write(group, projection);
    }
}
