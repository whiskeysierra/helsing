package org.whiskeysierra.helsing.sql;

public interface SqlParser {

    SelectStatement parse(String sql);

}
