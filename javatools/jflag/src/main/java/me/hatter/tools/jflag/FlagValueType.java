package me.hatter.tools.jflag;

public enum FlagValueType {
    _ccstr, _ccstrlist, _intx, _uint64_t, _uintx, _bool, _double;

    public String getName() {
        return this.name().substring(1);
    }

    public String toString() {
        return this.getName();
    }
}
