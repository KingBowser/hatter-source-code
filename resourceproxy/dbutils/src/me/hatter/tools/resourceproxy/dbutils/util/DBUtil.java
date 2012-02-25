package me.hatter.tools.resourceproxy.dbutils.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hatter.tools.resourceproxy.dbutils.annotation.NonField;
import me.hatter.tools.resourceproxy.dbutils.annotation.Table;

public class DBUtil {

    private static Map<Class<?>, String>       tableNameMap   = new HashMap<Class<?>, String>();
    private static Map<Class<?>, List<String>> fieldListMap   = new HashMap<Class<?>, List<String>>();
    private static Map<Class<?>, List<String>> fieldPkListMap = new HashMap<Class<?>, List<String>>();

    synchronized public static String getTableName(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        String tableName = tableNameMap.get(clazz);
        if (tableName != null) {
            return tableName;
        }
        Table table = clazz.getAnnotation(Table.class);
        if ((table != null) && (!table.name().isEmpty())) {
            tableName = table.name();
        }
        tableName = StringUtil.toUnder(clazz.getName());
        tableNameMap.put(clazz, tableName);
        return tableName;
    }

    synchronized public static List<String> getTableFields(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        List<String> fieldList = fieldListMap.get(clazz);
        if (fieldList != null) {
            return fieldList;
        }
        List<Field> fieldTypeList = getDatabaseTableFields(clazz);
        fieldList = transformToDatabaseName(fieldTypeList);
        fieldListMap.put(clazz, fieldList);
        return fieldList;
    }

    synchronized public static List<String> getPkList(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        List<String> fieldList = fieldPkListMap.get(clazz);
        if (fieldList != null) {
            return fieldList;
        }
        List<Field> fieldTypeList = CollUtil.filter(getDatabaseTableFields(clazz), new CollUtil.Filter<Field>() {

            @Override
            public boolean accept(Field object) {
                me.hatter.tools.resourceproxy.dbutils.annotation.Field f = object.getAnnotation(me.hatter.tools.resourceproxy.dbutils.annotation.Field.class);
                if (f == null) {
                    return false;
                }
                return f.id();
            }
        });
        fieldList = transformToDatabaseName(fieldTypeList);
        fieldPkListMap.put(clazz, fieldList);
        return fieldList;
    }

    private static List<String> transformToDatabaseName(List<Field> fieldTypeList) {
        return CollUtil.transform(fieldTypeList, new CollUtil.Transformer<Field, String>() {

            @Override
            public String transform(Field object) {
                me.hatter.tools.resourceproxy.dbutils.annotation.Field f = object.getAnnotation(me.hatter.tools.resourceproxy.dbutils.annotation.Field.class);
                if ((f != null) && (!f.name().isEmpty())) {
                    return f.name();
                } else {
                    return StringUtil.toUnder(object.getName());
                }
            }
        });
    }

    private static List<Field> getDatabaseTableFields(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        CollUtil.Filter<Field> fieldFilter = null;
        if ((table != null) && (!table.defaultAllFields())) {
            fieldFilter = new CollUtil.Filter<Field>() {

                public boolean accept(Field object) {
                    return (object.getAnnotation(me.hatter.tools.resourceproxy.dbutils.annotation.Field.class) != null);
                }
            };
        } else {
            fieldFilter = new CollUtil.Filter<Field>() {

                @Override
                public boolean accept(Field object) {
                    return (object.getAnnotation(NonField.class) == null);
                }
            };
        }
        return CollUtil.filter(ReflectUtil.getFields(clazz), fieldFilter);
    }
}
