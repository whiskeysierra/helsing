package de.bht.pat.tenzing.events;

import de.bht.pat.tenzing.sql.SelectStatement;

import javax.annotation.concurrent.Immutable;

@Immutable
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
