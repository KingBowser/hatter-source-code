package me.hatter.tools.cook.util;

public class CookArgs {

    boolean isVersion = false;
    boolean isHelp    = false;

    public boolean isVersion() {
        return isVersion;
    }

    public void setVersion(boolean isVersion) {
        this.isVersion = isVersion;
    }

    public boolean isHelp() {
        return isHelp;
    }

    public void setHelp(boolean isHelp) {
        this.isHelp = isHelp;
    }
}
