package org.whiskeysierra.helsing.sql;

import net.sf.jsqlparser.schema.Column;

final class DefaultSqlGroupBy implements SqlGroupBy {

    private final Column column;

    public DefaultSqlGroupBy(Column column) {
        this.column = column;
    }

    @Override
    public SqlColumn column() {
        return new DefaultSqlColumn(column);
    }

}
