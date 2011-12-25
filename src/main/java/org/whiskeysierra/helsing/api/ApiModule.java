package org.whiskeysierra.helsing.api;

import com.google.inject.AbstractModule;
import org.whiskeysierra.helsing.api.sql.SqlModule;

public final class ApiModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new SqlModule());
    }

}
