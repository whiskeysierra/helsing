package de.bht.pat.tenzing.sql;

public interface SelectStatement {

    SqlProjection projection();

    SqlTable from();

    SqlGroupBy groupBy();

}
