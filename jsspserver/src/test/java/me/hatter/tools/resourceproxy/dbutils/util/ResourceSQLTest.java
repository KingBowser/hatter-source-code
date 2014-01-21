package me.hatter.tools.resourceproxy.dbutils.util;

public class ResourceSQLTest {

    public static void main(String[] args) {
        System.out.println(XSQL.resource().sql("test").variable("IDS", 2));
        System.out.println(XSQL.resource().sql("test").variable("IDS", "?,?,?"));
        System.out.println(XSQL.resource("sql/sql2.xml").sql("test2"));
    }
}