package org.whiskeysierra.helsing.api.sql;

public interface SqlFunction extends SqlExpression {

    String name();

    SqlColumn column();

}
