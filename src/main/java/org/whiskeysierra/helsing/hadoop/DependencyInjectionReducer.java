package org.whiskeysierra.helsing.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

abstract class DependencyInjectionReducer<KI, VI, KO, VO> extends Reducer<KI, VI, KO, VO> {

    @Override
    protected final void setup(Context context) throws IOException, InterruptedException {
        final Configuration config = context.getConfiguration();
        Configurator.configure(config).injectMembers(this);
        configure(context);
    }

    protected void configure(Context context) throws IOException, InterruptedException {

    }

}
