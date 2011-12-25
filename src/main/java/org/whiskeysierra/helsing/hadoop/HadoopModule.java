package org.whiskeysierra.helsing.hadoop;

import com.google.inject.AbstractModule;
import org.whiskeysierra.helsing.PropertiesModule;
import org.whiskeysierra.helsing.api.ApiModule;
import org.whiskeysierra.helsing.hadoop.functions.FunctionsModule;
import org.whiskeysierra.helsing.util.inject.InjectModule;
import org.whiskeysierra.helsing.util.io.InputOutputModule;

final class HadoopModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ApiModule());
        install(new FunctionsModule());
        install(new PropertiesModule());
        install(new InjectModule());
        install(new InputOutputModule());
    }

}
