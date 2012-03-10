package me.hatter.tools.resourceproxy.httpobjects.objects;

import me.hatter.tools.resourceproxy.dbutils.annotation.Field;
import me.hatter.tools.resourceproxy.dbutils.annotation.Table;
import me.hatter.tools.resourceproxy.dbutils.annotation.UpdateIgnore;
import me.hatter.tools.resourceproxy.dbutils.util.DBUtil;

@Table
public class HostConfig {

    public static void main(String[] a) {
        System.out.println(DBUtil.generateCreateSQL(HostConfig.class));
    }

    @Field
    @UpdateIgnore
    private Integer id;
    @Field(pk = true)
    private String  domain;
    @Field(pk = true)
    private String  accessAddress;
    @Field
    private String  targetIp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAccessAddress() {
        return accessAddress;
    }

    public void setAccessAddress(String accessAddress) {
        this.accessAddress = accessAddress;
    }

    public String getTargetIp() {
        return targetIp;
    }

    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }
}
