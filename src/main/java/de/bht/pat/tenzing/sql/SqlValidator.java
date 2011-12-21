package de.bht.pat.tenzing.sql;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.bht.pat.tenzing.events.SqlEvent;
import de.bht.pat.tenzing.events.QueryEvent;
import de.bht.pat.tenzing.events.SyntaxError;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;

import javax.inject.Inject;
import java.io.StringReader;
import java.sql.SQLException;

final class SqlValidator {

    private final EventBus bus;

    @Inject
    public SqlValidator(EventBus bus) {
        this.bus = bus;

        bus.register(this);
    }

    @Subscribe
    public void onInput(SqlEvent event) throws SQLException {
        try {
            final CCJSqlParserManager manager = new CCJSqlParserManager();
            final String sql = event.getSql();
            manager.parse(new StringReader(sql));
            bus.post(new QueryEvent(sql));
        } catch (JSQLParserException e) {
            bus.post(new SyntaxError(e));
        }
    }

}
