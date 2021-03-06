package org.whiskeysierra.helsing.util;

import com.google.inject.AbstractModule;
import org.whiskeysierra.helsing.util.concurrent.ConcurrencyModule;
import org.whiskeysierra.helsing.util.inject.InjectModule;
import org.whiskeysierra.helsing.util.format.FormatModule;
import org.whiskeysierra.helsing.util.text.TextModule;

public final class UtilityModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ConcurrencyModule());
        install(new InjectModule());
        install(new FormatModule());
        install(new TextModule());
    }

}
