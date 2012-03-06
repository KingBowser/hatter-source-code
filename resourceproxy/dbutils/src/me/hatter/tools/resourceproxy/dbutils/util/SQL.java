package me.hatter.tools.resourceproxy.dbutils.util;

import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.dbutils.objects.Test;

public class SQL {

    public static void main(String[] a) {
        System.out.println(SQL.sql(Cmd.DELETE).table(Test.class).where("id=?").get());
        System.out.println(SQL.sql(Cmd.SELECT).table(Test.class).where("id=?").get());
        System.out.println(SQL.sql(Cmd.UPDATE).table(Test.class).update("name=?").where("id=?").get());
    }

    public static enum Cmd {
        DELETE, UPDATE, SELECT;
    }

    private String command;
    private String table;
    private String update;
    private String where;

    public static SQL sql(Cmd command) {
        SQL sql = new SQL();
        sql.command = command.name();
        return sql;
    }

    public SQL table(Class<?> clazz) {
        return table(DBUtil.getTableName(clazz));
    }

    public SQL table(String table) {
        this.table = table;
        return this;
    }

    public SQL update(String update) {
        this.update = update;
        return this;
    }

    public SQL where(String where) {
        this.where = where;
        return this;
    }

    public String get() {
        StringBuilder sql = new StringBuilder();
        sql.append(command.toLowerCase());
        if (Cmd.DELETE.name().equalsIgnoreCase(StringUtil.trim(command))) {
            sql.append(" from ").append(table);
            if (where != null) {
                sql.append(" where ").append(where);
            }
        } else if (Cmd.UPDATE.name().equalsIgnoreCase(StringUtil.trim(command))) {
            sql.append(" ").append(table).append(" set ").append(update).append(" where ").append(where);
        } else if (Cmd.SELECT.name().equalsIgnoreCase(StringUtil.trim(command))) {
            sql.append(" * from ").append(table);
            if (where != null) {
                sql.append(" where ").append(where);
            }
        } else {
            throw new RuntimeException("Unknow command: " + command);
        }
        return sql.toString();
    }
}
