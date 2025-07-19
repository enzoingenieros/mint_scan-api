package com.mintscan.api.core.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintscan.api.exceptions.MintApiException;
import com.mintscan.api.models.ErrorResponse;
import com.mintscan.api.core.error.ErrorMessageTranslator;
import com.mintscan.common.Constants;
import com.mintscan.common.Messages;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

/**
 * High-level API client that handles serialization and error handling.
 * Uses HttpClientWrapper for actual HTTP operations.
 */
public class ApiClient {
    
    private final HttpClientWrapper httpClient;
    private final ObjectMapper objectMapper;
    private final ErrorMessageTranslator errorTranslator;
    
    public ApiClient() {
        this.httpClient = new HttpClientWrapper();
        this.objectMapper = new ObjectMapper();
        this.errorTranslator = new ErrorMessageTranslator();
    }
    
    public ApiClient(HttpClientWrapper httpClient, ObjectMapper objectMapper, 
                    ErrorMessageTranslator errorTranslator) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.errorTranslator = errorTranslator;
    }
    
    /**
     * Performs a GET request and deserializes the response.
     *
     * @param endpoint the API endpoint
     * @param headers additional headers
     * @param responseType the expected response type
     * @return the deserialized response
     * @throws MintApiException if the request fails
     */
    public <T> T get(String endpoint, Map<String, String> headers, Class<T> responseType) 
            throws MintApiException {
        try {
            HttpResponse<String> response = httpClient.get(endpoint, headers);
            return handleResponse(response, responseType);
        } catch (IOException | InterruptedException e) {
            throw new MintApiException(Messages.CONNECTION_ERROR, e);
        }
    }
    
    /**
     * Performs a POST request and deserializes the response.
     *
     * @param endpoint the API endpoint
     * @param headers additional headers
     * @param body the request body
     * @param responseType the expected response type
     * @return the deserialized response
     * @throws MintApiException if the request fails
     */
    public <T> T post(String endpoint, Map<String, String> headers, Object body, Class<T> responseType) 
            throws MintApiException {
        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            HttpResponse<String> response = httpClient.post(endpoint, headers, jsonBody);
            return handleResponse(response, responseType);
        } catch (IOException | InterruptedException e) {
            throw new MintApiException(Messages.CONNECTION_ERROR, e);
        }
    }
    
    /**
     * Handles HTTP response and converts to expected type.
     *
     * @param response the HTTP response
     * @param responseType the expected response type
     * @return the deserialized response
     * @throws MintApiException if the response indicates an error
     */
    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseType) 
            throws MintApiException {
        if (isSuccessful(response.statusCode())) {
            try {
                return objectMapper.readValue(response.body(), responseType);
            } catch (IOException e) {
                throw new MintApiException(Messages.PROCESSING_ERROR, e);
            }
        } else {
            handleErrorResponse(response);
            return null; // Never reached
        }
    }
    
    /**
     * Checks if HTTP status code indicates success.
     *
     * @param statusCode the HTTP status code
     * @return true if successful
     */
    private boolean isSuccessful(int statusCode) {
        return statusCode >= Constants.HTTP_OK && statusCode < 300;
    }
    
    /**
     * Handles error responses from the API.
     *
     * @param response the error response
     * @throws MintApiException always thrown with appropriate error message
     */
    private void handleErrorResponse(HttpResponse<String> response) throws MintApiException {
        int statusCode = response.statusCode();
        String errorCode = null;
        String errorMessage;
        
        try {
            ErrorResponse errorResponse = objectMapper.readValue(response.body(), ErrorResponse.class);
            errorCode = errorResponse.getCode();
            errorMessage = errorTranslator.translateHttpStatus(statusCode, errorCode);
        } catch (IOException e) {
            // Could not parse error response
            errorMessage = errorTranslator.translateCommunicationError(statusCode);
        }
        
        throw new MintApiException(errorMessage, statusCode, errorCode);
    }
}