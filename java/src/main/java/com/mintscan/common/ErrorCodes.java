package com.mintscan.common;

/**
 * API error codes enumeration.
 * Maps API error codes to meaningful constants.
 */
public enum ErrorCodes {
    
    // Authentication Errors
    INVALID_CREDENTIALS("INVALID_CREDENTIALS"),
    TOKEN_EXPIRED("TOKEN_EXPIRED"),
    TOKEN_INVALID("TOKEN_INVALID"),
    
    // Document Errors
    DOCUMENT_NOT_FOUND("DOCUMENT_NOT_FOUND"),
    DOCUMENT_NOT_SAME_STATION("DOCUMENT_NOT_SAME_STATION"),
    DOCUMENT_NOT_SAME_CUSTOMER("DOCUMENT_NOT_SAME_CUSTOMER"),
    DOCUMENT_NOT_PROCESSED("DOCUMENT_NOT_PROCESSED"),
    
    // Validation Errors
    INVALID_DOCUMENT_TYPE("INVALID_DOCUMENT_TYPE"),
    INVALID_VEHICLE_CATEGORY("INVALID_VEHICLE_CATEGORY"),
    INVALID_IMAGE_FORMAT("INVALID_IMAGE_FORMAT"),
    MISSING_REQUIRED_FIELD("MISSING_REQUIRED_FIELD"),
    
    // Processing Errors
    PROCESSING_FAILED("PROCESSING_FAILED"),
    QUEUE_FULL("QUEUE_FULL"),
    
    // Generic Errors
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
    SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE"),
    UNKNOWN_ERROR("UNKNOWN_ERROR");
    
    private final String code;
    
    ErrorCodes(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
    
    /**
     * Gets ErrorCode from string value.
     *
     * @param code the error code string
     * @return the matching ErrorCode, or UNKNOWN_ERROR if not found
     */
    public static ErrorCodes fromCode(String code) {
        if (code == null) {
            return UNKNOWN_ERROR;
        }
        
        for (ErrorCodes errorCode : values()) {
            if (errorCode.code.equals(code)) {
                return errorCode;
            }
        }
        return UNKNOWN_ERROR;
    }
    
    /**
     * Checks if this error code represents an authentication error.
     *
     * @return true if this is an authentication-related error
     */
    public boolean isAuthenticationError() {
        return this == INVALID_CREDENTIALS || 
               this == TOKEN_EXPIRED || 
               this == TOKEN_INVALID;
    }
    
    /**
     * Checks if this error code represents a document access error.
     *
     * @return true if this is a document access-related error
     */
    public boolean isDocumentAccessError() {
        return this == DOCUMENT_NOT_FOUND ||
               this == DOCUMENT_NOT_SAME_STATION ||
               this == DOCUMENT_NOT_SAME_CUSTOMER ||
               this == DOCUMENT_NOT_PROCESSED;
    }
    
    /**
     * Checks if this error code represents a validation error.
     *
     * @return true if this is a validation-related error
     */
    public boolean isValidationError() {
        return this == INVALID_DOCUMENT_TYPE ||
               this == INVALID_VEHICLE_CATEGORY ||
               this == INVALID_IMAGE_FORMAT ||
               this == MISSING_REQUIRED_FIELD;
    }
}