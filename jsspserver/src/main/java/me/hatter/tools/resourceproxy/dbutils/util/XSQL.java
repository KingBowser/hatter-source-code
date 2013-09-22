package me.hatter.tools.resourceproxy.dbutils.util;

import java.util.Properties;

public class XSQL {

    public static interface TokenResolver {

        String resolve(String token);
    }

    public static void main(String[] args) {
        String a = "aa@@x@@b+x@@ee##1##";
        String b = "##";
        TokenResolver tr = new TokenResolver() {

            public String resolve(String token) {
                return "(" + token + ")";
            }
        };
        System.out.println(resolveSqlToken(a, b, tr));
    }

    private String     id;
    private String     rawSql;
    private Properties refProperties;

    public XSQL(String id, Properties properties) {
        this.id = id;
        this.rawSql = properties.getProperty(id);
        this.refProperties = properties;
    }

    public static ResourceSQL resource() {
        return new ResourceSQL("sql.xml");
    }

    public static ResourceSQL resource(String resource) {
        return new ResourceSQL(resource);
    }

    protected static String resolveSqlToken(String text, final String mark, final TokenResolver tokenResolver) {
        if ((text != null) && (text.contains(mark))) {
            StringBuilder sb = new StringBuilder(text.length() + 100);
            String[] sqlSeqs = (" " + text + " ").split(mark);
            for (int i = 0; i < sqlSeqs.length; i++) {
                if (((i % 2) == 1) && (i < (sqlSeqs.length - 1))) {
                    String findSql = tokenResolver.resolve(sqlSeqs[i]);
                    if (findSql != null) {
                        sb.append(findSql);
                    } else {
                        sb.append(mark);
                        sb.append(sqlSeqs[i]);
                        sb.append(mark);
                    }
                } else {
                    if ((i % 2) == 1) {
                        sb.append(mark);
                    }
                    sb.append(sqlSeqs[i]);
                }
            }
            text = sb.toString().substring(1, (sb.length() - 1));
        }
        return text;
    }

    @Override
    public String toString() {
        return null;// XXX
    }
}
