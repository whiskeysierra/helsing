package org.whiskeysierra.helsing;

import com.google.common.eventbus.EventBus;
import com.google.common.io.Closeables;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import org.nnsoft.guice.rocoto.converters.FileConverter;
import org.nnsoft.guice.rocoto.converters.PatternConverter;
import org.whiskeysierra.helsing.api.ApiModule;
import org.whiskeysierra.helsing.hadoop.functions.FunctionsModule;
import org.whiskeysierra.helsing.mapreduce.MapReduceModule;
import org.whiskeysierra.helsing.sql.SqlModule;
import org.whiskeysierra.helsing.util.UtilityModule;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public final class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        bindProperties();
        bindEventBus();
        installModules();
    }

    private void bindProperties() {
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

    private void bindEventBus() {
        bind(EventBus.class).in(Singleton.class);
    }

    private void installModules() {
        install(new ApiModule());
        install(new FunctionsModule());
        install(new MapReduceModule());
        install(new SqlModule());
        install(new UtilityModule());
    }

}
