package org.whiskeysierra.helsing.hadoop;

import com.google.inject.Guice;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

abstract class DependencyInjectionMapper<KI, VI, KO, VO> extends Mapper<KI, VI, KO, VO> {

    @Override
    protected final void setup(Context context) throws IOException, InterruptedException {
        final Configuration config = context.getConfiguration();
        Guice.createInjector(
            new HadoopModule(),
            new ConfigurationModule(config)
        ).injectMembers(this);

        configure(context);
    }

    protected void configure(Context context) throws IOException, InterruptedException {

    }

}
