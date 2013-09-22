package me.hatter.tools.resourceproxy.dbutils.util;

import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.dbutils.objects.Test;

public class SQL {

    private static final LogTool logTool = LogTools.getLogTool(SQL.class);

    public static void main(String[] a) {
        logTool.info(SQL.sql(Cmd.DELETE).table(Test.class).where("id=?").get());
        logTool.info(SQL.sql(Cmd.SELECT).table(Test.class).where("id=?").get());
        logTool.info(SQL.sql(Cmd.UPDATE).table(Test.class).update("name=?").where("id=?").get());
        logTool.info(SQL.deleteQuery(Test.class).where("id=?").get());
        logTool.info(SQL.selectQuery(Test.class).where("id=?").get());
        logTool.info(SQL.updateQuery(Test.class).update("name=?").where("id=?").get());
        logTool.info(SQL.updateQuery(Test.class).update("name=?").update("sex=?").where("id=?").get());
    }

    public static enum Cmd {
        DELETE, UPDATE, SELECT;
    }

    private String       command;
    private String       table;
    private List<String> updates = new ArrayList<String>();
    private String       where;
    private String       order;
    private List<Object> params  = new ArrayList<Object>();

    public static SQL sql(Cmd command) {
        SQL sql = new SQL();
        sql.command = command.name();
        return sql;
    }

    public static SQL selectQuery() {
        return sql(Cmd.SELECT);
    }

    public static SQL updateQuery() {
        return sql(Cmd.UPDATE);
    }

    public static SQL deleteQuery() {
        return sql(Cmd.DELETE);
    }

    public static SQL selectQuery(Class<?> clazz) {
        return selectQuery().table(clazz);
    }

    public static SQL updateQuery(Class<?> clazz) {
        return updateQuery().table(clazz);
    }

    public static SQL deleteQuery(Class<?> clazz) {
        return deleteQuery().table(clazz);
    }

    public SQL table(Class<?> clazz) {
        return table(DBUtil.getTableName(clazz));
    }

    public SQL table(String table) {
        this.table = table;
        return this;
    }

    public SQL update(String update, Object... params) {
        this.updates.add(update);
        if (params != null) {
            for (Object param : params) {
                this.params.add(param);
            }
        }
        return this;
    }

    public SQL where(String where, Object... params) {
        this.where = where;
        if (params != null) {
            for (Object param : params) {
                this.params.add(param);
            }
        }
        return this;
    }

    public SQL order(String order) {
        this.order = order;
        return this;
    }

    public String toString() {
        return get();
    }

    public List<Object> getParams() {
        return this.params;
    }

    public String get() {
        StringBuilder sql = new StringBuilder();
        sql.append(command.toLowerCase());
        if (Cmd.DELETE.name().equalsIgnoreCase(StringUtil.trim(command))) {
            sql.append(" from ").append(table);
            if (where == null) {
                throw new IllegalArgumentException("Delete query must have where!");
            }
            sql.append(" where ").append(where);
        } else if (Cmd.UPDATE.name().equalsIgnoreCase(StringUtil.trim(command))) {
            if (where == null) {
                throw new IllegalArgumentException("Update query must have where!");
            }
            sql.append(" ").append(table);
            sql.append(" set ").append(StringUtil.join(updates, ", "));
            sql.append(" where ").append(where);
        } else if (Cmd.SELECT.name().equalsIgnoreCase(StringUtil.trim(command))) {
            sql.append(" * from ").append(table);
            if (where != null) {
                sql.append(" where ").append(where);
            }
            if (order != null) {
                sql.append(" order by " + order);
            }
        } else {
            throw new IllegalStateException("Unknow command: " + command);
        }
        return sql.toString();
    }
}
