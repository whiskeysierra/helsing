package org.whiskeysierra.helsing.hadoop;

import com.google.inject.AbstractModule;
import org.whiskeysierra.helsing.PropertiesModule;
import org.whiskeysierra.helsing.api.ApiModule;
import org.whiskeysierra.helsing.hadoop.functions.FunctionsModule;
import org.whiskeysierra.helsing.hadoop.io.IoModule;
import org.whiskeysierra.helsing.util.format.FormatModule;
import org.whiskeysierra.helsing.util.inject.InjectModule;

final class HadoopModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ApiModule());
        install(new FunctionsModule());
        install(new PropertiesModule());
        install(new InjectModule());
        install(new IoModule());
        install(new FormatModule());
    }

}
