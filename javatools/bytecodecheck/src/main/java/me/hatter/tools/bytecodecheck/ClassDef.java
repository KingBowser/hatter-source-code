package me.hatter.tools.bytecodecheck;

import java.util.Collection;

public class ClassDef {

    private String                refJar;
    private String                name;
    private Collection<MethodDef> methodDefs;

    public String getRefJar() {
        return refJar;
    }

    public void setRefJar(String refJar) {
        this.refJar = refJar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<MethodDef> getMethodDefs() {
        return methodDefs;
    }

    public void setMethodDefs(Collection<MethodDef> methodDefs) {
        this.methodDefs = methodDefs;
    }
}
