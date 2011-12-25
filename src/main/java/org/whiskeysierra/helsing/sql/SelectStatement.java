package org.whiskeysierra.helsing.sql;

public interface SelectStatement {

    SqlProjection projection();

    SqlTable from();

    SqlGroupBy groupBy();

}
