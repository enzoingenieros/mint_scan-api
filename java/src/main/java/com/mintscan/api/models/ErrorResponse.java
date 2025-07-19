package com.mintscan.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model for API errors.
 */
public class ErrorResponse {
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("detail")
    private String detail;
    
    @JsonProperty("code")
    private String code;
    
    @JsonProperty("status")
    private int status;
    
    /**
     * Default constructor for Jackson deserialization.
     */
    public ErrorResponse() {
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDetail() {
        return detail;
    }
    
    public void setDetail(String detail) {
        this.detail = detail;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    /**
     * Enum for known error codes.
     */
    public enum ErrorCode {
        NOT_AUTHORIZED,
        TOKEN_NOT_PROVIDED,
        INVALID_TOKEN,
        INVALID_SIGNATURE,
        EXPIRED_TOKEN,
        INVALID_CREDENTIALS,
        INVALID_FORMAT,
        INTERNAL_SERVER_ERROR,
        DOCUMENT_NOT_FOUND,
        DOCUMENT_NOT_SAME_STATION,
        DOCUMENT_NOT_SAME_CUSTOMER,
        DOCUMENT_NOT_PROCESSED
    }
}