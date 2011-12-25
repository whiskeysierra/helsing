package org.whiskeysierra.helsing;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.whiskeysierra.helsing.api.ApiModule;
import org.whiskeysierra.helsing.hadoop.functions.FunctionsModule;
import org.whiskeysierra.helsing.mapreduce.MapReduceModule;
import org.whiskeysierra.helsing.sql.SqlModule;
import org.whiskeysierra.helsing.ui.UserInterfaceModule;
import org.whiskeysierra.helsing.util.UtilityModule;

final class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EventBus.class).in(Singleton.class);

        install(new ApiModule());
        install(new ConfigurationModule());
        install(new FunctionsModule());
        install(new MapReduceModule());
        install(new SqlModule());
        install(new UserInterfaceModule());
        install(new UtilityModule());
    }

}
