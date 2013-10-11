package me.hatter.tools.resourceproxy.dbutils.dataaccess.dialect.impl;

import me.hatter.tools.resourceproxy.dbutils.dataaccess.dialect.DatabaseDialect;

public class SQLiteDialect implements DatabaseDialect {

    public String lastIdQuery() {
        return "select last_insert_rowid() id";
    }
}
