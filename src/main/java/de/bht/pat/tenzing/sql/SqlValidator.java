package de.bht.pat.tenzing.sql;

import java.sql.SQLException;

public interface SqlValidator {

    void validate(String sql) throws SQLException;

}
