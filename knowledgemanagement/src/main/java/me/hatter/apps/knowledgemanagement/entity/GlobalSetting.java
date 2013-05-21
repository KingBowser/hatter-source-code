package me.hatter.apps.knowledgemanagement.entity;

import me.hatter.tools.resourceproxy.dbutils.annotation.Field;
import me.hatter.tools.resourceproxy.dbutils.annotation.Table;

@Table(name = "global_setting", defaultAllFields = true)
public class GlobalSetting {

    @Field(pk = true)
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
