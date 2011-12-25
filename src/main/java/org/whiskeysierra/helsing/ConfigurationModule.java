package org.whiskeysierra.helsing;

import com.google.common.io.Closeables;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

final class ConfigurationModule extends AbstractModule {

    @Override
    protected void configure() {
        final Properties properties = new Properties();
        final URL url = Resources.getResource("application.properties");

        final InputStream stream;

        try {
            stream = url.openStream();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        try {
            properties.load(stream);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            Closeables.closeQuietly(stream);
        }

        Names.bindProperties(binder(), properties);
    }

}
