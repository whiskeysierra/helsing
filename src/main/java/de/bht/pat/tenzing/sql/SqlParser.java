package de.bht.pat.tenzing.sql;

public interface SqlParser {

    SelectStatement parse(String sql);

}
