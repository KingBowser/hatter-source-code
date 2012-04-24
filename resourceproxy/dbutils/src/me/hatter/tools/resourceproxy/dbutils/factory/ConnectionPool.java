package me.hatter.tools.resourceproxy.dbutils.factory;

import java.sql.Connection;
import java.util.Stack;

public class ConnectionPool {

    private int               poolSize        = 5;
    private Stack<Connection> connectionStack = new Stack<Connection>();

    public ConnectionPool() {
    }

    public Connection borrowConnection() {
        synchronized (connectionStack) {
            if (!connectionStack.isEmpty()) {
                return connectionStack.pop();
            }
        }
        return ConnectionFactory.getConnection();
    }

    public void returnConnection(Connection connection) {
        boolean returnedToPool = false;
        synchronized (connectionStack) {
            if ((connectionStack.size() < poolSize) && ConnectionFactory.validConnection(connection)) {
                connectionStack.push(connection);
                returnedToPool = true;
            }
        }
        if (!returnedToPool) {
            ConnectionFactory.destoryConnection(connection);
        }
    }

    public void returnConnectionWithError(Connection connection) {
        ConnectionFactory.destoryConnection(connection);
    }
}
