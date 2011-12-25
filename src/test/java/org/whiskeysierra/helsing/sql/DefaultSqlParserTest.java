package org.whiskeysierra.helsing.sql;

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
        Assert.assertNull(statement.groupBy());
    }

    @Test
    public void groupBy() {
        final SqlParser unit = new DefaultSqlParser();
        final String sql = "SELECT country, MAX(year) FROM countries.csv GROUP BY country";
        final SelectStatement statement = unit.parse(sql);

        Assert.assertEquals("country", statement.projection().get(0).as(SqlColumn.class).name());
        Assert.assertEquals("MAX", statement.projection().get(1).as(SqlFunction.class).name());
        Assert.assertEquals("year", statement.projection().get(1).as(SqlFunction.class).column().name());
        Assert.assertEquals("countries.csv", statement.from().name());
        Assert.assertEquals("country", statement.groupBy().column().name());
    }

}
