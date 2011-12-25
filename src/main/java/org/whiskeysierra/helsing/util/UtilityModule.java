package org.whiskeysierra.helsing.util;

import com.google.inject.AbstractModule;
import org.whiskeysierra.helsing.util.concurrent.ProcessModule;
import org.whiskeysierra.helsing.util.text.TextModule;

public final class UtilityModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ProcessModule());
        install(new TextModule());
    }

}
