package me.hatter.tools.iapp.main;

import me.hatter.tools.commons.jvm.HotSpotAttachTool;
import me.hatter.tools.commons.jvm.HotSpotVMUtil;
import me.hatter.tools.commons.log.LogUtil;

public class Main {

    @SuppressWarnings("restriction")
    public static void main(String[] args) {
        if ((args == null) || (args.length != 2)) {
            System.out.println("args[0] <pid> args[1] <jar url>");
            System.exit(0);
        }

        HotSpotVMUtil.autoAddToolsJarDependency();
        HotSpotAttachTool attachTool = new HotSpotAttachTool(args[0]);
        attachTool.attach();
        try {
            attachTool.getVM().loadAgent(args[1]);
        } catch (Exception e) {
            LogUtil.error("Error in load agent.", e);
        } finally {
            attachTool.detach();
        }
    }
}
