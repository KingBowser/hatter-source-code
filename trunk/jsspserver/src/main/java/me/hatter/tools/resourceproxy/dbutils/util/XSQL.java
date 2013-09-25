package me.hatter.tools.resourceproxy.dbutils.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import me.hatter.tools.commons.string.StringUtil;

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

    private String              id;
    private String              rawSql;
    private String              sql     = null;
    private Properties          refProperties;
    private Map<String, String> context = new HashMap<String, String>();

    public XSQL(String id, Properties properties) {
        this.id = id;
        this.refProperties = properties;
        this.rawSql = this.refProperties.getProperty(this.id);
    }

    public static ResourceSQL res() {
        return resource();
    }

    public static ResourceSQL res(String resource) {
        return resource(resource);
    }

    public static ResourceSQL resource() {
        return new ResourceSQL("sql.xml");
    }

    public static ResourceSQL resource(String resource) {
        return new ResourceSQL(resource);
    }

    public static XSQL sql(String id) {
        return res().sql(id);
    }

    public XSQL var(String rep, int quots) {
        return variable(rep, quots);
    }

    public XSQL var(String rep, String val) {
        return variable(rep, val);
    }

    public XSQL variable(String rep, int quots) {
        return variable(rep, StringUtil.repeat("?", ", ", quots));
    }

    public XSQL variable(String rep, String val) {
        if (sql != null) {
            throw new IllegalStateException("Arealdy generated sql, cannot set context variables.");
        }
        context.put(rep, val);
        return this;
    }

    public String sql() {
        return generateSql();
    }

    public String generateSql() {
        if (this.sql != null) {
            return this.sql;
        }
        String _sql = this.rawSql;
        _sql = resolveSqlToken(_sql, "@@", new TokenResolver() {

            public String resolve(String token) {
                return XSQL.this.refProperties.getProperty(token);
            }
        });
        _sql = resolveSqlToken(_sql, "##", new TokenResolver() {

            public String resolve(String token) {
                return XSQL.this.context.get(token);
            }
        });
        this.sql = _sql;
        return this.sql;
    }

    protected static String resolveSqlToken(String text, final String mark, final TokenResolver tokenResolver) {
        if ((text != null) && (text.contains(mark))) {
            StringBuilder sb = new StringBuilder(text.length() + 100);
            String[] sqlSeqs = Pattern.compile(mark, Pattern.LITERAL).split(" " + text + " ", 0);
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
        return generateSql();
    }
}
