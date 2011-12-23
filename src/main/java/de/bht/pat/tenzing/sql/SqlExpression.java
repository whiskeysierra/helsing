package de.bht.pat.tenzing.sql;

public interface SqlExpression {

    <T extends SqlExpression> boolean is(Class<T> type);

    <T extends SqlExpression> T as(Class<T> type);

}
