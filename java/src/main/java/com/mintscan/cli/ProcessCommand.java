package com.mintscan.cli;

/**
 * Process command implementation.
 * @deprecated Use {@link com.mintscan.cli.commands.ProcessCommand} instead.
 */
@Deprecated
public class ProcessCommand {
    
    public static void execute(String[] args) throws Exception {
        // Delegate to the refactored command
        CommandLineParser parser = new CommandLineParser(args);
        com.mintscan.cli.commands.ProcessCommand command = new com.mintscan.cli.commands.ProcessCommand();
        command.execute(parser);
    }
    
    public static void printHelp() {
        // Delegate to the refactored command
        com.mintscan.cli.commands.ProcessCommand command = new com.mintscan.cli.commands.ProcessCommand();
        command.printHelp();
    }
}