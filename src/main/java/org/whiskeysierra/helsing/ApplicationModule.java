package org.whiskeysierra.helsing;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.whiskeysierra.helsing.api.ApiModule;
import org.whiskeysierra.helsing.hadoop.functions.FunctionsModule;
import org.whiskeysierra.helsing.mapreduce.MapReduceModule;
import org.whiskeysierra.helsing.sql.SqlModule;
import org.whiskeysierra.helsing.util.UtilityModule;

public final class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        bindEventBus();
        installModules();
    }

    private void bindEventBus() {
        bind(EventBus.class).in(Singleton.class);
    }

    private void installModules() {
        install(new PropertiesModule());
        install(new ApiModule());
        install(new FunctionsModule());
        install(new MapReduceModule());
        install(new SqlModule());
        install(new UtilityModule());
    }

}
