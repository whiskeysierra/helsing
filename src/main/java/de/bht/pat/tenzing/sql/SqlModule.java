package de.bht.pat.tenzing.sql;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public final class SqlModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SqlParser.class).to(DefaultSqlParser.class).in(Singleton.class);
        bind(SqlValidator.class).asEagerSingleton();
    }

}
