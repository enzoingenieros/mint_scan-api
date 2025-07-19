package com.mintitv.common;

/**
 * Application-wide constants.
 * Centralizes all magic values and configuration constants.
 */
public final class Constants {
    
    private Constants() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    // API Configuration
    public static final String API_BASE_URL = "https://rest.mintitv.com/api/v1";
    public static final int DEFAULT_TIMEOUT_SECONDS = 30;
    public static final String CONTENT_TYPE_JSON = "application/json";
    
    // Field Limits
    public static final int MAX_NAME_LENGTH = 100;
    
    // Display Symbols
    public static final String SUCCESS_SYMBOL = "✓";
    public static final String ERROR_SYMBOL = "✗";
    
    // Environment Variables
    public static final String ENV_TOKEN = "MINTITV_TOKEN";
    public static final String ENV_USER = "MINTITV_USER";
    public static final String ENV_PASS = "MINTITV_PASS";
    
    // HTTP Status Codes
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_UNPROCESSABLE_ENTITY = 422;
    public static final int HTTP_SERVER_ERROR = 500;
    
    // File Types
    public static final String MIME_JPEG = "image/jpeg";
    public static final String MIME_PNG = "image/png";
    public static final String MIME_PDF = "application/pdf";
    public static final String MIME_TIFF = "image/tiff";
    
    // File Extensions
    public static final String EXT_JPG = ".jpg";
    public static final String EXT_JPEG = ".jpeg";
    public static final String EXT_PNG = ".png";
    public static final String EXT_PDF = ".pdf";
    public static final String EXT_TIFF = ".tiff";
    public static final String EXT_TIF = ".tif";
    
    // Supported Document Types
    public static final String[] SUPPORTED_DOCUMENT_TYPES = {
        "coc", "titv-old", "titv-new", "reduced", "single-approval", "cdc", "manual"
    };
    
    // Supported Vehicle Categories
    public static final String[] SUPPORTED_VEHICLE_CATEGORIES = {
        "M1", "M3", "N1", "N3", "L", "O", "T", "TR", "OS", "OSR"
    };
    
    // Error Exit Codes
    public static final int EXIT_SUCCESS = 0;
    public static final int EXIT_ERROR = 1;
    
    // Default Values
    public static final boolean DEFAULT_EXTRACT_ACCURACY = false;
}