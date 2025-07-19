package com.mintitv.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model for process pool endpoint.
 */
public class ProcessPoolResponse {
    
    @JsonProperty("success")
    private boolean success;
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("message")
    private String message;
    
    /**
     * Default constructor for Jackson deserialization.
     */
    public ProcessPoolResponse() {
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}