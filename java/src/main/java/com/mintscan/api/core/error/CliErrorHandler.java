package com.mintscan.api.core.error;

import com.mintscan.common.Constants;
import com.mintscan.common.Messages;

/**
 * Error handling strategy for CLI applications.
 * Prints errors to stderr and exits with appropriate codes.
 */
public class CliErrorHandler implements ErrorHandlingStrategy {
    
    private final boolean verbose;
    
    public CliErrorHandler() {
        this(false);
    }
    
    public CliErrorHandler(boolean verbose) {
        this.verbose = verbose;
    }
    
    @Override
    public void handleError(String message) {
        System.err.println(Messages.ERROR_PREFIX + message);
        System.exit(Constants.EXIT_ERROR);
    }
    
    @Override
    public void handleError(String message, Throwable cause) {
        System.err.println(Messages.ERROR_PREFIX + message);
        if (verbose && cause != null) {
            System.err.println("Cause: " + cause.getMessage());
            cause.printStackTrace(System.err);
        }
        System.exit(Constants.EXIT_ERROR);
    }
    
    @Override
    public void handleValidationError(String message) {
        System.err.println(Messages.ERROR_PREFIX + message);
        System.err.println();
        System.err.println("Use --help for usage information");
        System.exit(Constants.EXIT_ERROR);
    }
    
    @Override
    public void handleAuthenticationError(String message) {
        System.err.println(Messages.ERROR_PREFIX + message);
        System.err.println();
        System.err.println(Messages.RUN_LOGIN_TO_GET_TOKEN);
        System.exit(Constants.EXIT_ERROR);
    }
    
    @Override
    public void handleWarning(String message) {
        System.err.println(Messages.WARNINGS_PREFIX + message);
    }
    
    @Override
    public boolean exitsOnError() {
        return true;
    }
}