package de.bht.pat.tenzing.jobs;

import com.google.common.base.Splitter;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import de.bht.pat.tenzing.util.Formatting;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DefaultStringifier;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Stringifier;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.List;

public final class SelectMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

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
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        final List<String> cells = split(value);
        final List<String> output = Lists.newLinkedList();

        for (int index : indices) {
            output.add(cells.get(index));
        }

        context.write(NullWritable.get(), new Text(Formatting.JOINER.join(output)));
    }

    private List<String> split(Text value) {
        return Lists.newLinkedList(Formatting.SPLITTER.split(value.toString()));
    }

}
