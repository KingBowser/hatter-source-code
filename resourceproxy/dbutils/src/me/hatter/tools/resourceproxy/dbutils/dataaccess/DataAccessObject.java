package me.hatter.tools.resourceproxy.dbutils.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.hatter.tools.resourceproxy.dbutils.factory.ConnectionPool;

//
public class DataAccessObject {

    private static final ConnectionPool connectionPool = new ConnectionPool();

//    public static void main(String[] args) {
//        AccessItem ai = getAccessItem("1.1.1.1", "http://sample.com/");
//        System.out.println(ai.getId());
//        System.out.println(ai.getContent());
//        System.out.println(ai.getHeader());
//    }
//
//    public static interface Execute<T> {
//
//        T execute(Connection connection) throws Exception;
//    }
//
//    protected static <T> T execute(Execute<T> execute) {
//        Connection connection = connectionPool.borrowConnection();
//        try {
//            return execute.execute(connection);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            connectionPool.returnConnection(connection);
//        }
//    }
//
//    public static void updateAccessItemCount(final String ip, final String url) {
//        execute(new Execute<Void>() {
//
//            @Override
//            public Void execute(Connection connection) throws Exception {
//                PreparedStatement preparedStatement = connection.prepareStatement(/* NL */
//                // _______________________________1_______________________________________2_________3__
//                "update access_table set modified=?, access_count=access_count+1 where ip=? and url=?");
//                preparedStatement.setString(1, getSDF().format(new Date()));
//                preparedStatement.setString(2, ip);
//                preparedStatement.setString(3, url);
//                preparedStatement.execute();
//                return null;
//            }
//        });
//    }
//
//    public static void putAccessItem(final AccessItem accessItem) {
//        execute(new Execute<Void>() {
//
//            @Override
//            public Void execute(Connection connection) throws Exception {
//                AccessItem accessItemFromDB = getAccessItem(accessItem.getIp(), accessItem.getUrl());
//                if (accessItemFromDB == null) {
//                    PreparedStatement preparedStatement = connection.prepareStatement( /* NL */
//                    "insert into access_table "
//                            // ________1___2________3_________4_____________5________6_______7______8____9________________
//                            + "       (ip, created, modified, access_count, content, header, status, url, custom_content) "
//                            + "values (?,  ?,       ?,        ?,            ?,       ?,      ?,      ?,   ?)");
//                    preparedStatement.setString(1, accessItem.getIp());
//                    preparedStatement.setString(2, getSDF().format(new Date()));
//                    preparedStatement.setString(3, getSDF().format(new Date()));
//                    preparedStatement.setInt(4, 1);
//                    preparedStatement.setString(5, accessItem.getContent());
//                    preparedStatement.setString(6, accessItem.getHeader());
//                    preparedStatement.setInt(7, accessItem.getStatus());
//                    preparedStatement.setString(8, accessItem.getUrl());
//                    preparedStatement.setBoolean(9, accessItem.isCustom_content());
//                    preparedStatement.execute();
//                } else {
//                    if (!accessItemFromDB.isCustom_content()) {
//                        PreparedStatement preparedStatement = connection.prepareStatement(/* NL */
//                        "update access_table set "
//                        // _________________1_______________________________________2_________3_________4______5__
//                                + "modified=?, access_count=access_count+1, content=?, header=?, status=?, url=?");
//                        preparedStatement.setString(1, getSDF().format(new Date()));
//                        preparedStatement.setString(2, accessItem.getContent());
//                        preparedStatement.setString(3, accessItem.getHeader());
//                        preparedStatement.setInt(4, accessItem.getStatus());
//                        preparedStatement.setString(5, accessItem.getUrl());
//                        preparedStatement.execute();
//                    }
//                }
//
//                return null;
//            }
//        });
//    }
//
//    public static AccessItem getAccessItem(final String ip, final String url) {
//        return execute(new Execute<AccessItem>() {
//
//            @Override
//            public AccessItem execute(Connection connection) throws Exception {
//                PreparedStatement preparedStatement = connection.prepareStatement("select * from access_table where ip=? and url=?");
//                preparedStatement.setString(1, ip);
//                preparedStatement.setString(2, url);
//                ResultSet resultSet = preparedStatement.executeQuery();
//
//                AccessItem accessItem = null;
//                if (resultSet.next()) {
//                    accessItem = new AccessItem();
//                    accessItem.setId(resultSet.getInt("id"));
//                    accessItem.setIp(resultSet.getString("ip"));
//                    accessItem.setCreated(getSDF().parse(resultSet.getString("created")));
//                    accessItem.setModified(getSDF().parse(resultSet.getString("modified")));
//                    accessItem.setAccess_count(resultSet.getInt("access_count"));
//                    accessItem.setUrl(resultSet.getString("url"));
//                    accessItem.setStatus(resultSet.getInt("status"));
//                    accessItem.setHeader(resultSet.getString("header"));
//                    accessItem.setContent(resultSet.getString("content"));
//                }
//
//                return accessItem;
//            }
//        });
//    }

    private static SimpleDateFormat getSDF() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }
}
