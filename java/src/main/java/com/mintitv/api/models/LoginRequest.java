package com.mintitv.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request model for login endpoint.
 */
public class LoginRequest {
    
    @JsonProperty("username")
    private final String username;
    
    @JsonProperty("password")
    private final String password;
    
    /**
     * Constructs a new LoginRequest.
     *
     * @param username the username
     * @param password the password
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
}