package org.whiskeysierra.helsing.sql;

public interface SqlFunction extends SqlExpression {

    String name();

    SqlColumn column();

}
