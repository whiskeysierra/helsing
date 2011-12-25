package org.whiskeysierra.helsing.hadoop;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.hadoop.conf.Configuration;

final class Configurator {

    public static Injector configure(Configuration config) {
        return Guice.createInjector(
            new HadoopModule(),
            new ConfigurationModule(config),
            new SideDataModule()
        );
    }

}
