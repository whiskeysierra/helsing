package org.whiskeysierra.helsing.hadoop;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.apache.hadoop.conf.Configuration;

import java.util.Map.Entry;

final class ConfigurationModule extends AbstractModule {

    private final Configuration config;

    public ConfigurationModule(Configuration config) {
        this.config = config;
    }

    @Override
    protected void configure() {
        for (Entry<String, String> entry : config) {
            bind(String.class).annotatedWith(Names.named(entry.getKey())).toInstance(entry.getValue());
        }
    }

}
