package me.hatter.tests.datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ZookeeperCobarDruidDatasourceTest {

    public static void main(String[] args) {
        ZookeeperCobarDruidDataSource dataSource = new ZookeeperCobarDruidDataSource();
        dataSource.setUrl("jdbc:cobarzk://c1.hatter.zj.cn:2181,b1.hatter.zj.cn:2181/;cluster1:dbtest");
        dataSource.setUsername("admin");
        dataSource.setPassword("admin");

        for (int i = 0; i < 1000; i++) {
            try {
                System.out.println("[INFO] Round: " + i);
                Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement("select * from test");
                ps.execute();
                ResultSet rs = ps.getResultSet();
                while (rs.next()) {
                    System.out.println("[INFO] " + rs.getLong("id") + ":" + rs.getString("value"));
                }
            } catch (Exception e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
            }
        }
    }
}
