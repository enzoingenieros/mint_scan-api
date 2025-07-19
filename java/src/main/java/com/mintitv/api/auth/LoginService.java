package com.mintitv.api.auth;

import com.mintitv.api.exceptions.MintApiException;
import com.mintitv.api.models.LoginRequest;
import com.mintitv.api.models.LoginResponse;
import com.mintitv.api.utils.HttpClient;

/**
 * Service for handling authentication with the MintITV API.
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
     * Authenticates with the MintITV API and obtains a JWT token.
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