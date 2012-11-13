package me.hatter.tools.mbeanclient.console.commands;

import java.util.Set;

import javax.management.ObjectName;

import me.hatter.tools.mbeanclient.MBeanClientUtil;
import me.hatter.tools.mbeanclient.console.CommandLine.CommandHandle;
import me.hatter.tools.mbeanclient.console.CommandLine.CommandResult;
import me.hatter.tools.mbeanclient.console.CommandLine.ParsedCommand;

public class ShowCommandHandle implements CommandHandle {

    public CommandResult handle(ParsedCommand cmd) {
        if (cmd.getSubCommand().trim().equals("mbeans")) {
            Set<ObjectName> mbeanNames = MBeanClientUtil.queryMBeanNames();
            MBeanClientUtil.idObjectNameMap.clear();
            int i = 0;
            for (ObjectName mbeanName : mbeanNames) {
                System.out.println("[" + (i < 10 ? " " : "") + i + "] " + mbeanName);
                MBeanClientUtil.idObjectNameMap.put(i, mbeanName);
                i++;
            }
            return CommandResult.SUCCESS;
        }
        return CommandResult.PARSE_FAILED;
    }
}
