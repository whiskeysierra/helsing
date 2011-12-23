package de.bht.pat.tenzing.util.text;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

import java.util.Locale;
import java.util.ResourceBundle;

public final class TextModule extends AbstractModule {

    @Override
    protected void configure() {
        final ResourceBundle bundle = ResourceBundle.getBundle("unit", Locale.ENGLISH);
        bind(ResourceBundle.class).annotatedWith(Names.named("unit")).toInstance(bundle);

        bind(TimeFormatter.class).in(Singleton.class);
    }

}
