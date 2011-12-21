package de.bht.pat.tenzing.sql;

import com.google.inject.AbstractModule;

public final class SqlModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SqlValidator.class).asEagerSingleton();
    }

}
