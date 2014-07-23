package me.hatter.tools.commons.bytecode;

import java.util.List;

import me.hatter.tools.commons.collection.CollectionUtil;
import me.hatter.tools.commons.function.Function;
import me.hatter.tools.commons.string.StringUtil;

public class SimpleClassMethod {

    private String       ret;
    private String       cls;
    private String       met;
    private List<String> par;

    public SimpleClassMethod(String ret, String cls, String met, List<String> par) {
        this.ret = ret;
        this.cls = cls;
        this.met = met;
        this.par = par;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getMet() {
        return met;
    }

    public void setMet(String met) {
        this.met = met;
    }

    public List<String> getPar() {
        return par;
    }

    public void setPar(List<String> par) {
        this.par = par;
    }

    public String toString() {
        return ret + " " + cls + "#" + met + "(" + StringUtil.join(par, ", ") + ")";
    }

    public String toString2() {
        return ret + " " + ByteCodeUtil.getSimpleClassName(cls) + "#" + met + "(" + StringUtil.join(par, ", ") + ")";
    }

    public String toString3() {
        return ByteCodeUtil.getSimpleClassName(ret) + " " + ByteCodeUtil.getSimpleClassName(cls) + "#" + met + "("
               + StringUtil.join(CollectionUtil.it(par).map(new Function<String, String>() {

                   @Override
                   public String apply(String obj) {
                       return ByteCodeUtil.getSimpleClassName(obj);
                   }
               }).asList(), ", ") + ")";
    }

    public String toString4() {
        return met + "(" + StringUtil.join(CollectionUtil.it(par).map(new Function<String, String>() {

            @Override
            public String apply(String obj) {
                return ByteCodeUtil.getSimpleClassName(obj);
            }
        }).asList(), ", ") + ")";
    }
}
