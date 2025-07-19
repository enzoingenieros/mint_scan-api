package com.mintitv.api.utils;

import com.mintitv.api.exceptions.MintApiException;
import com.mintitv.api.core.http.ApiClient;
import com.mintitv.api.core.http.HttpClientWrapper;

import java.util.Map;

/**
 * HTTP client wrapper for MintITV API communication.
 * @deprecated Use {@link ApiClient} directly for new code.
 */
@Deprecated
public class HttpClient {
    
    private final ApiClient apiClient;
    
    /**
     * Constructs a new HttpClient.
     */
    public HttpClient() {
        this.apiClient = new ApiClient();
    }
    
    /**
     * Performs a GET request to the specified endpoint.
     *
     * @param endpoint the API endpoint
     * @param headers additional headers to include
     * @param responseType the expected response type
     * @return the deserialized response
     * @throws MintApiException if the request fails
     */
    public <T> T get(String endpoint, Map<String, String> headers, Class<T> responseType) 
            throws MintApiException {
        return apiClient.get(endpoint, headers, responseType);
    }
    
    /**
     * Performs a POST request to the specified endpoint.
     *
     * @param endpoint the API endpoint
     * @param headers additional headers to include
     * @param body the request body
     * @param responseType the expected response type
     * @return the deserialized response
     * @throws MintApiException if the request fails
     */
    public <T> T post(String endpoint, Map<String, String> headers, Object body, Class<T> responseType) 
            throws MintApiException {
        return apiClient.post(endpoint, headers, body, responseType);
    }
    
    /**
     * Creates authorization header with Bearer token.
     *
     * @param token the JWT token
     * @return map containing the authorization header
     */
    public static Map<String, String> createBearerHeader(String token) {
        return HttpClientWrapper.createBearerHeader(token);
    }
}