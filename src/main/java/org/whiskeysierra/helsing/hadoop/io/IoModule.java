package org.whiskeysierra.helsing.hadoop.io;

import com.google.inject.AbstractModule;

public final class IoModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Serializer.class).to(GsonSerializer.class);
    }

}
