package de.bht.pat.tenzing.hadoop.jobs;

import de.bht.pat.tenzing.util.Formatting;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.util.List;

@AggregateFunction("STDDEV")
public final class StddevReducer extends AbstractReducer {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        long sum = 0;
        long count = 0;
        long squared = 0;

        Text last = key;

        for (Text line : values) {
            last = line;
            final List<String> cells = Input.split(line);
            final long value = Long.parseLong(cells.get(index()));
            sum += value;
            squared += value * value;
            count++;
        }

        final long avg = sum / count;
        final long var = Math.abs(squared - avg * sum) / count;
        final long stddev = (long) Math.sqrt(var);

        final List<String> cells = Input.split(last);
        cells.set(index(), Long.toString(stddev));

        context.write(NullWritable.get(), new Text(Formatting.JOINER.join(cells)));
    }

}
