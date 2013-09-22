package me.hatter.tools.resourceproxy.dbutils.util;

public class ResourceSQLTest {

    public static void main(String[] args) {
        System.out.println(SQL.resource().sql("test"));
        System.out.println(SQL.resource("sql/sql2.xml").sql("test2"));
    }
}
