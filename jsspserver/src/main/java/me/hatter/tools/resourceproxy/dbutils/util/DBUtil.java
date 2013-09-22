package me.hatter.tools.resourceproxy.dbutils.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.hatter.tools.resourceproxy.commons.util.CollUtil;
import me.hatter.tools.resourceproxy.commons.util.ReflectUtil;
import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.dbutils.annotation.NonField;
import me.hatter.tools.resourceproxy.dbutils.annotation.Table;
import me.hatter.tools.resourceproxy.dbutils.annotation.UpdateIgnore;

public class DBUtil {

    private static Map<Class<?>, String>             tableNameMap         = new HashMap<Class<?>, String>();
    private static Map<Class<?>, List<String>>       fieldListMap         = new HashMap<Class<?>, List<String>>();
    private static Map<Class<?>, List<String>>       fieldPkListMap       = new HashMap<Class<?>, List<String>>();
    private static Map<Class<?>, List<String>>       fieldUpIgnoreListMap = new HashMap<Class<?>, List<String>>();
    private static Map<Class<?>, Map<String, Field>> databaseNameFileMap  = new HashMap<Class<?>, Map<String, Field>>();

    public static List<Object> objects(Object... objects) {
        return Arrays.asList(objects);
    }

    public static String generateCreateSQL(final Class<?> clazz) {
        String tableName = getTableName(clazz);
        List<String> fieldList = getTableFields(clazz);
        final Set<String> pkSet = new HashSet<String>(getPkList(clazz));

        StringBuilder sql = new StringBuilder();
        sql.append("create table ");
        sql.append(tableName);
        sql.append(" ( ");
        sql.append(StringUtil.join(CollUtil.transform(fieldList, new CollUtil.Transformer<String, String>() {

            // http://www.sqlite.org/datatype3.html#2.2 Affinity Name Examples
            // @Override
            public String transform(String object) {
                Field f = ReflectUtil.getField(clazz, StringUtil.toCamel(object));
                String type = null;
                if (f.getType() == String.class) {
                    type = "TEXT";
                } else if (f.getType() == Integer.class) {
                    type = "INTEGER";
                } else if (f.getType() == Long.class) {
                    type = "LONG";
                } else if (f.getType() == Date.class) {
                    type = "TEXT";
                } else if (f.getType() == Boolean.class) {
                    type = "NUMERIC";
                } else {
                    throw new RuntimeException("Unsupoort ed type: " + type);
                }
                return object + " " + type + ((pkSet.contains(object)) ? " PRIMARY KEY" : "");
            }
        }), ", "));
        sql.append(" )");

        return sql.toString();
    }

    public static String generateInsertSQL(Class<?> clazz, List<String> refFieldList) {
        String tableName = getTableName(clazz);
        List<String> fieldList = getTableFields(clazz);
        List<String> quesList = transformToQuesList(fieldList);

        StringBuilder sql = new StringBuilder();
        sql.append("insert into ");
        sql.append(tableName);
        sql.append(" (");
        sql.append(StringUtil.join(fieldList, ", "));
        sql.append(") values (");
        sql.append(StringUtil.join(quesList, ", "));
        sql.append(")");

        refFieldList.addAll(fieldList);

        return sql.toString();
    }

    public static String generateSelectSQL(Class<?> clazz, List<String> refFieldList) {
        String tableName = getTableName(clazz);
        List<String> pkList = getPkList(clazz);

        StringBuilder sql = new StringBuilder();
        sql.append("select * from ");
        sql.append(tableName);
        sql.append(" where ");
        sql.append(StringUtil.join(CollUtil.transform(pkList, new CollUtil.Transformer<String, String>() {

            // @Override
            public String transform(String object) {
                return (object + " = ?");
            }
        }), " and "));

        refFieldList.addAll(pkList);

        return sql.toString();
    }

    public static String generateUpdateSQL(Class<?> clazz, List<String> refFieldList) {
        String tableName = getTableName(clazz);
        List<String> fieldList = getTableFields(clazz);
        List<String> pkList = getPkList(clazz);
        List<String> upIgnoreList = getUpIgnoreList(clazz);
        List<String> setFieldList = CollUtil.minus(CollUtil.minus(fieldList, pkList), upIgnoreList);

        StringBuilder sql = new StringBuilder();
        sql.append("update ");
        sql.append(tableName);
        sql.append(" set ");
        sql.append(StringUtil.join(CollUtil.transform(setFieldList, new CollUtil.Transformer<String, String>() {

            // @Override
            public String transform(String object) {
                return (object + " = ?");
            }
        }), ", "));
        sql.append(" where ");
        sql.append(StringUtil.join(CollUtil.transform(pkList, new CollUtil.Transformer<String, String>() {

            // @Override
            public String transform(String object) {
                return (object + " = ?");
            }
        }), " and "));

        refFieldList.addAll(setFieldList);
        refFieldList.addAll(pkList);

        return sql.toString();
    }

