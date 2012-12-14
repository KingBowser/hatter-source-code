package me.hatter.tests.datasource;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

import com.alibaba.druid.pool.ha.DataSourceHolder;
import com.alibaba.druid.pool.ha.config.ConfigLoader;

public class ZookeeperCobarConfigLoader implements ConfigLoader {

    private ZkClient                            zkClient;

    private final ZookeeperCobarDruidDataSource dataSource;

    public ZookeeperCobarConfigLoader(ZookeeperCobarDruidDataSource cobarDataSource) throws SQLException {
        this.dataSource = cobarDataSource;
    }

    public ZookeeperCobarDruidDataSource getDataSource() {
        return dataSource;
    }

    public static boolean isCobar(String url) {
        return url.startsWith("jdbc:cobarzk://");
    }

    protected String createJdbcUrl(String ip, int port, String schema) {
        return "jdbc:mysql://" + ip + ":" + port + "/" + schema;
    }

    public void load() throws SQLException {
        if (zkClient != null) return;// load once is OK
        String url = dataSource.getUrl();
        if (!isCobar(url)) {
            throw new SQLException("URL format is not cobar/zk: " + url);
        }
        String cobarZkUrl = url.substring("jdbc:cobarzk://".length());
        int indexOfC = cobarZkUrl.lastIndexOf(';');
        if (indexOfC < 0) {
            throw new SQLException("URL format is not cobar/zk: " + url);
        }
        String zk = cobarZkUrl.substring(0, indexOfC);
        String clusterAndSchema = cobarZkUrl.substring(indexOfC + 1);
        int indexOfX = clusterAndSchema.indexOf(':');
        if (indexOfX < 0) {
            throw new SQLException("URL format is not cobar/zk: " + url);
        }
        String cluster = clusterAndSchema.substring(0, indexOfX);
        final String schema = clusterAndSchema.substring(indexOfX + 1);

        final ZkClient zkc = new ZkClient(zk);
        String serversPath = "/cobar/" + cluster + "/servers";
        List<String> servers = zkc.getChildren(serversPath);

        zkc.subscribeChildChanges(serversPath, new IZkChildListener() {

            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                updateDataSources(zkc, parentPath, currentChilds, schema);
            }
        });

        // update
        updateDataSources(zkc, serversPath, servers, schema);

        zkClient = zkc;// OK
    }

    synchronized private void updateDataSources(ZkClient zkc, String serversPath, List<String> servers, String schema)
                                                                                                                      throws SQLException {
        Set<String> keys = new HashSet<String>();
        for (String server : servers) {
            try {
                // XXX ignore weight
                String ipAndPort = zkc.readData(serversPath + "/" + server);
                String[] ipPorts = ipAndPort.split(":");
                DataSourceHolder holder = dataSource.getDataSourceHolder(ipAndPort);
                if (holder == null) {
                    System.out.println("[WARN] Add server: " + ipAndPort);
                    holder = dataSource.createDataSourceHolder(createJdbcUrl(ipPorts[0], Integer.valueOf(ipPorts[1]),
                                                                             schema), 1);
                    dataSource.addDataSource(ipAndPort, holder);
                }
                keys.add(ipAndPort);
            } catch (ZkNoNodeException nee) {
                // IGNORE
            }
        }
        int removeCount = 0;
        Iterator<Map.Entry<String, DataSourceHolder>> iter = dataSource.getDataSources().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, DataSourceHolder> entry = iter.next();
            if (!keys.contains(entry.getKey())) {
                System.out.println("[WARN] Remove server: " + entry.getKey());
                iter.remove();
                dataSource.handleDataSourceDiscard(entry.getValue());
                removeCount++;
            }
        }
        if (removeCount != 0) {
            dataSource.afterDataSourceChanged(null);
        }
    }
}
