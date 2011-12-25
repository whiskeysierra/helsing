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

final class SelectMapper extends DependencyInjectionMapper<LongWritable, Text, Writable, Text> {

    private FileFormat format;
    private List<Integer> indices;

    @Inject
    public void setFormat(FileFormat format) {
        this.format = format;
    }

    @Inject
    public void setIndices(@Named(SideData.PROJECTION) List<Integer> indices) {
        this.indices = indices;
    }

    @Override
    protected void map(LongWritable ignored, Text value, Context context) throws IOException, InterruptedException {
        final Line line = format.lineOf(value);

        final Line projection = line.keep(indices);

        context.write(new Text(), projection.toText());
    }

}
