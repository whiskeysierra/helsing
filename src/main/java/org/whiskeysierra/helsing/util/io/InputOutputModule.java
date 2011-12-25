package org.whiskeysierra.helsing.util.io;

import com.google.inject.AbstractModule;

public final class InputOutputModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Line.class).to(DefaultLine.class);
        bind(FileFormat.class).to(DefaultFileFormat.class);
    }

}