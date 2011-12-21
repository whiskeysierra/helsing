package de.bht.pat.tenzing.sql;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;

import java.io.StringReader;
import java.sql.SQLException;

public final class DefaultSqlValidator implements SqlValidator {

    @Override
    public void validate(String sql) throws SQLException {
        try {
            final CCJSqlParserManager manager = new CCJSqlParserManager();
            manager.parse(new StringReader(sql));
        } catch (JSQLParserException e) {
            throw new SQLException(e);
        }
    }

}
