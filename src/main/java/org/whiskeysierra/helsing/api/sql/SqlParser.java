package org.whiskeysierra.helsing.api.sql;

public interface SqlParser {

    SelectStatement parse(String sql);

}
