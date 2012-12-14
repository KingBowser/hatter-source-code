package me.hatter.tests.zookeeper;

import org.I0Itec.zkclient.ZkClient;

public class ZookeeperCatTree {

    public static void main(String[] args) throws Exception {
        ZkClient zkClient = new ZkClient("c1.hatter.zj.cn:2181,b1.hatter.zj.cn:2181,a1.hatter.zj.cn:2181/");
        // final ZkClient zkClient = new ZkClient("127.0.0.1:2181");
        zkClient.showFolders(System.out);
        zkClient.close();
    }
}
