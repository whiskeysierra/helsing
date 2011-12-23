package de.bht.pat.tenzing.sql;

import net.sf.jsqlparser.schema.Table;

final class DefaultSqlTable implements SqlTable {

    private final Table table;

    public DefaultSqlTable(Table table) {
        this.table = table;
    }

    @Override
    public String name() {
        return table.getWholeTableName();
    }

}
