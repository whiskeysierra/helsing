package org.whiskeysierra.helsing.util.format;

import com.google.inject.AbstractModule;

public final class FormatModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Line.class).to(DefaultLine.class);
        bind(FileFormat.class).to(DefaultFileFormat.class);
    }

}