    public static String generateDeleteSQL(Class<?> clazz, List<String> refFieldList) {
        String tableName = getTableName(clazz);
        List<String> pkList = getPkList(clazz);

        StringBuilder sql = new StringBuilder();
        sql.append("delete from ");
        sql.append(tableName);
        sql.append(" where ");
        sql.append(StringUtil.join(CollUtil.transform(pkList, new CollUtil.Transformer<String, String>() {

            // @Override
            public String transform(String object) {
                return (object + " = ?");
            }
        }), " and "));

        refFieldList.addAll(pkList);

        return sql.toString();
    }

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
        } else {
            tableName = StringUtil.toUnder(clazz.getSimpleName());
        }
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

    synchronized public static List<String> getUpIgnoreList(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        List<String> fieldList = fieldUpIgnoreListMap.get(clazz);
        if (fieldList != null) {
            return fieldList;
        }
        List<Field> fieldTypeList = CollUtil.filter(getDatabaseTableFields(clazz), new CollUtil.Filter<Field>() {

            // @Override
            public boolean accept(Field object) {
                UpdateIgnore ui = object.getAnnotation(UpdateIgnore.class);
                return (ui != null);
            }
        });
        fieldList = transformToDatabaseName(fieldTypeList);
        fieldUpIgnoreListMap.put(clazz, fieldList);
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

            // @Override
            public boolean accept(Field object) {
                me.hatter.tools.resourceproxy.dbutils.annotation.Field f = object.getAnnotation(me.hatter.tools.resourceproxy.dbutils.annotation.Field.class);
                if (f == null) {
                    return false;
                }
                return f.pk();
            }
        });
        fieldList = transformToDatabaseName(fieldTypeList);
        fieldPkListMap.put(clazz, fieldList);
        return fieldList;
    }

    synchronized public static Field getClassFieldByDbName(Class<?> clazz, String databaseFieldName) {
        Map<String, Field> dbNameToField = databaseNameFileMap.get(clazz);
        if (dbNameToField == null) {
            List<Field> fieldTypeList = getDatabaseTableFields(clazz);
            dbNameToField = transformToDatabaseNameToFieldMapping(fieldTypeList);
            databaseNameFileMap.put(clazz, dbNameToField);
        }
        Field field = dbNameToField.get(StringUtil.toLowerCase(databaseFieldName));
        if (field == null) {
            throw new IllegalStateException("Cannot find database field name: " + databaseFieldName + " from clazz: "
                                            + clazz);
        }
        return field;
    }

    private static <T> List<String> transformToQuesList(List<T> list) {
        return CollUtil.transform(list, new CollUtil.Transformer<T, String>() {

            // @Override
            public String transform(T object) {
                return "?";
            }
        });
    }

    private static Map<String, Field> transformToDatabaseNameToFieldMapping(List<Field> fieldTypeList) {
        List<Object[]> dbNameToFieldList;
        dbNameToFieldList = CollUtil.transform(fieldTypeList, new CollUtil.Transformer<Field, Object[]>() {

            // @Override
            public Object[] transform(Field object) {
                me.hatter.tools.resourceproxy.dbutils.annotation.Field f = object.getAnnotation(me.hatter.tools.resourceproxy.dbutils.annotation.Field.class);
                if ((f != null) && (!f.name().isEmpty())) {
                    return new Object[] { f.name(), object };
                } else {
                    return new Object[] { StringUtil.toUnder(object.getName()), object };
                }
            }
        });
        Map<String, Field> dbNameToFiledMapping = new HashMap<String, Field>();
        for (Object[] dbNameToField : dbNameToFieldList) {
            dbNameToFiledMapping.put((String) dbNameToField[0], (Field) dbNameToField[1]);
        }
        return dbNameToFiledMapping;
    }

    private static List<String> transformToDatabaseName(List<Field> fieldTypeList) {
        return CollUtil.transform(fieldTypeList, new CollUtil.Transformer<Field, String>() {

            // @Override
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

                // @Override
                public boolean accept(Field object) {
                    return (object.getAnnotation(NonField.class) == null);
                }
            };
        }
        return CollUtil.filter(ReflectUtil.getFields(clazz), fieldFilter);
    }
}
