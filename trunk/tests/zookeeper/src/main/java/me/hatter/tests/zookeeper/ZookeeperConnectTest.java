package me.hatter.tests.zookeeper;

import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ZookeeperConnectTest {

    public static void main(String[] args) {
        final ZkClient zkClient = new ZkClient("c2.hatter.zj.cn:2181,d2.hatter.zj.cn:2181/");
        zkClient.createEphemeral("/testnode_111", null);
        zkClient.subscribeStateChanges(new IZkStateListener() {

            public void handleStateChanged(KeeperState state) throws Exception {
                System.out.println("@state: " + state);
                if (state == KeeperState.SyncConnected) {
                    if (!zkClient.exists("/testnode_111")) {
                        zkClient.createEphemeral("/testnode_111", null);
                    }
                }
            }

            public void handleNewSession() throws Exception {
                System.out.println("@handle new session");
            }
        });

        for (int i = 0; i < 1000; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        zkClient.close();
    }
}
