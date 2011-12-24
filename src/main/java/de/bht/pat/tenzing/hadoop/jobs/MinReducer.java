package de.bht.pat.tenzing.hadoop.jobs;

import de.bht.pat.tenzing.util.Formatting;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.util.List;

@AggregateFunction("MIN")
public final class MinReducer extends AbstractReducer {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        long min = Integer.MAX_VALUE;

        Text last = key;

        for (Text line : values) {
            last = line;
            final List<String> cells = Input.split(line);
            min = Math.min(min, Long.parseLong(cells.get(index())));
        }

        final List<String> cells = Input.split(last);
        cells.set(index(), Long.toString(min));

        context.write(NullWritable.get(), new Text(Formatting.JOINER.join(cells)));
    }

}
