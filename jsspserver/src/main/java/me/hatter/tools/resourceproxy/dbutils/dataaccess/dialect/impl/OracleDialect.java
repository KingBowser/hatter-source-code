package me.hatter.tools.resourceproxy.dbutils.dataaccess.dialect.impl;

import me.hatter.tools.resourceproxy.dbutils.dataaccess.dialect.DatabaseDialect;

public class OracleDialect implements DatabaseDialect {

    public String lastIdQuery() {
        throw new UnsupportedOperationException();
    }
}
