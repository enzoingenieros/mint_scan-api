package com.mintitv.api.exceptions;

/**
 * Exception thrown when an error occurs while interacting with the MintITV API.
 */
public class MintApiException extends Exception {
    
    private final int statusCode;
    private final String errorCode;
    
    /**
     * Constructs a new MintApiException with the specified detail message.
     *
     * @param message the detail message
     */
    public MintApiException(String message) {
        super(message);
        this.statusCode = -1;
        this.errorCode = null;
    }
    
    /**
     * Constructs a new MintApiException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public MintApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = -1;
        this.errorCode = null;
    }
    
    /**
     * Constructs a new MintApiException with the specified detail message, HTTP status code, and error code.
     *
     * @param message the detail message
     * @param statusCode the HTTP status code
     * @param errorCode the API error code
     */
    public MintApiException(String message, int statusCode, String errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
    
    /**
     * Gets the HTTP status code associated with this exception.
     *
     * @return the HTTP status code, or -1 if not available
     */
    public int getStatusCode() {
        return statusCode;
    }
    
    /**
     * Gets the API error code associated with this exception.
     *
     * @return the API error code, or null if not available
     */
    public String getErrorCode() {
        return errorCode;
    }
}