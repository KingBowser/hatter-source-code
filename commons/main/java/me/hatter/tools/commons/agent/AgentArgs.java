package me.hatter.tools.commons.agent;

import java.util.Map;

public class AgentArgs {

    private String              oriArgs;
    private Map<String, String> argsMap;

    public AgentArgs(String args) {
        this.oriArgs = args;
        this.argsMap = AgentUtil.parseAgentArgsMap(args);
    }

    public String getOriArgs() {
        return oriArgs;
    }

    public String get(String key) {
        return argsMap.get(key);
    }
}
