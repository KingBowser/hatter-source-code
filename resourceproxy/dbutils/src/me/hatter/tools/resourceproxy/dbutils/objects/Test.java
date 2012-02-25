package me.hatter.tools.resourceproxy.dbutils.objects;

import me.hatter.tools.resourceproxy.dbutils.annotation.Field;
import me.hatter.tools.resourceproxy.dbutils.annotation.Table;
import me.hatter.tools.resourceproxy.dbutils.util.DBUtil;

@Table(defaultAllFields = true)
public class Test {

    public static void main(String[] a) {
        System.out.println(DBUtil.generateCreateSQL(Test.class));
    }

    @Field(pk = true)
    private Integer id;
    private String  name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
