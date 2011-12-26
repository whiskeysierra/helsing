package org.whiskeysierra.helsing.hadoop;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.whiskeysierra.helsing.api.sql.SelectStatement;
import org.whiskeysierra.helsing.api.sql.SqlParser;

public final class Hadoop extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        final Options options = parse(args);
        final Injector injector = createInjector(options);

        final JobAssembler assembler = injector.getInstance(JobAssembler.class);
        final Job job = assembler.assemble();

        return job.waitForCompletion(true) ? 0 : 1;
    }

    private Options parse(String[] args) {
        final Options options = new Options();
        final CmdLineParser parser = new CmdLineParser(options);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            throw new IllegalStateException(e);
        }
        return options;
    }

    private Injector createInjector(final Options options) {
        return Guice.createInjector(
            new HadoopModule(),
            new AbstractModule() {

                @Override
                protected void configure() {
                    bind(Configuration.class).toInstance(getConf());
                    bind(Options.class).toInstance(options);

                }

                @Provides
                public SelectStatement provide(Options options, SqlParser parser) {
                    final String query = options.getQuery();
                    return parser.parse(query);
                }

            }
        );
    }

    public static void main(String[] args) throws Exception {
        final Configuration config = new Configuration();
        int code = ToolRunner.run(config, new Hadoop(), args);
        System.exit(code);
    }

}
