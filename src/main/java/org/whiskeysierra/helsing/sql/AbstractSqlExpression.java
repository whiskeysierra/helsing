package org.whiskeysierra.helsing.sql;

abstract class AbstractSqlExpression implements SqlExpression {

    @Override
    public <T extends SqlExpression> boolean is(Class<T> type) {
        return type.isAssignableFrom(this.getClass());
    }

    @Override
    public <T extends SqlExpression> T as(Class<T> type) {
        return type.cast(this);
    }

}
