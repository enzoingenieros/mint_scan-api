package com.mintitv.api.core.validation;

import com.mintitv.api.models.DocumentType;
import com.mintitv.api.models.VehicleCategory;
import com.mintitv.api.models.ImageObject;
import com.mintitv.common.Messages;

import java.util.List;
import java.util.UUID;

/**
 * Utility class for common validation operations.
 * Centralizes validation logic to avoid duplication.
 */
public final class ValidationUtils {
    
    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Validates that a token is not null or empty.
     *
     * @param token the token to validate
     * @throws IllegalArgumentException if token is invalid
     */
    public static void validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException(Messages.TOKEN_REQUIRED);
        }
    }
    
    /**
     * Validates and parses a document type string.
     *
     * @param typeStr the document type string
     * @return the parsed DocumentType
     * @throws IllegalArgumentException if type is invalid
     */
    public static DocumentType validateDocumentType(String typeStr) {
        if (typeStr == null || typeStr.trim().isEmpty()) {
            throw new IllegalArgumentException(Messages.DOCUMENT_TYPE_REQUIRED);
        }
        
        try {
            return DocumentType.fromValue(typeStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                String.format(Messages.INVALID_DOCUMENT_TYPE, typeStr)
            );
        }
    }
    
    /**
     * Validates and parses a vehicle category string.
     *
     * @param categoryStr the vehicle category string
     * @return the parsed VehicleCategory
     * @throws IllegalArgumentException if category is invalid
     */
    public static VehicleCategory validateVehicleCategory(String categoryStr) {
        if (categoryStr == null || categoryStr.trim().isEmpty()) {
            throw new IllegalArgumentException(Messages.VEHICLE_CATEGORY_REQUIRED);
        }
        
        try {
            return VehicleCategory.valueOf(categoryStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                String.format(Messages.INVALID_VEHICLE_CATEGORY, categoryStr)
            );
        }
    }
    
    /**
     * Validates that a list of images is not null or empty.
     *
     * @param images the images to validate
     * @throws IllegalArgumentException if images are invalid
     */
    public static void validateImages(List<ImageObject> images) {
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException(Messages.AT_LEAST_ONE_IMAGE_REQUIRED);
        }
    }
    
    /**
     * Validates and parses a UUID string.
     *
     * @param uuidStr the UUID string
     * @return the parsed UUID
     * @throws IllegalArgumentException if UUID is invalid
     */
    public static UUID validateUUID(String uuidStr) {
        if (uuidStr == null || uuidStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            return UUID.fromString(uuidStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                String.format(Messages.INVALID_UUID, uuidStr)
            );
        }
    }
    
    /**
     * Validates request parameters for process pool operations.
     *
     * @param token the authentication token
     * @param documentType the document type
     * @param vehicleCategory the vehicle category
     * @param images the list of images
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public static void validateProcessPoolParameters(String token, 
                                                   DocumentType documentType,
                                                   VehicleCategory vehicleCategory,
                                                   List<ImageObject> images) {
        validateToken(token);
        
        if (documentType == null) {
            throw new IllegalArgumentException(Messages.DOCUMENT_TYPE_REQUIRED);
        }
        
        if (vehicleCategory == null) {
            throw new IllegalArgumentException(Messages.VEHICLE_CATEGORY_REQUIRED);
        }
        
        validateImages(images);
    }
    
    /**
     * Validates and truncates a name to maximum allowed length.
     *
     * @param name the name to validate
     * @param maxLength the maximum allowed length
     * @return the validated and possibly truncated name
     */
    public static String validateAndTruncateName(String name, int maxLength) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        
        String trimmed = name.trim();
        return trimmed.length() > maxLength ? trimmed.substring(0, maxLength) : trimmed;
    }
    
    /**
     * Validates that a required string is not null or empty.
     *
     * @param value the value to validate
     * @param errorMessage the error message if validation fails
     * @throws IllegalArgumentException if value is invalid
     */
    public static void validateRequired(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
    
    /**
     * Validates that a required object is not null.
     *
     * @param value the value to validate
     * @param errorMessage the error message if validation fails
     * @throws IllegalArgumentException if value is null
     */
    public static void validateNotNull(Object value, String errorMessage) {
        if (value == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}