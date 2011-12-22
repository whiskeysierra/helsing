package de.bht.pat.tenzing.events;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class QueryEvent {

    private final String query;

    public QueryEvent(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

}
