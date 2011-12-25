package org.whiskeysierra.helsing.sql;

import com.google.common.base.Objects;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.InverseExpression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.Union;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.lang.StringUtils;
import org.whiskeysierra.helsing.api.Functions;
import org.whiskeysierra.helsing.api.sql.SqlParser;
import org.whiskeysierra.helsing.events.FeatureError;
import org.whiskeysierra.helsing.events.QueryEvent;
import org.whiskeysierra.helsing.events.SqlEvent;
import org.whiskeysierra.helsing.events.SyntaxError;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

final class SqlValidator implements StatementVisitor, SelectVisitor, SelectItemVisitor, FromItemVisitor, ExpressionVisitor {

    private final EventBus bus;
    private final SqlParser parser;
    private final Set<String> functions;

    @Inject
    public SqlValidator(EventBus bus, SqlParser parser, @Functions Set<String> functions) {
        this.bus = bus;
        this.parser = parser;
        this.functions = functions;

        bus.register(this);
    }

    @Subscribe
    public void onInput(SqlEvent event) throws SQLException {
        try {
            final String sql = event.getSql();

            validate(sql);

            bus.post(new QueryEvent(sql, parser.parse(sql)));
        } catch (JSQLParserException e) {
            bus.post(new SyntaxError(e));
        } catch (UnsupportedOperationException e) {
            bus.post(new FeatureError(e.getMessage()));
        }
    }

    private void validate(String sql) throws JSQLParserException {
        final CCJSqlParserManager manager = new CCJSqlParserManager();
        manager.parse(new StringReader(sql)).accept(this);
    }

    private void assertNull(Object element, String feature) {
        if (element != null) {
            throw new UnsupportedOperationException(feature);
        }
    }

    private void assertNullOrEmpty(List<?> list, String feature) {
        if (list != null && !list.isEmpty()) {
            throw new UnsupportedOperationException(feature);
        }
    }

    @Override
    public void visit(Select select) {
        assertNullOrEmpty(select.getWithItemsList(), "WITH");
        select.getSelectBody().accept(this);
    }

