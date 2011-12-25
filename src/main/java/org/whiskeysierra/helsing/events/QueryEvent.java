package org.whiskeysierra.helsing.events;

import org.whiskeysierra.helsing.api.sql.SelectStatement;

public final class QueryEvent {

    private final String query;
    private final SelectStatement statement;

    public QueryEvent(String query, SelectStatement statement) {
        this.query = query;
        this.statement = statement;
    }

    public String getQuery() {
        return query;
    }

    public SelectStatement getStatement() {
        return statement;
    }

}
