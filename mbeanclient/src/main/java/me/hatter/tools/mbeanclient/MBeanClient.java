package me.hatter.tools.mbeanclient;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.mbeanclient.console.CommandLine;
import me.hatter.tools.mbeanclient.console.commands.DescCommandHandle;
import me.hatter.tools.mbeanclient.console.commands.ShowCommandHandle;

public class MBeanClient {

    public static void main(String[] args) {
        UnixArgsutil.parseGlobalArgs(args);
        if (UnixArgsutil.ARGS.args().length < 1) {
            System.out.println("java -jar mbeanclientall.jar <PID>");
            System.exit(-1);
        }
        CommandLine commandLine = new CommandLine();
        commandLine.register("show", new ShowCommandHandle());
        commandLine.register("desc", new DescCommandHandle());
        commandLine.start();
    }
}
