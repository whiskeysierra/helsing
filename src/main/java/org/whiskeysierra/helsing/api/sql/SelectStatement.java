package org.whiskeysierra.helsing.api.sql;

public interface SelectStatement {

    SqlProjection projection();

    SqlTable from();

    SqlGroupBy groupBy();

}
