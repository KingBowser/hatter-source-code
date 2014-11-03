package me.hatter.tools.jflag;

import me.hatter.tools.jflag.jflagcommand.ClassVerboseHandler;

public enum JFlagCommand {

    ClassVerbose(FlagValueType._bool, new ClassVerboseHandler());

    private FlagValueType       type;
    private JFlagCommandHandler handler;

    public FlagValueType getType() {
        return this.type;
    }

    public JFlagCommandHandler getHandler() {
        return this.handler;
    }

    private JFlagCommand(FlagValueType type, JFlagCommandHandler handler) {
        this.type = type;
        this.handler = handler;
    }
}
