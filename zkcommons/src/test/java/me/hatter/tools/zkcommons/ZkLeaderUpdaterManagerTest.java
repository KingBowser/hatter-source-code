package me.hatter.tools.zkcommons;

public class ZkLeaderUpdaterManagerTest {

    public static void main(String[] args) {
        ZkCommonClient client1 = new ZkCommonClient("localhost:2181");
        ZkCommonClient client2 = new ZkCommonClient("localhost:2181");

        ZkLeaderUpdaterManager manager1 = new ZkLeaderUpdaterManager(client1.getZkClient(), "/cluster_group", "node",
                                                                     null);
        ZkLeaderUpdaterManager manager2 = new ZkLeaderUpdaterManager(client2.getZkClient(), "/cluster_group", "node",
                                                                     null);

        manager1.addUpdater(new ZkLeaderUpdater() {

            public void updateStatus(ZkLeaderStatus status) {
                System.out.println("[INFO] Tx1 " + status);
            }
        });
        manager2.addUpdater(new ZkLeaderUpdater() {

            public void updateStatus(ZkLeaderStatus status) {
                System.out.println("[INFO] Tx2 " + status);
            }
        });
        manager1.start();
        manager2.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        client1.close();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        client2.close();
    }
}
