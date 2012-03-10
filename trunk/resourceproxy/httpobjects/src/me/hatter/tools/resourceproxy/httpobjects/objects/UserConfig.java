package me.hatter.tools.resourceproxy.httpobjects.objects;

import me.hatter.tools.resourceproxy.dbutils.annotation.Field;
import me.hatter.tools.resourceproxy.dbutils.annotation.Table;
import me.hatter.tools.resourceproxy.dbutils.util.DBUtil;

@Table
public class UserConfig {

    public static void main(String[] a) {
        System.out.println(DBUtil.generateCreateSQL(UserConfig.class));
    }

    @Field(pk = true)
    private String accessAddress;
    @Field
    private String userAgent;

    public String getAccessAddress() {
        return accessAddress;
    }

    public void setAccessAddress(String accessAddress) {
        this.accessAddress = accessAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
