package me.hatter.tools.zkcommons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.hatter.tools.zkcommons.util.ZkCommonUtil;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

public class ZkLeaderUpdaterManager {

    private ZkClient              zkClient;
    private String                basePath;
    private String                nodeName;
    private Object                data;
    private String                thisNodeFullName;
    private ZkLeaderStatus        status   = null;
    private List<ZkLeaderUpdater> updaters = new ArrayList<ZkLeaderUpdater>();

    public ZkLeaderUpdaterManager(ZkClient zkClient, String basePath, String nodeName, Object data) {
        this.zkClient = zkClient;
        this.basePath = basePath;
        this.nodeName = nodeName;
        this.data = data;
    }

    public List<ZkLeaderUpdater> getUpdaters() {
        return updaters;
    }

    public void setUpdaters(List<ZkLeaderUpdater> updaters) {
        this.updaters = updaters;
    }

    public void addUpdater(ZkLeaderUpdater updater) {
        updaters.add(updater);
    }

    public void start() {
        ZkCommonUtil.createPersistent(zkClient, basePath);
        this.thisNodeFullName = zkClient.createEphemeralSequential(basePath + "/" + nodeName, data);
        zkClient.subscribeChildChanges(basePath, new IZkChildListener() {

            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                update(parentPath, currentChilds);
            }
        });
        List<String> childs = zkClient.getChildren(basePath);
        update(basePath, childs);
    }

    synchronized public void update(String parentPath, List<String> nodes) {
        if (nodes.isEmpty()) {
            // something error
            return;
        }
        List<String> nodeList = new ArrayList<String>(nodes);
        Collections.sort(nodeList);
        String firstNodeName = nodeList.get(0);
        boolean isThisFirstNode = (parentPath + "/" + firstNodeName).equals(thisNodeFullName);
        setLeader(isThisFirstNode);
    }

    public void setLeader(boolean isLeader) {
        ZkLeaderStatus newStatus = isLeader ? ZkLeaderStatus.LEADER : ZkLeaderStatus.FOLLOWER;
        if (status == newStatus) return; // status not changed
        status = newStatus;
        if (updaters == null) return;
        for (ZkLeaderUpdater updater : updaters) {
            updater.updateStatus(newStatus);
        }
    }
}