    @Override
    public void visit(PlainSelect select) {
        assertNull(select.getDistinct(), "DISTINCT");
        assertNull(select.getHaving(), "HAVING");
        assertNull(select.getInto(), "INTO");
        assertNullOrEmpty(select.getJoins(), "JOIN");
        assertNull(select.getLimit(), "LIMIT");
        assertNullOrEmpty(select.getOrderByElements(), "ORDER BY");
        assertNull(select.getTop(), "TOP");
        assertNull(select.getWhere(), "WHERE");

        for (SelectItem item : getSelectItems(select)) {
            item.accept(this);
        }

        select.getFromItem().accept(this);

        for (Column column : getGroupByColumns(select)) {
            column.accept(this);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Column> getGroupByColumns(PlainSelect select) {
        return Objects.firstNonNull(select.getGroupByColumnReferences(), Collections.EMPTY_LIST);
    }

    @SuppressWarnings("unchecked")
    private List<SelectItem> getSelectItems(PlainSelect select) {
        return Objects.firstNonNull(select.getSelectItems(), Collections.EMPTY_LIST);
    }

    @Override
    public void visit(Column column) {
        // supported
    }

    @Override
    public void visit(Function function) {
        final String name = StringUtils.upperCase(function.getName());
        if (functions.contains(name)) return;
        throw new UnsupportedOperationException(name);
    }

    @Override
    public void visit(Table table) {
        assertNull(table.getAlias(), "AS");
        // rest is supported
    }

    @Override
    public void visit(SelectExpressionItem item) {
        assertNull(item.getAlias(), "AS");
        item.getExpression().accept(this);
    }

    @Override
    public void visit(Delete delete) {
        throw new UnsupportedOperationException("DELETE");
    }

    @Override
    public void visit(Update update) {
        throw new UnsupportedOperationException("UPDATE");
    }

    @Override
    public void visit(Insert insert) {
        throw new UnsupportedOperationException("INSERT");
    }

    @Override
    public void visit(Replace replace) {
        throw new UnsupportedOperationException("REPLACE");
    }

    @Override
    public void visit(Drop drop) {
        throw new UnsupportedOperationException("DROP");
    }

    @Override
    public void visit(Truncate truncate) {
        throw new UnsupportedOperationException("TRUNCATE");
    }

    @Override
    public void visit(CreateTable createTable) {
        throw new UnsupportedOperationException("CREATE TABLE");
    }

    @Override
    public void visit(Union union) {
        throw new UnsupportedOperationException("UNION");
    }

    @Override
    public void visit(AllColumns columns) {
        throw new UnsupportedOperationException("SELECT *");
    }

    @Override
    public void visit(AllTableColumns columns) {
        throw new UnsupportedOperationException("SELECT " + columns.getTable().getName() + ".*");
    }

    @Override
    public void visit(NullValue nullValue) {
        throw new UnsupportedOperationException("NULL");
    }

    @Override
    public void visit(InverseExpression inverseExpression) {
        inverseExpression.getExpression().accept(this);
        // TODO fail anyway
    }

    @Override
    public void visit(JdbcParameter jdbcParameter) {
        throw new UnsupportedOperationException("?");
    }

    @Override
    public void visit(DoubleValue doubleValue) {
        throw new UnsupportedOperationException("Double literal " + doubleValue.getValue());
    }

    @Override
    public void visit(LongValue longValue) {
        throw new UnsupportedOperationException("Long literal " + longValue.getValue());
    }

    @Override
    public void visit(DateValue dateValue) {
        throw new UnsupportedOperationException("Date literal " + dateValue.getValue());
    }

    @Override
    public void visit(TimeValue timeValue) {
        throw new UnsupportedOperationException("Time literal " + timeValue.getValue());
    }

    @Override
    public void visit(TimestampValue timestampValue) {
        throw new UnsupportedOperationException("Timestamp literal " + timestampValue.getValue());
    }

    @Override
    public void visit(Parenthesis parenthesis) {
        throw new UnsupportedOperationException("Parenthesis");
    }

    @Override
    public void visit(StringValue stringValue) {
        throw new UnsupportedOperationException("String literal " + stringValue.getValue());
    }

    @Override
    public void visit(Addition addition) {
        throw new UnsupportedOperationException("Operator +");
    }

    @Override
    public void visit(Division division) {
        throw new UnsupportedOperationException("Operator /");
    }

    @Override
    public void visit(Multiplication multiplication) {
        throw new UnsupportedOperationException("Operator *");
    }

    @Override
    public void visit(Subtraction subtraction) {
        throw new UnsupportedOperationException("Operator -");
    }

    @Override
    public void visit(AndExpression andExpression) {
        throw new UnsupportedOperationException("AND");
    }

    @Override
    public void visit(OrExpression orExpression) {
        throw new UnsupportedOperationException("OR");
    }

    @Override
    public void visit(Between between) {
        throw new UnsupportedOperationException("BETWEEN");
    }

    @Override
    public void visit(EqualsTo equalsTo) {
        throw new UnsupportedOperationException("Operator =");
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        throw new UnsupportedOperationException("Operator >");
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        throw new UnsupportedOperationException("Operator >=");
    }

    @Override
    public void visit(InExpression inExpression) {
        throw new UnsupportedOperationException("IN");
    }

    @Override
    public void visit(IsNullExpression isNullExpression) {
        throw new UnsupportedOperationException("IS NULL");
    }

    @Override
    public void visit(LikeExpression likeExpression) {
        throw new UnsupportedOperationException("LIKE");
    }

    @Override
    public void visit(MinorThan minorThan) {
        throw new UnsupportedOperationException("Operator <");
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        throw new UnsupportedOperationException("Operator <=");
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        throw new UnsupportedOperationException("<>");
    }

    @Override
    public void visit(SubSelect subSelect) {
        throw new UnsupportedOperationException("Sub SELECT");
    }

    @Override
    public void visit(CaseExpression caseExpression) {
        throw new UnsupportedOperationException("CASE");
    }

    @Override
    public void visit(WhenClause whenClause) {
        throw new UnsupportedOperationException("WHEN");
    }

    @Override
    public void visit(ExistsExpression existsExpression) {
        throw new UnsupportedOperationException("EXISTS");
    }

    @Override
    public void visit(AllComparisonExpression allComparisonExpression) {
        // TODO ?
    }

    @Override
    public void visit(AnyComparisonExpression anyComparisonExpression) {
        // TODO ?
    }

    @Override
    public void visit(Concat concat) {
        throw new UnsupportedOperationException("CONCAT");
    }

    @Override
    public void visit(Matches matches) {
        throw new UnsupportedOperationException("MATCHES");
    }

    @Override
    public void visit(BitwiseAnd bitwiseAnd) {
        throw new UnsupportedOperationException("&");
    }

    @Override
    public void visit(BitwiseOr bitwiseOr) {
        throw new UnsupportedOperationException("|");
    }

    @Override
    public void visit(BitwiseXor bitwiseXor) {
        throw new UnsupportedOperationException("^");
    }

    @Override
    public void visit(SubJoin subJoin) {
        throw new UnsupportedOperationException("JOIN");
    }

}
