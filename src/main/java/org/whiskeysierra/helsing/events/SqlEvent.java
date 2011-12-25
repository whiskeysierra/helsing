package org.whiskeysierra.helsing.events;

public final class SqlEvent {

    private final String sql;

    public SqlEvent(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }

}
