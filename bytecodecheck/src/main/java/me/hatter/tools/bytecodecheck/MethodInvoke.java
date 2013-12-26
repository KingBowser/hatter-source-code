package me.hatter.tools.bytecodecheck;

public class MethodInvoke {

    private String clazz;
    private String method;
    private String desc;

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
        result = prime * result + ((desc == null) ? 0 : desc.hashCode());
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        MethodInvoke other = (MethodInvoke) obj;
        if (clazz == null) {
            if (other.clazz != null) return false;
        } else if (!clazz.equals(other.clazz)) return false;
        if (desc == null) {
            if (other.desc != null) return false;
        } else if (!desc.equals(other.desc)) return false;
        if (method == null) {
            if (other.method != null) return false;
        } else if (!method.equals(other.method)) return false;
        return true;
    }
}
