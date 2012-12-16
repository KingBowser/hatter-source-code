package me.hatter.tools.zkcommons;

import org.I0Itec.zkclient.ZkClient;

public class ZkCommonClient {

    private boolean  isClosed;
    private ZkClient zkClient;

    public ZkCommonClient(String serverstring) {
        this(new ZkClient(serverstring));
    }

    public ZkCommonClient(ZkClient zkClient) {
        this.zkClient = zkClient;
        // close zk client when shutdown
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                close();
            }
        });
    }

    synchronized public void close() {
        if (isClosed) return;
        isClosed = true;
        zkClient.close();
    }

    public ZkClient getZkClient() {
        return zkClient;
    }
}
