package org.whiskeysierra.helsing.api.sql;

import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
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

    @Override
    public boolean isEmpty() {
        return columns.isEmpty();
    }

    @Override
    public Iterable<String> toStrings() {
        return Iterables.transform(this, Functions.toStringFunction());
    }

}
