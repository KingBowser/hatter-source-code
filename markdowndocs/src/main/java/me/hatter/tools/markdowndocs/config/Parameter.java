package me.hatter.tools.markdowndocs.config;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class Parameter {

    public static void main(String[] args) {
        Menu m1 = new Menu();
        m1.setPath("/");
        m1.setTitle("Index");
        Menu m2 = new Menu();
        m2.setPath("/1");
        m2.setTitle("Index1");
        Menu ma = new Menu();
        ma.setPath("/a");
        ma.setTitle("IndexA");
        Menu gm = new Menu();
        Menu mg1 = new Menu();
        mg1.setPath("/g1");
        mg1.setTitle("IndexG1");
        gm.setPath("/g");
        gm.setTitle("IndexG");
        gm.getList().add(mg1);

        Parameter p = new Parameter();
        List<Menu> lefts = new ArrayList<Menu>();
        List<Menu> rights = new ArrayList<Menu>();
        lefts.add(m1);
        lefts.add(m2);
        lefts.add(gm);
        rights.add(ma);

        p.setLefts(lefts);
        p.setRights(rights);

        System.out.println(JSONObject.toJSONString(p));
        System.out.println(JSONObject.parseObject(JSONObject.toJSONString(p), Parameter.class));
        System.out.println(JSONObject.toJSONString(JSONObject.parseObject(JSONObject.toJSONString(p), Parameter.class)));
    }

    private String     template;

    private List<Menu> lefts;
    private List<Menu> rights;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<Menu> getLefts() {
        return lefts;
    }

    public void setLefts(List<Menu> lefts) {
        this.lefts = lefts;
    }

    public List<Menu> getRights() {
        return rights;
    }

    public void setRights(List<Menu> rights) {
        this.rights = rights;
    }
}
