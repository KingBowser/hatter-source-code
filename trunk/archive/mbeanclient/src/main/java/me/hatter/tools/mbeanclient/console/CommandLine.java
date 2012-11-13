package me.hatter.tools.mbeanclient.console;

import java.util.HashMap;
import java.util.Map;

public class CommandLine {

    public static interface CommandHandle {

        CommandResult handle(ParsedCommand cmd);
    }

    public static enum CommandResult {
        SUCCESS,

        ERROR,

        PARSE_FAILED;
    }

    public static class ParsedCommand {

        private String raw;
        private String prefix;
        private String subCommand;

        public ParsedCommand(String raw, String prefix, String subCommand) {
            this.raw = raw;
            this.prefix = prefix;
            this.subCommand = subCommand;
        }

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getSubCommand() {
            return subCommand;
        }

        public void setSubCommand(String subCommand) {
            this.subCommand = subCommand;
        }
    }

    public CommandLine() {
        register("exit", new CommandHandle() {

            public CommandResult handle(ParsedCommand cmd) {
                System.out.println("Bye!");
                System.exit(0);
                return CommandResult.SUCCESS;
            }
        });
        register("help", new CommandHandle() {

            public CommandResult handle(ParsedCommand cmd) {
                System.out.println("Command list:");
                for (String c : commandHandleMap.keySet()) {
                    System.out.println("    " + c);
                }
                return CommandResult.SUCCESS;
            }
        });
    }

    private Map<String, CommandHandle> commandHandleMap = new HashMap<String, CommandLine.CommandHandle>();

    public void register(String prefix, CommandHandle handle) {
        commandHandleMap.put(prefix, handle);
    }

    public void start() {
        while (true) {
            System.out.print("java$ ");
            String cmd = System.console().readLine();
            cmd = cmd.trim();
            if (!cmd.isEmpty()) {
                ParsedCommand command = parseCommand(cmd);
                CommandHandle handle = commandHandleMap.get(command.getPrefix());
                if (handle == null) {
                    System.out.println("Unknow command!");
                    continue;
                }
                CommandResult result = handle.handle(command);
                if (result == CommandResult.PARSE_FAILED) {
                    System.out.println("Unknow subcommand!");
                }
            }
        }
    }

    public ParsedCommand parseCommand(String cmd) {
        int firstSpace = cmd.indexOf(' ');
        if (firstSpace < 0) {
            return new ParsedCommand(cmd, cmd, null);
        }
        String prefix = cmd.substring(0, firstSpace).toLowerCase().trim();
        String subCommand = cmd.substring(firstSpace + 1);
        return new ParsedCommand(cmd, prefix, subCommand);
    }
}
