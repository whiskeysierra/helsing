package de.bht.pat.tenzing.events;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class QueryEvent {

    private final String sql;

    public QueryEvent(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }

}
