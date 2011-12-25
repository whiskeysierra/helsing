package org.whiskeysierra.helsing.util.concurrent;

import com.google.inject.AbstractModule;
import org.whiskeysierra.helsing.util.concurrent.process.ProcessModule;

public final class ConcurrencyModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ProcessModule());
    }

}
