package de.bht.pat.tenzing.jobs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DefaultStringifier;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Stringifier;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.File;

public final class Tenzing extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        final Options options = new Options();
        final CmdLineParser parser = new CmdLineParser(options);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            return 1;
        }

        final Configuration conf = getConf();

        final File input = options.getInput();
        final File output = options.getOutput();

        final Stringifier<Projection> stringifier = new DefaultStringifier<>(conf, Projection.class);

        try {
            final Projection projection = new Projection();
            // TODO make dynamic
            projection.put("year", 1);
            projection.put("population", 2);

            // IMPORTANT set parameters before passing the config to the job
            // http://hadoop-common.472056.n3.nabble.com/Configuration-set-Configuration-get-now-working-td103806.html
            conf.set(SideData.PROJECTION, stringifier.toString(projection));
        } finally {
            stringifier.close();
        }

        final Job job = new Job(conf, "Tenzing");

        job.setJarByClass(Tenzing.class);

        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(SelectMapper.class);
        job.setReducerClass(NoopReducer.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(input.getAbsolutePath()));
        FileOutputFormat.setOutputPath(job, new Path(output.getAbsolutePath()));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        final Configuration config = new Configuration();
        int code = ToolRunner.run(config, new Tenzing(), args);
        System.exit(code);
    }

}
