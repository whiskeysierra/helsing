package org.whiskeysierra.helsing.api.sql;

final class DefaultSelectStatement implements SelectStatement {

    private final SqlProjection projection;
    private final SqlTable table;
    private final SqlGroupBy groupBy;

    public DefaultSelectStatement(SqlProjection projection, SqlTable table, SqlGroupBy groupBy) {
        this.projection = projection;
        this.table = table;
        this.groupBy = groupBy;
    }

    @Override
    public SqlProjection projection() {
        return projection;
    }

    @Override
    public SqlTable from() {
        return table;
    }

    @Override
    public SqlGroupBy groupBy() {
        return groupBy;
    }

}
