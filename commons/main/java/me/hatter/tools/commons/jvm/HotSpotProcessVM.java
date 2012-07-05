package me.hatter.tools.commons.jvm;

public class HotSpotProcessVM {

    private int     pid;
    private String  fullClassName;
    private boolean isAttachAble;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    public boolean isAttachAble() {
        return isAttachAble;
    }

    public void setAttachAble(boolean isAttachAble) {
        this.isAttachAble = isAttachAble;
    }
}
