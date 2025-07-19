package com.mintitv.api.core.error;

/**
 * Strategy interface for handling errors in different contexts.
 * Allows different error handling behaviors for CLI vs API library usage.
 */
public interface ErrorHandlingStrategy {
    
    /**
     * Handles an error with the given message.
     *
     * @param message the error message
     * @throws RuntimeException implementations may throw exceptions
     */
    void handleError(String message) throws RuntimeException;
    
    /**
     * Handles an error with a throwable cause.
     *
     * @param message the error message
     * @param cause the underlying cause
     * @throws RuntimeException implementations may throw exceptions
     */
    void handleError(String message, Throwable cause) throws RuntimeException;
    
    /**
     * Handles a validation error.
     *
     * @param message the validation error message
     * @throws RuntimeException implementations may throw exceptions
     */
    void handleValidationError(String message) throws RuntimeException;
    
    /**
     * Handles an authentication error.
     *
     * @param message the authentication error message
     * @throws RuntimeException implementations may throw exceptions
     */
    void handleAuthenticationError(String message) throws RuntimeException;
    
    /**
     * Handles a warning that doesn't stop execution.
     *
     * @param message the warning message
     */
    void handleWarning(String message);
    
    /**
     * Determines if the strategy should exit the application on error.
     *
     * @return true if the strategy exits on error
     */
    boolean exitsOnError();
}