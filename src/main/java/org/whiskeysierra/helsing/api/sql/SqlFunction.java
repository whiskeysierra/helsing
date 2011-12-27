package org.whiskeysierra.helsing.api.sql;

public interface SqlFunction extends SqlExpression {

    String name();

    Iterable<SqlColumn> columns();

}
