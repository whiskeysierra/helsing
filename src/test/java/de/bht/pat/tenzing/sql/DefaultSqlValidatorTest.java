package de.bht.pat.tenzing.sql;

import org.junit.Test;

import java.sql.SQLException;

public class DefaultSqlValidatorTest {

    private final SqlValidator unit = new DefaultSqlValidator();

    @Test
    public void select() throws SQLException {
        unit.validate("SELECT year, country FROM countries");
    }

    @Test
    public void groupBy() throws SQLException {
        unit.validate("SELECT year FROM countries GROUP BY year");
    }

    @Test
    public void aggregate() throws SQLException {
        unit.validate("SELECT year, AVG(population) FROM countries GROUP BY year");
    }

}
