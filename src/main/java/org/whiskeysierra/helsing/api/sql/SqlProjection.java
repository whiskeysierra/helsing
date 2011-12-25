package org.whiskeysierra.helsing.api.sql;

import java.util.List;

public interface SqlProjection extends List<SqlExpression> {

    List<String> toStrings();

}
