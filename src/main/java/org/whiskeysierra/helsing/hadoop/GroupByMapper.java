package org.whiskeysierra.helsing.hadoop;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.whiskeysierra.helsing.util.io.FileFormat;
import org.whiskeysierra.helsing.util.io.Line;

import java.io.IOException;
import java.util.List;

final class GroupByMapper extends DependencyInjectionMapper<LongWritable, Text, Writable, Text> {

    private FileFormat format;
    private List<Integer> indices;
    private List<Integer> groupIndices;

    @Inject
    public void setFormat(FileFormat format) {
        this.format = format;
    }

    @Inject
    public void setIndices(@Named(SideData.PROJECTION) List<Integer> indices) {
        this.indices = indices;
    }

    @Inject
    public void setGroupIndices(@Named(SideData.GROUPS) List<Integer> groupIndices) {
        this.groupIndices = groupIndices;
    }

    @Override
    protected void map(LongWritable ignored, Text value, Context context) throws IOException, InterruptedException {
        final Line line = format.lineOf(value);

        final Writable group = line.select(groupIndices).toText();
        final Text projection = line.select(indices).toText();

        context.write(group, projection);
    }

}
