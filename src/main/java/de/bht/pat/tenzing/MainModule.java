package de.bht.pat.tenzing;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import de.bht.pat.tenzing.mapreduce.MapReduceModule;
import de.bht.pat.tenzing.ui.UiModule;
import de.bht.pat.tenzing.sql.SqlModule;
import de.bht.pat.tenzing.util.concurrent.ProcessModule;

final class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EventBus.class).in(Singleton.class);

        install(new ConfigurationModule());
        install(new MapReduceModule());
        install(new SqlModule());
        install(new UiModule());
        install(new ProcessModule());
    }

}
