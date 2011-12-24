package de.bht.pat.tenzing;

import com.google.inject.AbstractModule;
import de.bht.pat.tenzing.inject.Data;
import de.bht.pat.tenzing.inject.Schema;

import java.io.File;
import java.util.regex.Pattern;

final class ConfigurationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(File.class).annotatedWith(Data.class).toInstance(new File("data"));
        bind(Pattern.class).annotatedWith(Schema.class).toInstance(Pattern.compile(".*\\.schema\\.csv$"));
        bind(Pattern.class).annotatedWith(Data.class).toInstance(Pattern.compile(".*\\.csv$"));
    }

}
