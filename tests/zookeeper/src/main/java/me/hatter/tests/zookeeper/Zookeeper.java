package me.hatter.tests.zookeeper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

public class Zookeeper {

    private static volatile String me;

    public static void main(String[] args) throws Exception {
        ZkClient zkClient = new ZkClient("c1.hatter.zj.cn:2181,b1.hatter.zj.cn:2181,a1.hatter.zj.cn:2181/");
        // final ZkClient zkClient = new ZkClient("127.0.0.1:2181");
        zkClient.subscribeChildChanges("/group", new IZkChildListener() {

            public void handleChildChange(String arg0, List<String> arg1) throws Exception {
                List<String> nl = new ArrayList<String>(arg1);
                Collections.sort(nl);
                if (nl.size() > 0) {
                    if ((arg0 + "/" + nl.get(0)).equals(me)) {
                        System.out.println("[CHANGE] I am the leader: " + me);
                    }
                }
            }
        });
        me = zkClient.createEphemeralSequential("/group/default", null);
        // System.out.println("[ME] " + me);
        Thread.sleep(new Random(System.currentTimeMillis()).nextInt(100000));
        zkClient.close();
    }
}
