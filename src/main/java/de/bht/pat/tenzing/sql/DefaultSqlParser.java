package de.bht.pat.tenzing.sql;

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

            final List<?> references = select.getGroupByColumnReferences();
            final Column groupColumn = references == null || references.isEmpty() ?
                null : Column.class.cast(references.get(0));

            final SqlProjection projection = new DefaultSqlProjection(expressions);
            final SqlTable t = new DefaultSqlTable(table);
            final SqlGroupBy groupBy = groupColumn == null ? null : new DefaultSqlGroupBy(groupColumn);

            return new DefaultSelectStatement(projection, t, groupBy);
        } catch (JSQLParserException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
