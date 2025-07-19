package com.mintscan.api.core.error;

import com.mintscan.common.ErrorCodes;
import com.mintscan.common.Messages;
import com.mintscan.common.Constants;

/**
 * Translates error codes and HTTP status codes to user-friendly messages.
 */
public class ErrorMessageTranslator {
    
    /**
     * Translates an error code to a user-friendly message.
     *
     * @param errorCode the error code
     * @return user-friendly error message
     */
    public String translateErrorCode(ErrorCodes errorCode) {
        switch (errorCode) {
            case INVALID_CREDENTIALS:
                return Messages.INVALID_CREDENTIALS;
            case TOKEN_EXPIRED:
            case TOKEN_INVALID:
                return Messages.TOKEN_INVALID_OR_EXPIRED;
            case DOCUMENT_NOT_FOUND:
                return Messages.DOCUMENT_NOT_FOUND_API;
            case DOCUMENT_NOT_SAME_STATION:
                return Messages.DOCUMENT_NOT_SAME_STATION;
            case DOCUMENT_NOT_SAME_CUSTOMER:
                return Messages.DOCUMENT_NOT_SAME_CUSTOMER;
            case DOCUMENT_NOT_PROCESSED:
                return Messages.DOCUMENT_NOT_PROCESSED;
            case INTERNAL_SERVER_ERROR:
                return Messages.SERVER_ERROR;
            case SERVICE_UNAVAILABLE:
                return Messages.SERVICE_UNAVAILABLE;
            default:
                return Messages.PROCESSING_ERROR;
        }
    }
    
    /**
     * Translates an HTTP status code to a user-friendly message.
     *
     * @param statusCode the HTTP status code
     * @param errorCode optional error code for more specific messages
     * @return user-friendly error message
     */
    public String translateHttpStatus(int statusCode, String errorCode) {
        ErrorCodes code = ErrorCodes.fromCode(errorCode);
        
        switch (statusCode) {
            case Constants.HTTP_UNAUTHORIZED:
                if (code == ErrorCodes.INVALID_CREDENTIALS) {
                    return Messages.INVALID_CREDENTIALS;
                }
                return Messages.TOKEN_INVALID_OR_EXPIRED;
                
            case Constants.HTTP_FORBIDDEN:
                return Messages.ACCESS_DENIED;
                
            case Constants.HTTP_NOT_FOUND:
                return Messages.SERVICE_UNAVAILABLE;
                
            case Constants.HTTP_UNPROCESSABLE_ENTITY:
                // Use specific error code message if available
                if (code != ErrorCodes.UNKNOWN_ERROR) {
                    return translateErrorCode(code);
                }
                return Messages.INVALID_INPUT_DATA;
                
            default:
                if (statusCode >= Constants.HTTP_SERVER_ERROR) {
                    return Messages.SERVER_ERROR;
                }
                return String.format(Messages.ERROR_WITH_CODE, statusCode);
        }
    }
    
    /**
     * Translates a communication error.
     *
     * @param statusCode the HTTP status code
     * @return user-friendly error message
     */
    public String translateCommunicationError(int statusCode) {
        if (statusCode >= Constants.HTTP_SERVER_ERROR) {
            return Messages.SERVER_ERROR;
        }
        return String.format(Messages.COMMUNICATION_ERROR_WITH_CODE, statusCode);
    }
}