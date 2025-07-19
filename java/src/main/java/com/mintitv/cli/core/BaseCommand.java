package com.mintitv.cli.core;

import com.mintitv.cli.CommandLineParser;
import com.mintitv.common.Constants;
import com.mintitv.common.Messages;
import com.mintitv.api.exceptions.MintApiException;

/**
 * Abstract base class for CLI commands.
 * Provides common functionality for all command implementations.
 */
public abstract class BaseCommand implements Command {
    
    /**
     * Validates and retrieves the authentication token.
     * Checks command line arguments first, then environment variable.
     *
     * @param parser the command line parser
     * @return the authentication token
     * @throws IllegalArgumentException if no token is found
     */
    protected String validateAndGetToken(CommandLineParser parser) {
        String token = parser.getOption("token", parser.getOption("t"));
        if (token == null) {
            token = System.getenv(Constants.ENV_TOKEN);
            if (token == null) {
                throw new IllegalArgumentException(Messages.TOKEN_REQUIRED);
            }
        }
        return token;
    }
    
    /**
     * Gets a required option from the parser.
     *
     * @param parser the command line parser
     * @param longOption the long option name
     * @param shortOption the short option name
     * @param errorMessage the error message if option is missing
     * @return the option value
     * @throws IllegalArgumentException if option is missing
     */
    protected String getRequiredOption(CommandLineParser parser, String longOption, 
                                     String shortOption, String errorMessage) {
        String value = parser.getOption(longOption, parser.getOption(shortOption));
        if (value == null) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }
    
    /**
     * Gets an optional value from parser or environment.
     *
     * @param parser the command line parser
     * @param longOption the long option name
     * @param shortOption the short option name
     * @param envVariable the environment variable name
     * @return the value or null if not found
     */
    protected String getOptionalValue(CommandLineParser parser, String longOption,
                                    String shortOption, String envVariable) {
        String value = parser.getOption(longOption, parser.getOption(shortOption));
        if (value == null && envVariable != null) {
            value = System.getenv(envVariable);
        }
        return value;
    }
    
    /**
     * Handles errors in a consistent way.
     *
     * @param e the exception to handle
     */
    protected void handleError(Exception e) {
        if (e instanceof MintApiException) {
            System.err.println(Messages.ERROR_PREFIX + e.getMessage());
        } else if (e instanceof IllegalArgumentException) {
            System.err.println(Messages.ERROR_PREFIX + e.getMessage());
            printHelp();
        } else {
            System.err.println(Messages.ERROR_PREFIX + e.getMessage());
        }
        System.exit(Constants.EXIT_ERROR);
    }
    
    /**
     * Template method for command execution.
     * Handles common error handling and validation.
     *
     * @param parser the command line parser
     * @throws Exception if execution fails
     */
    @Override
    public void execute(CommandLineParser parser) throws Exception {
        try {
            if (parser.isHelpRequested()) {
                printHelp();
                return;
            }
            
            if (!validate(parser)) {
                System.exit(Constants.EXIT_ERROR);
                return;
            }
            
            doExecute(parser);
        } catch (Exception e) {
            handleError(e);
        }
    }
    
    /**
     * Actual command execution logic to be implemented by subclasses.
     *
     * @param parser the command line parser
     * @throws Exception if execution fails
     */
    protected abstract void doExecute(CommandLineParser parser) throws Exception;
    
    /**
     * Default validation that can be overridden by subclasses.
     *
     * @param parser the command line parser
     * @return true if validation passes
     */
    @Override
    public boolean validate(CommandLineParser parser) {
        return true;
    }
    
    /**
     * Prints a formatted option description.
     *
     * @param shortOption the short option (e.g., "-t")
     * @param longOption the long option (e.g., "--token")
     * @param description the option description
     */
    protected void printOption(String shortOption, String longOption, String description) {
        if (shortOption != null && longOption != null) {
            System.out.printf("  %s, %-20s %s%n", shortOption, longOption, description);
        } else if (longOption != null) {
            System.out.printf("      %-20s %s%n", longOption, description);
        }
    }
    
    /**
     * Prints verbose information if verbose flag is set.
     *
     * @param parser the command line parser
     * @param message the message to print
     * @param args format arguments
     */
    protected void printVerbose(CommandLineParser parser, String message, Object... args) {
        if (parser.hasFlag("verbose") || parser.hasFlag("v")) {
            System.out.printf(message + "%n", args);
        }
    }
}