package org.whiskeysierra.helsing.api.sql;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;

final class DefaultSqlParser implements SqlParser {

    @Override
    public SelectStatement parse(String sql) {
        final CCJSqlParserManager manager = new CCJSqlParserManager();

        try {
            final Statement statement = manager.parse(new StringReader(sql));
            assert statement instanceof Select;
            final SelectBody body = Select.class.cast(statement).getSelectBody();
            assert body instanceof PlainSelect;
            final PlainSelect select = PlainSelect.class.cast(body);
            final List items = select.getSelectItems();

            final List<SqlExpression> expressions = Lists.newArrayList();

            for (final SelectExpressionItem item : Iterables.filter(items, SelectExpressionItem.class)) {
                final Expression expression = item.getExpression();

                if (expression instanceof Column) {
                    final Column column = Column.class.cast(expression);
                    expressions.add(new DefaultSqlColumn(column));
                } else if (expression instanceof Function) {
                    final Function function = Function.class.cast(expression);
                    expressions.add(new DefaultSqlFunction(function));
                }
            }

            final FromItem item = select.getFromItem();
            assert item instanceof Table;
            final Table table = Table.class.cast(item);

            final List<Column> groupColumns = getGroupByColumns(select);

            final SqlProjection projection = new DefaultSqlProjection(expressions);
            final SqlTable t = new DefaultSqlTable(table);

            final SqlGroupBy groupBy = new DefaultSqlGroupBy(groupColumns);

            return new DefaultSelectStatement(projection, t, groupBy);
        } catch (JSQLParserException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Column> getGroupByColumns(PlainSelect select) {
        final List<?> references = select.getGroupByColumnReferences();
        return references == null ?
            Collections.<Column>emptyList() : (List<Column>) references;
    }

}
