package de.bht.pat.tenzing.sql;

import net.sf.jsqlparser.schema.Column;

class DefaultSqlGroupBy implements SqlGroupBy {

    private final Column column;

    public DefaultSqlGroupBy(Column column) {
        this.column = column;
    }

    @Override
    public SqlColumn column() {
        return new DefaultSqlColumn(column);
    }

}
