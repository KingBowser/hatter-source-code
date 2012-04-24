package me.hatter.tools.resourceproxy.dbutils.conn;

import java.sql.Connection;

public class TimeBasedValidConnection extends FilterConnection implements ConnectionValidity {

    private long initMillis;
    private long validMillis;

    public TimeBasedValidConnection(Connection conn, long validMillis) {
        super(conn);
        this.initMillis = System.currentTimeMillis();
        this.validMillis = validMillis;
    }

    public boolean isConnValid() {
        return (System.currentTimeMillis() > (initMillis + validMillis));
    }
}
