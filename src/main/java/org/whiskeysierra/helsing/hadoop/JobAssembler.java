package org.whiskeysierra.helsing.hadoop;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.whiskeysierra.helsing.api.sql.SelectStatement;
import org.whiskeysierra.helsing.api.sql.SqlColumn;
import org.whiskeysierra.helsing.api.sql.SqlExpression;
import org.whiskeysierra.helsing.api.sql.SqlFunction;
import org.whiskeysierra.helsing.hadoop.io.Serializer;
import org.whiskeysierra.helsing.hadoop.io.Types;
import org.whiskeysierra.helsing.hadoop.io.Types.FunctionDefinition;
import org.whiskeysierra.helsing.hadoop.io.Types.Functions;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.List;

final class JobAssembler {

    private final Configuration config;
    private final Options options;
    private final SelectStatement statement;
    private final Serializer serializer;

    @Inject
    public JobAssembler(Configuration config, Options options, SelectStatement statement, Serializer serializer) {
        this.config = config;
        this.options = options;
        this.statement = statement;
        this.serializer = serializer;
    }

    public Job assemble() throws IOException {
        final File input = options.getInput();
        final File output = options.getOutput();

        final File schema = new File(input.getParentFile(), input.getName().replace(".csv", ".schema.csv"));
        final List<String> columns = Files.readLines(schema, Charsets.UTF_8);

        final List<Integer> projection = Lists.newLinkedList();
        final Functions functions = new Functions();

        for (SqlExpression expression : statement.projection()) {
            if (expression.is(SqlColumn.class)) {
                final SqlColumn column = expression.as(SqlColumn.class);
                final String name = column.name();
                projection.add(columns.indexOf(name));
            } else if (expression.is(SqlFunction.class)) {
                final SqlFunction function = expression.as(SqlFunction.class);

                final List<Integer> columnIndices = Lists.newArrayList();
                for (SqlColumn column : function.columns()) {
                    final String name = column.name();
                    projection.add(columns.indexOf(name));
                    columnIndices.add(columns.indexOf(name));
                }

                functions.put(statement.projection().indexOf(function),
                    new FunctionDefinition(function.name(), columnIndices));
            }
        }

        final List<Integer> groups = Lists.newLinkedList();

        for (SqlColumn column : statement.groupBy()) {
            groups.add(columns.indexOf(column.name()));
        }

        // IMPORTANT set parameters before passing the config to the job
        config.set(SideData.PROJECTION, serializer.serialize(projection, Types.Indices.class));
        config.set(SideData.FUNCTIONS, serializer.serialize(functions, Types.Functions.class));
        config.set(SideData.GROUPS, serializer.serialize(groups, Types.Indices.class));

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

        return job;
    }

}
