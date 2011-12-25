package org.whiskeysierra.helsing.hadoop;

import com.google.inject.AbstractModule;
import org.whiskeysierra.helsing.api.ApiModule;
import org.whiskeysierra.helsing.hadoop.functions.FunctionsModule;

final class HadoopModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ApiModule());
        install(new FunctionsModule());
    }

}
