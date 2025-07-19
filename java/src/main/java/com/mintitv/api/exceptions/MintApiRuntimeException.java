package com.mintitv.api.exceptions;

/**
 * Runtime exception thrown when an error occurs while interacting with the MintITV API.
 * This is an unchecked version of MintApiException for use in contexts where checked exceptions are not appropriate.
 */
public class MintApiRuntimeException extends RuntimeException {
    
    private final int statusCode;
    private final String errorCode;
    
    /**
     * Constructs a new MintApiRuntimeException with the specified detail message.
     *
     * @param message the detail message
     */
    public MintApiRuntimeException(String message) {
        super(message);
        this.statusCode = -1;
        this.errorCode = null;
    }
    
    /**
     * Constructs a new MintApiRuntimeException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public MintApiRuntimeException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = -1;
        this.errorCode = null;
    }
    
    /**
     * Constructs a new MintApiRuntimeException with the specified detail message, HTTP status code, and error code.
     *
     * @param message the detail message
     * @param statusCode the HTTP status code
     * @param errorCode the API error code
     */
    public MintApiRuntimeException(String message, int statusCode, String errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
    
    /**
     * Creates a MintApiRuntimeException from a MintApiException.
     *
     * @param e the MintApiException to convert
     * @return a new MintApiRuntimeException
     */
    public static MintApiRuntimeException from(MintApiException e) {
        return new MintApiRuntimeException(e.getMessage(), e.getStatusCode(), e.getErrorCode());
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