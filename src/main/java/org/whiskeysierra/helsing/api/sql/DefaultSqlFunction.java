package org.whiskeysierra.helsing.api.sql;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.schema.Column;

import java.util.List;
import java.util.Locale;

final class DefaultSqlFunction extends AbstractSqlExpression implements SqlFunction {

    private final Function function;

    public DefaultSqlFunction(Function function) {
        this.function = function;
    }

    @Override
    public String name() {
        return function.getName().toUpperCase(Locale.ENGLISH);
    }

    @Override
    public Iterable<SqlColumn> columns() {
        final ExpressionList parameters = function.getParameters();
        final List expressions = parameters.getExpressions();
        final Iterable<Column> columns = Iterables.filter(expressions, Column.class);

        return Iterables.transform(columns, DefaultSqlColumn.NEW);
    }

    @Override
    public String toString() {
        return name() + "(" + Joiner.on(", ").join(columns()) + ")";
    }

}
