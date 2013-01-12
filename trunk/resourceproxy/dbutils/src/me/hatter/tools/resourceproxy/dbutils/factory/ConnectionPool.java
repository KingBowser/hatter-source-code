package me.hatter.tools.resourceproxy.dbutils.factory;

import java.sql.Connection;
import java.util.Stack;

public class ConnectionPool {

    private int               poolSize        = 5;
    private Stack<Connection> connectionStack = new Stack<Connection>();
    private ConnectionFactory connectionFactory;

    public ConnectionPool(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Connection borrowConnection() {
        synchronized (connectionStack) {
            if (!connectionStack.isEmpty()) {
                return connectionStack.pop();
            }
        }
        return connectionFactory.getConnection();
    }

    public void returnConnection(Connection connection) {
        boolean returnedToPool = false;
        synchronized (connectionStack) {
            if ((connectionStack.size() < poolSize) && connectionFactory.validConnection(connection)) {
                connectionStack.push(connection);
                returnedToPool = true;
            }
        }
        if (!returnedToPool) {
            connectionFactory.destoryConnection(connection);
        }
    }

    public void returnConnectionWithError(Connection connection) {
        connectionFactory.destoryConnection(connection);
    }
}
