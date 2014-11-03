package me.hatter.tools.bytecodecheck;

import java.util.Collection;

public class MethodDef {

    private String                   name;
    private String                   desc;
    private Collection<MethodInvoke> methodInvokes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Collection<MethodInvoke> getMethodInvokes() {
        return methodInvokes;
    }

    public void setMethodInvokes(Collection<MethodInvoke> methodInvokes) {
        this.methodInvokes = methodInvokes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((desc == null) ? 0 : desc.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        MethodDef other = (MethodDef) obj;
        if (desc == null) {
            if (other.desc != null) return false;
        } else if (!desc.equals(other.desc)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }
}
