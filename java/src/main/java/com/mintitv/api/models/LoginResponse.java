package com.mintitv.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model for successful login.
 */
public class LoginResponse {
    
    @JsonProperty("token")
    private String token;
    
    /**
     * Default constructor for Jackson deserialization.
     */
    public LoginResponse() {
    }
    
    /**
     * Constructs a new LoginResponse.
     *
     * @param token the JWT token
     */
    public LoginResponse(String token) {
        this.token = token;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
}