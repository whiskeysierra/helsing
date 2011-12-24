package de.bht.pat.tenzing;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import de.bht.pat.tenzing.hadoop.functions.FunctionsModule;
import de.bht.pat.tenzing.mapreduce.MapReduceModule;
import de.bht.pat.tenzing.sql.SqlModule;
import de.bht.pat.tenzing.ui.UiModule;
import de.bht.pat.tenzing.util.UtilityModule;

final class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EventBus.class).in(Singleton.class);

        install(new ConfigurationModule());
        install(new FunctionsModule());
        install(new MapReduceModule());
        install(new SqlModule());
        install(new UiModule());
        install(new UtilityModule());
    }

}
