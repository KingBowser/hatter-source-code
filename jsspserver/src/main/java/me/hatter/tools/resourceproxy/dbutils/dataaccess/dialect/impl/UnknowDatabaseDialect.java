package me.hatter.tools.resourceproxy.dbutils.dataaccess.dialect.impl;

import me.hatter.tools.resourceproxy.dbutils.dataaccess.dialect.DatabaseDialect;

public class UnknowDatabaseDialect implements DatabaseDialect {

    public String lastIdQuery() {
        throw new UnsupportedOperationException();
    }
}
