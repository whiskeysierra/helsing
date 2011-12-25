package org.whiskeysierra.helsing.api.sql;

public interface SqlExpression {

    <T extends SqlExpression> boolean is(Class<T> type);

    <T extends SqlExpression> T as(Class<T> type);

}
