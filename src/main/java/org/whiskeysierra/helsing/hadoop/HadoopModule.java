package org.whiskeysierra.helsing.hadoop;

import com.google.inject.AbstractModule;
import org.whiskeysierra.helsing.hadoop.functions.FunctionsModule;
import org.whiskeysierra.helsing.sql.SqlModule;

final class HadoopModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FunctionsModule());
        install(new SqlModule());
    }

}
