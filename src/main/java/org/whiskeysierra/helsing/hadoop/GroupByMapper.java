package org.whiskeysierra.helsing.hadoop;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.whiskeysierra.helsing.util.io.FileFormat;
import org.whiskeysierra.helsing.util.io.Line;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

final class GroupByMapper extends DependencyInjectionMapper<LongWritable, Text, Text, Text> {

    private FileFormat format;
    private List<Integer> indices;
    private List<Integer> groups = Collections.emptyList();

    @Inject
    public void setFormat(FileFormat format) {
        this.format = format;
    }

    @Inject
    public void setIndices(@Named(SideData.PROJECTION) List<Integer> indices) {
        this.indices = indices;
    }

    @Inject(optional = true)
    public void setGroups(@Named(SideData.GROUPS) List<Integer> groups) {
        this.groups = groups;
    }

    @Override
    protected void map(LongWritable ignored, Text value, Context context) throws IOException, InterruptedException {
        final Line line = format.lineOf(value);

        final Text group = line.select(groups).toText();
        final Text projection = line.select(indices).toText();

        context.write(group, projection);
    }

}
