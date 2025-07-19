package com.mintscan.api.auth;

import com.mintscan.api.exceptions.MintApiException;
import com.mintscan.api.models.LoginRequest;
import com.mintscan.api.models.LoginResponse;
import com.mintscan.api.utils.HttpClient;

/**
 * Service for handling authentication with the MintScan API.
 */
public class LoginService {
    
    private final HttpClient httpClient;
    
    /**
     * Constructs a new LoginService.
     */
    public LoginService() {
        this.httpClient = new HttpClient();
    }
    
    /**
     * Authenticates with the MintScan API and obtains a JWT token.
     *
     * @param username the username
     * @param password the password
     * @return the JWT token
     * @throws MintApiException if authentication fails
     */
    public String login(String username, String password) throws MintApiException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es requerido");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es requerida");
        }
        
        LoginRequest request = new LoginRequest(username, password);
        LoginResponse response = httpClient.post("/login", null, request, LoginResponse.class);
        
        if (response.getToken() == null || response.getToken().isEmpty()) {
            throw new MintApiException("No hay token en la respuesta");
        }
        
        return response.getToken();
    }
}