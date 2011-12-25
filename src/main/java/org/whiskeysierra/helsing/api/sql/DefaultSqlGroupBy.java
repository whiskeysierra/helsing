package org.whiskeysierra.helsing.api.sql;

import com.google.common.collect.Lists;
import net.sf.jsqlparser.schema.Column;

import java.util.Iterator;
import java.util.List;

final class DefaultSqlGroupBy implements SqlGroupBy {

    private final List<SqlColumn> columns = Lists.newArrayList();

    public DefaultSqlGroupBy(Iterable<Column> columns) {
        for (Column column : columns) {
            this.columns.add(new DefaultSqlColumn(column));
        }
    }

    @Override
    public Iterator<SqlColumn> iterator() {
        return columns.iterator();
    }

}
