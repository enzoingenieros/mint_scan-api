package com.mintitv.common;

/**
 * User-facing messages in English.
 * Centralizes all output messages for consistency and potential internationalization.
 */
public final class Messages {
    
    private Messages() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    // General Messages
    public static final String PROCESSING_FILES = "Processing %d file(s)...";
    public static final String PROCESS_ID = "Process ID: %s";
    public static final String DOCUMENT_TYPE = "Type: %s";
    public static final String VEHICLE_CATEGORY = "Category: %s";
    public static final String NAME = "Name: %s";
    
    // Success Messages
    public static final String PROCESSING_STARTED_SUCCESSFULLY = "Processing started successfully";
    public static final String AUTHENTICATION_SUCCESSFUL = "Authentication successful";
    public static final String TOKEN_SAVED = "Token saved in %s";
    public static final String DOCUMENTS_RETRIEVED = "Retrieved %d document(s)";
    
    // Error Messages - General
    public static final String ERROR_PREFIX = "Error: ";
    public static final String CONNECTION_ERROR = "Connection error";
    public static final String PROCESSING_ERROR = "Processing error";
    public static final String INVALID_INPUT_DATA = "Invalid input data";
    
    // Error Messages - Authentication
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String TOKEN_INVALID_OR_EXPIRED = "Token invalid or expired";
    public static final String ACCESS_DENIED = "Access denied";
    public static final String TOKEN_REQUIRED = "Token required (--token or MINTITV_TOKEN environment variable)";
    public static final String USERNAME_REQUIRED = "Username required (--user or MINTITV_USER environment variable)";
    public static final String PASSWORD_REQUIRED = "Password required (--pass or MINTITV_PASS environment variable)";
    
    // Error Messages - File Operations
    public static final String FILE_NOT_FOUND = "File not found: %s";
    public static final String NOT_A_FILE = "Not a file: %s";
    public static final String UNSUPPORTED_FILE_TYPE = "Unsupported file type: %s";
    public static final String ERROR_PROCESSING_FILE = "Error processing %s: %s";
    public static final String NO_VALID_FILES = "No valid files found to process";
    public static final String AT_LEAST_ONE_FILE_REQUIRED = "At least one file required for processing";
    
    // Error Messages - Validation
    public static final String DOCUMENT_TYPE_REQUIRED = "Document type required (--tipo)";
    public static final String VEHICLE_CATEGORY_REQUIRED = "Vehicle category required (--categoria)";
    public static final String INVALID_DOCUMENT_TYPE = "Invalid document type: %s";
    public static final String INVALID_VEHICLE_CATEGORY = "Invalid vehicle category: %s";
    public static final String INVALID_UUID = "Invalid ID (must be UUID): %s";
    public static final String AT_LEAST_ONE_IMAGE_REQUIRED = "At least one image required";
    
    // Error Messages - API Specific
    public static final String SERVICE_UNAVAILABLE = "Service unavailable";
    public static final String DOCUMENT_NOT_FOUND_API = "Document not found";
    public static final String DOCUMENT_NOT_SAME_STATION = "Document not available for this station";
    public static final String DOCUMENT_NOT_SAME_CUSTOMER = "Document not available for this customer";
    public static final String DOCUMENT_NOT_PROCESSED = "Document not yet processed";
    public static final String SERVER_ERROR = "Server error";
    public static final String ERROR_WITH_CODE = "Error processing request (code: %d)";
    public static final String COMMUNICATION_ERROR_WITH_CODE = "Communication error (code: %d)";
    
    // Help Messages
    public static final String USAGE_PREFIX = "Usage: ";
    public static final String ARGUMENTS_SECTION = "Arguments:";
    public static final String REQUIRED_OPTIONS_SECTION = "Required Options:";
    public static final String ADDITIONAL_OPTIONS_SECTION = "Additional Options:";
    public static final String EXAMPLES_SECTION = "Examples:";
    public static final String SUPPORTED_DOCUMENT_TYPES = "Supported document types:";
    public static final String SUPPORTED_VEHICLE_CATEGORIES = "Vehicle categories:";
    public static final String SUPPORTED_FILE_FORMATS = "Supported file formats:";
    
    // Command Descriptions
    public static final String LOGIN_DESCRIPTION = "Authenticate with MintITV API";
    public static final String PROCESS_DESCRIPTION = "Process document images in MintITV API";
    public static final String LIST_DESCRIPTION = "List processed documents from MintITV API";
    public static final String RETRIEVE_DESCRIPTION = "Retrieve a specific document from MintITV API";
    
    // Option Descriptions
    public static final String OPTION_TOKEN = "JWT authentication token";
    public static final String OPTION_USER = "Username";
    public static final String OPTION_PASSWORD = "Password";
    public static final String OPTION_DOCUMENT_TYPE = "Document type";
    public static final String OPTION_VEHICLE_CATEGORY = "Vehicle category";
    public static final String OPTION_NAME = "Descriptive name (max 100 characters)";
    public static final String OPTION_ID = "Process UUID (generated if not specified)";
    public static final String OPTION_EXTRACT_ACCURACY = "Calculate extraction accuracy (experimental)";
    public static final String OPTION_VERBOSE = "Show detailed information";
    public static final String OPTION_HELP = "Show this help";
    public static final String OPTION_OUTPUT_FILE = "Save result to file";
    public static final String OPTION_LIMIT = "Maximum number of documents to retrieve";
    public static final String OPTION_CURSOR = "Pagination cursor for next page";
    
    // Warnings
    public static final String WARNINGS_PREFIX = "Warnings: ";
    public static final String FILES_COULD_NOT_BE_PROCESSED = "Could not process files: %s";
    
    // Info Messages
    public static final String CHECK_STATUS_WITH = "You can check the status with:";
    public static final String RUN_LOGIN_TO_GET_TOKEN = "Run 'mintitv-cli login' to get a token";
    public static final String FILES_TO_PROCESS = "Files to process";
    public static final String INTERACTIVE_PASSWORD_PROMPT = "Enter password: ";
}