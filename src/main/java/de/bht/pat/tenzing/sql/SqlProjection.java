package de.bht.pat.tenzing.sql;

import java.util.List;

public interface SqlProjection extends List<SqlExpression> {

    List<String> toStrings();

}
