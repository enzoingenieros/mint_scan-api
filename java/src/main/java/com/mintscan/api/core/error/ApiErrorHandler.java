package com.mintscan.api.core.error;

import com.mintscan.api.exceptions.MintApiRuntimeException;

/**
 * Error handling strategy for API library usage.
 * Throws exceptions instead of exiting the application.
 */
public class ApiErrorHandler implements ErrorHandlingStrategy {
    
    @Override
    public void handleError(String message) {
        throw new MintApiRuntimeException(message);
    }
    
    @Override
    public void handleError(String message, Throwable cause) {
        throw new MintApiRuntimeException(message, cause);
    }
    
    @Override
    public void handleValidationError(String message) {
        throw new IllegalArgumentException(message);
    }
    
    @Override
    public void handleAuthenticationError(String message) {
        throw new MintApiRuntimeException(message, 401, "AUTHENTICATION_ERROR");
    }
    
    @Override
    public void handleWarning(String message) {
        // In API mode, warnings are logged but don't throw
        System.err.println("WARNING: " + message);
    }
    
    @Override
    public boolean exitsOnError() {
        return false;
    }
}