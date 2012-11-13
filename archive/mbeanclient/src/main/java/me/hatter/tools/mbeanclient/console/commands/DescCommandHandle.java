package me.hatter.tools.mbeanclient.console.commands;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.ObjectName;

import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.mbeanclient.MBeanClientUtil;
import me.hatter.tools.mbeanclient.console.CommandLine.CommandHandle;
import me.hatter.tools.mbeanclient.console.CommandLine.CommandResult;
import me.hatter.tools.mbeanclient.console.CommandLine.ParsedCommand;

public class DescCommandHandle implements CommandHandle {

    public CommandResult handle(ParsedCommand cmd) {
        if (MBeanClientUtil.idObjectNameMap.isEmpty()) {
            System.out.println("Use \"show mbeans\" first.");
            return CommandResult.SUCCESS;
        }
        int ind;
        try {
            ind = Integer.parseInt(cmd.getSubCommand());
        } catch (Exception e) {
            System.out.println("Parse index failed.");
            return CommandResult.ERROR;
        }
        ObjectName objectName = MBeanClientUtil.idObjectNameMap.get(ind);
        if (objectName == null) {
            System.out.println("Cannot find object name.");
            return CommandResult.ERROR;
        }
        MBeanInfo info;
        try {
            info = MBeanClientUtil.connection.getMBeanInfo(objectName);
        } catch (Exception e) {
            LogUtil.error("Run command failed.", e);
            return CommandResult.ERROR;
        }
        System.out.println("[" + (ind < 10 ? " " : "") + ind + "]" + info.getClassName());
        System.out.println(">>>> description");
        System.out.println("    " + info.getDescription());
        System.out.println(">>>> descriptor");
        System.out.println("    " + info.getDescriptor());
        if ((info.getAttributes() != null) && (info.getAttributes().length > 0)) {
            System.out.println(">>>> attributes");
            for (MBeanAttributeInfo attribute : info.getAttributes()) {
                System.out.println("    " + attribute);
            }
        }

        return CommandResult.SUCCESS;
    }
}
