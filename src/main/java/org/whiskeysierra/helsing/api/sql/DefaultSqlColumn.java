package org.whiskeysierra.helsing.api.sql;

import com.google.common.base.Function;
import net.sf.jsqlparser.schema.Column;

final class DefaultSqlColumn extends AbstractSqlExpression implements SqlColumn {

    public static final Function<Column, SqlColumn> NEW = new Function<Column, SqlColumn>() {

        @Override
        public SqlColumn apply(Column input) {
            return new DefaultSqlColumn(input);
        }

    };

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
