package com.mintscan.api.core.http;

import com.mintscan.common.Constants;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

/**
 * Wrapper for Java HTTP client focused only on HTTP operations.
 * No business logic or error handling - just pure HTTP communication.
 */
public class HttpClientWrapper {
    
    private final HttpClient httpClient;
    private final String baseUrl;
    private final Duration timeout;
    
    public HttpClientWrapper() {
        this(Constants.API_BASE_URL, Duration.ofSeconds(Constants.DEFAULT_TIMEOUT_SECONDS));
    }
    
    public HttpClientWrapper(String baseUrl, Duration timeout) {
        this.baseUrl = baseUrl;
        this.timeout = timeout;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(timeout)
                .build();
    }
    
    /**
     * Performs a GET request.
     *
     * @param endpoint the API endpoint
     * @param headers additional headers
     * @return HTTP response
     * @throws IOException if connection fails
     * @throws InterruptedException if request is interrupted
     */
    public HttpResponse<String> get(String endpoint, Map<String, String> headers) 
            throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .timeout(timeout)
                .GET();
        
        if (headers != null) {
            headers.forEach(requestBuilder::header);
        }
        
        HttpRequest request = requestBuilder.build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
    
    /**
     * Performs a POST request.
     *
     * @param endpoint the API endpoint
     * @param headers additional headers
     * @param body the request body
     * @return HTTP response
     * @throws IOException if connection fails
     * @throws InterruptedException if request is interrupted
     */
    public HttpResponse<String> post(String endpoint, Map<String, String> headers, String body) 
            throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .timeout(timeout)
                .header("Content-Type", Constants.CONTENT_TYPE_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(body));
        
        if (headers != null) {
            headers.forEach(requestBuilder::header);
        }
        
        HttpRequest request = requestBuilder.build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
    
    /**
     * Creates authorization header with Bearer token.
     *
     * @param token the JWT token
     * @return map containing the authorization header
     */
    public static Map<String, String> createBearerHeader(String token) {
        return Map.of("Authorization", "Bearer " + token);
    }
}