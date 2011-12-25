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
    private Integer groupIndex;

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

    @Inject(optional = true)
    public void setGroupIndex(@Named(SideData.GROUP) Integer groupIndex) {
        this.groupIndex = groupIndex;
    }

    @Override
    protected void map(LongWritable ignored, Text value, Context context) throws IOException, InterruptedException {
        final Line line = format.lineOf(value);

        //final Writable group = new Text(line.get(groupIndex));
        //final Writable group = new Text(line.get(groupIndices.get(0)));

        final Text[] groups = new Text[groupIndices.size()];

        for (int i = 0; i < groupIndices.size(); i++) {
            groups[i] = new Text(line.get(groupIndices.get(i)));
        }

        final Writable group = new TextArray(groups);
        final Text projection = line.keep(indices).toText();

        context.write(group, projection);
    }
}
