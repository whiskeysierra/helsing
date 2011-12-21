package de.bht.pat.tenzing.mapreduce;

import com.google.inject.AbstractModule;

public final class MapReduceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(QueryProcessor.class).asEagerSingleton();
    }

}
