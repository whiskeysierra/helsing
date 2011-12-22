package de.bht.pat.tenzing.jobs;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public final class SelectMapper extends Mapper<LongWritable, Text, LongWritable, Text> {

    private final Set<Integer> indices = Sets.newHashSet();

    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        final Configuration config = context.getConfiguration();
        final String[] indices = config.getStrings(SideData.SELECT_INDICES, new String[0]);

        for (String index : indices) {
            this.indices.add(Integer.valueOf(index));
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        final Iterable<String> cells = Formatting.SPLITTER.split(value.toString());
        final List<String> output = Lists.newLinkedList();

        int i = 0;
        for (String cell : cells) {
            if (indices.contains(i++)) {
                output.add(cell);
            }
        }

        context.write(key, new Text(Formatting.JOINER.join(output)));
    }

}
