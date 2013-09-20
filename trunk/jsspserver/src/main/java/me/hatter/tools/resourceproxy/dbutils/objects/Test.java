package me.hatter.tools.resourceproxy.dbutils.objects;

import me.hatter.tools.resourceproxy.dbutils.annotation.Field;
import me.hatter.tools.resourceproxy.dbutils.annotation.Table;
import me.hatter.tools.resourceproxy.dbutils.config.PropertyConfig;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;

// this is a test class
@Table(defaultAllFields = true)
public class Test {

    public static void main(String[] a) {
        DataAccessObject dataAccessObject = new DataAccessObject(PropertyConfig.createDefaultPropertyConfig());
        Test t = new Test();
        for (int i = 101; i <= 200; i++) {
            t.setId(i);
            t.setName("hatter" + "@" + i);
            dataAccessObject.insertObject(t);
        }
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
