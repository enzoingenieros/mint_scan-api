package com.mintitv.cli.core;

import com.mintitv.cli.CommandLineParser;

/**
 * Interface for all CLI commands.
 * Defines the contract that all command implementations must follow.
 */
public interface Command {
    
    /**
     * Executes the command with the given parsed arguments.
     *
     * @param parser the command line parser containing parsed arguments
     * @throws Exception if command execution fails
     */
    void execute(CommandLineParser parser) throws Exception;
    
    /**
     * Gets the command name.
     *
     * @return the command name as it appears in CLI
     */
    String getName();
    
    /**
     * Gets a brief description of what the command does.
     *
     * @return command description
     */
    String getDescription();
    
    /**
     * Prints help information for this command.
     */
    void printHelp();
    
    /**
     * Validates if the command can be executed with the given arguments.
     *
     * @param parser the command line parser containing parsed arguments
     * @return true if the command can be executed, false otherwise
     */
    boolean validate(CommandLineParser parser);
}