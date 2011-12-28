package org.whiskeysierra.helsing.api.sql;

import com.google.common.collect.Iterables;
import org.junit.Assert;
import org.junit.Test;

public final class DefaultSqlParserTest {

    @Test
    public void projection() {
        final SqlParser unit = new DefaultSqlParser();
        final String sql = "SELECT country FROM countries.csv";
        final SelectStatement statement = unit.parse(sql);

        Assert.assertEquals("country", statement.projection().get(0).as(SqlColumn.class).name());
        Assert.assertEquals("countries.csv", statement.from().name());
        Assert.assertTrue(statement.groupBy().isEmpty());
    }

    @Test
    public void groupBy() {
        final SqlParser unit = new DefaultSqlParser();
        final String sql = "SELECT country, MAX(year) FROM countries.csv GROUP BY country";
        final SelectStatement statement = unit.parse(sql);

        Assert.assertEquals("country", statement.projection().get(0).as(SqlColumn.class).name());
        Assert.assertEquals("MAX", statement.projection().get(1).as(SqlFunction.class).name());
        Assert.assertEquals("year", Iterables.getOnlyElement(statement.projection().get(1).as(SqlFunction.class).columns()).name());
        Assert.assertEquals("countries.csv", statement.from().name());
        Assert.assertEquals("country", Iterables.getOnlyElement(statement.groupBy()).name());
    }

    @Test
    public void multiColumnAggregate() {
        final SqlParser unit = new DefaultSqlParser();
        final String sql = "SELECT year, COVAR(population, population) FROM countries.csv GROUP BY year";
        final SelectStatement statement = unit.parse(sql);

        Assert.assertEquals("year", statement.projection().get(0).as(SqlColumn.class).name());
        final SqlFunction function = statement.projection().get(1).as(SqlFunction.class);
        Assert.assertEquals("COVAR", function.name());
        Assert.assertEquals("population", Iterables.get(function.columns(), 0).name());
        Assert.assertEquals("population", Iterables.get(function.columns(), 1).name());
        Assert.assertEquals("countries.csv", statement.from().name());
        Assert.assertEquals("year", Iterables.getOnlyElement(statement.groupBy()).name());
    }

}
