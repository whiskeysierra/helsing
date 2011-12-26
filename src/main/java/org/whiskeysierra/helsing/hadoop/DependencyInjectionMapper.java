package org.whiskeysierra.helsing.hadoop;

import com.google.inject.Guice;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

abstract class DependencyInjectionMapper<KI, VI, KO, VO> extends Mapper<KI, VI, KO, VO> {

    @Override
    protected final void setup(Context context) throws IOException, InterruptedException {
        Guice.createInjector(new HadoopModule()).injectMembers(this);
        configure(context);
    }

    protected abstract void configure(Context context) throws IOException, InterruptedException;

}
