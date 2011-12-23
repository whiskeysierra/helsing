package de.bht.pat.tenzing.sql;

import com.google.common.base.Functions;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.Lists;

import java.util.List;

final class DefaultSqlProjection extends ForwardingList<SqlExpression> implements SqlProjection {

    private final List<SqlExpression> expressions;

    public DefaultSqlProjection(List<SqlExpression> expressions) {
        this.expressions = expressions;
    }

    @Override
    protected List<SqlExpression> delegate() {
        return expressions;
    }

    @Override
    public List<String> toStrings() {
        return Lists.transform(this, Functions.toStringFunction());
    }

}
