package org.whiskeysierra.helsing.hadoop;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
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
import org.whiskeysierra.helsing.api.sql.SelectStatement;
import org.whiskeysierra.helsing.api.sql.SqlColumn;
import org.whiskeysierra.helsing.api.sql.SqlExpression;
import org.whiskeysierra.helsing.api.sql.SqlFunction;
import org.whiskeysierra.helsing.api.sql.SqlParser;

import java.io.File;
import java.util.List;
import java.util.Map;

public final class Hadoop extends Configured implements Tool {

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

        // TODO boot up injector in own class and move stuff below there
        final Injector injector = Guice.createInjector(new HadoopModule());
        final SqlParser sqlParser = injector.getInstance(SqlParser.class);

        final Configuration config = getConf();

        final File input = options.getInput();
        final File output = options.getOutput();

        final String query = options.getQuery();
        final SelectStatement statement = sqlParser.parse(query);

        final File schema = new File(input.getParentFile(), input.getName().replace(".csv", ".schema.csv"));
        final List<String> columns = Files.readLines(schema, Charsets.UTF_8);

        @Deprecated
        final List<Integer> projection = Lists.newLinkedList();

        final Map<Integer, String> selections = Maps.newHashMap();
        final Map<Integer, String> functions = Maps.newHashMap();

        for (SqlExpression expression : statement.projection()) {
            if (expression.is(SqlColumn.class)) {
                final SqlColumn column = expression.as(SqlColumn.class);
                final String name = column.name();
                projection.add(columns.indexOf(name));
                selections.put(columns.indexOf(name), name);
            } else if (expression.is(SqlFunction.class)) {
                final SqlFunction function = expression.as(SqlFunction.class);
                final String name = function.column().name();
                projection.add(columns.indexOf(name));
                selections.put(columns.indexOf(name), name);
                functions.put(statement.projection().indexOf(function), function.name());
            }
        }

        final List<Integer> groups = Lists.newLinkedList();

        for (SqlColumn column : statement.groupBy()) {
            groups.add(columns.indexOf(column.name()));
        }

        // IMPORTANT set parameters before passing the config to the job
        // BEWARE on injection, config may not store empty strings
        config.set(SideData.PROJECTION, SideData.serialize(projection));
        config.set(SideData.COLUMNS, SideData.serialize(selections));
        config.set(SideData.FUNCTIONS, SideData.serialize(functions));
        config.set(SideData.GROUPS, SideData.serialize(groups));

        final Job job = new Job(config, "Helsing");

        job.setJarByClass(Hadoop.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(GroupByMapper.class);
        job.setReducerClass(AggregatorReducer.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(input.getAbsolutePath()));
        FileOutputFormat.setOutputPath(job, new Path(output.getAbsolutePath()));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        final Configuration config = new Configuration();
        int code = ToolRunner.run(config, new Hadoop(), args);
        System.exit(code);
    }

}
