package me.hatter.tools.resourceproxy.dbutils.dataaccess.dialect.impl;

import me.hatter.tools.resourceproxy.dbutils.dataaccess.dialect.DatabaseDialect;

public class MySQLDialect implements DatabaseDialect {

    public String lastIdQuery() {
        return "select last_insert_id() id";
    }
}
