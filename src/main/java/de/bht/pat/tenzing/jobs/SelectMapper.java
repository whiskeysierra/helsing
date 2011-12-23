package de.bht.pat.tenzing.jobs;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
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

    private final BiMap<String, Integer> projection = HashBiMap.create();

    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        final Configuration config = context.getConfiguration();
        final String string = config.get(SideData.PROJECTION);
        if (string == null) return;

        final Stringifier<Projection> stringifier = new DefaultStringifier<>(config, Projection.class);

        try {
            projection.putAll(stringifier.fromString(string));
        } finally {
            stringifier.close();
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        final Iterable<String> cells = Formatting.SPLITTER.split(value.toString());
        final List<String> output = Lists.newLinkedList();

        int i = 0;
        for (String cell : cells) {
            if (projection.containsValue(i++)) {
                output.add(cell);
            }
        }

        context.write(NullWritable.get(), new Text(Formatting.JOINER.join(output)));
    }

}
