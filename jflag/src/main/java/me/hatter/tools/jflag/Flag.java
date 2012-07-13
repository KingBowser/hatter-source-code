package me.hatter.tools.jflag;

public class Flag {

    private String          name;
    private FlagRuntimeType runtime;
    private FlagValueType   type;

    public Flag(String name, FlagRuntimeType runtime, FlagValueType type) {
        this.name = name;
        this.runtime = runtime;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public FlagRuntimeType getRuntime() {
        return runtime;
    }

    public FlagValueType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Flag [name=" + name + ", runtime=" + runtime + ", type=" + type + "]";
    }
}
