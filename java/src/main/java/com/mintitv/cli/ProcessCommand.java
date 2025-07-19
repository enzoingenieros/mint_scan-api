package com.mintitv.cli;

/**
 * Process command implementation.
 * @deprecated Use {@link com.mintitv.cli.commands.ProcessCommand} instead.
 */
@Deprecated
public class ProcessCommand {
    
    public static void execute(String[] args) throws Exception {
        // Delegate to the refactored command
        CommandLineParser parser = new CommandLineParser(args);
        com.mintitv.cli.commands.ProcessCommand command = new com.mintitv.cli.commands.ProcessCommand();
        command.execute(parser);
    }
    
    public static void printHelp() {
        // Delegate to the refactored command
        com.mintitv.cli.commands.ProcessCommand command = new com.mintitv.cli.commands.ProcessCommand();
        command.printHelp();
    }
}