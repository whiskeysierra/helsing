package de.bht.pat.tenzing.sql;

import net.sf.jsqlparser.schema.Column;

final class DefaultSqlColumn extends AbstractSqlExpression implements SqlColumn {

    private final String name;

    public DefaultSqlColumn(String name) {
        this.name = name;
    }

    public DefaultSqlColumn(Column column) {
        this(column.getColumnName());
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return name();
    }

}
