package de.bht.pat.tenzing.sql;

public interface SqlFunction extends SqlExpression {

    String name();

    SqlColumn column();

}
