package de.bht.pat.tenzing.util;

import com.google.inject.AbstractModule;
import de.bht.pat.tenzing.util.concurrent.ProcessModule;
import de.bht.pat.tenzing.util.text.TextModule;

public final class UtilityModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ProcessModule());
        install(new TextModule());
    }

}
