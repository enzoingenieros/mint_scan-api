package com.mintscan.api.process;

import com.mintscan.api.exceptions.MintApiException;
import com.mintscan.api.models.*;
import com.mintscan.api.utils.Base64Utils;
import com.mintscan.api.utils.HttpClient;
import com.mintscan.api.core.validation.ValidationUtils;
import com.mintscan.api.core.validation.FileValidator;
import com.mintscan.common.Constants;
import com.mintscan.common.Messages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service for processing images in pool for document recognition.
 */
public class ProcessImagePoolService {
    
    private final HttpClient httpClient;
    private final FileValidator fileValidator;
    
    /**
     * Constructs a new ProcessImagePoolService.
     */
    public ProcessImagePoolService() {
        this.httpClient = new HttpClient();
        this.fileValidator = new FileValidator();
    }
    
    /**
     * Constructs a new ProcessImagePoolService with dependencies.
     */
    public ProcessImagePoolService(HttpClient httpClient, FileValidator fileValidator) {
        this.httpClient = httpClient;
        this.fileValidator = fileValidator;
    }
    
    /**
     * Processes a single image file.
     *
     * @param token the JWT authentication token
     * @param filePath path to the image file
     * @param documentType the type of document
     * @param vehicleCategory the vehicle category
     * @return process pool response
     * @throws MintApiException if the request fails
     * @throws IOException if file cannot be read
     */
    public ProcessPoolResponse processSingleImage(
            String token,
            String filePath,
            DocumentType documentType,
            VehicleCategory vehicleCategory) throws MintApiException, IOException {
        
        return processSingleImage(token, filePath, documentType, vehicleCategory, null, Constants.DEFAULT_EXTRACT_ACCURACY);
    }
    
    /**
     * Processes a single image file with optional parameters.
     *
     * @param token the JWT authentication token
     * @param filePath path to the image file
     * @param documentType the type of document
     * @param vehicleCategory the vehicle category
     * @param name optional name for identification
     * @param extractAccuracy optional flag for accuracy calculation
     * @return process pool response
     * @throws MintApiException if the request fails
     * @throws IOException if file cannot be read
     */
    public ProcessPoolResponse processSingleImage(
            String token,
            String filePath,
            DocumentType documentType,
            VehicleCategory vehicleCategory,
            String name,
            boolean extractAccuracy) throws MintApiException, IOException {
        
        ImageObject image = Base64Utils.createImageObject(filePath);
        List<ImageObject> images = List.of(image);
        
        return processImages(token, images, documentType, vehicleCategory, name, extractAccuracy);
    }
    
    /**
     * Processes multiple image files.
     *
     * @param token the JWT authentication token
     * @param filePaths list of paths to image files
     * @param documentType the type of document
     * @param vehicleCategory the vehicle category
     * @return process pool response
     * @throws MintApiException if the request fails
     * @throws IOException if any file cannot be read
     */
    public ProcessPoolResponse processMultipleImages(
            String token,
            List<String> filePaths,
            DocumentType documentType,
            VehicleCategory vehicleCategory) throws MintApiException, IOException {
        
        return processMultipleImages(token, filePaths, documentType, vehicleCategory, null, Constants.DEFAULT_EXTRACT_ACCURACY);
    }
    
    /**
     * Processes multiple image files with optional parameters.
     *
     * @param token the JWT authentication token
     * @param filePaths list of paths to image files
     * @param documentType the type of document
     * @param vehicleCategory the vehicle category
     * @param name optional name for identification
     * @param extractAccuracy optional flag for accuracy calculation
     * @return process pool response
     * @throws MintApiException if the request fails
     * @throws IOException if any file cannot be read
     */
    public ProcessPoolResponse processMultipleImages(
            String token,
            List<String> filePaths,
            DocumentType documentType,
            VehicleCategory vehicleCategory,
            String name,
            boolean extractAccuracy) throws MintApiException, IOException {
        
        FileValidator.ValidationResult validationResult = fileValidator.validateFiles(filePaths);
        
        if (!validationResult.hasValidFiles()) {
            throw new MintApiException(String.format(Messages.FILES_COULD_NOT_BE_PROCESSED,
                String.join(", ", validationResult.getErrors())));
        }
        
        if (validationResult.hasErrors()) {
            System.err.println(Messages.WARNINGS_PREFIX + String.join(", ", validationResult.getErrors()));
        }
        
        List<ImageObject> images = new ArrayList<>();
        for (String filePath : validationResult.getValidFiles()) {
            try {
                ImageObject image = Base64Utils.createImageObject(filePath);
                images.add(image);
            } catch (IOException e) {
                throw new MintApiException(String.format(Messages.ERROR_PROCESSING_FILE, filePath, e.getMessage()));
            }
        }
        
        return processImages(token, images, documentType, vehicleCategory, name, extractAccuracy);
    }
    
    /**
     * Processes images for document recognition.
     *
     * @param token the JWT authentication token
     * @param images list of image objects
     * @param documentType the type of document
     * @param vehicleCategory the vehicle category
     * @param name optional name for identification
     * @param extractAccuracy optional flag for accuracy calculation
     * @return process pool response
     * @throws MintApiException if the request fails
     */
    public ProcessPoolResponse processImages(
            String token,
            List<ImageObject> images,
            DocumentType documentType,
            VehicleCategory vehicleCategory,
            String name,
            boolean extractAccuracy) throws MintApiException {
        
        ValidationUtils.validateProcessPoolParameters(token, documentType, vehicleCategory, images);
        
        // Generate UUID for the process
        String processId = UUID.randomUUID().toString();
        
        // Build request
        ProcessPoolRequest.Builder requestBuilder = new ProcessPoolRequest.Builder()
                .id(processId)
                .type(documentType)
                .category(vehicleCategory)
                .images(images);
        
        String validatedName = ValidationUtils.validateAndTruncateName(name, Constants.MAX_NAME_LENGTH);
        if (validatedName != null) {
            requestBuilder.name(validatedName);
        }
        
        requestBuilder.extractAccuracy(extractAccuracy);
        
        ProcessPoolRequest request = requestBuilder.build();
        
        return httpClient.post(
            "/process/pool",
            HttpClient.createBearerHeader(token),
            request,
            ProcessPoolResponse.class
        );
    }
    
    /**
     * Processes images with a custom process ID.
     *
     * @param token the JWT authentication token
     * @param processId custom UUID for the process
     * @param images list of image objects
     * @param documentType the type of document
     * @param vehicleCategory the vehicle category
     * @param name optional name for identification
     * @param extractAccuracy optional flag for accuracy calculation
     * @return process pool response
     * @throws MintApiException if the request fails
     */
    public ProcessPoolResponse processImagesWithId(
            String token,
            String processId,
            List<ImageObject> images,
            DocumentType documentType,
            VehicleCategory vehicleCategory,
            String name,
            boolean extractAccuracy) throws MintApiException {
        
        // Validate UUID format
        ValidationUtils.validateUUID(processId);
        
        ValidationUtils.validateProcessPoolParameters(token, documentType, vehicleCategory, images);
        
        // Build request
        ProcessPoolRequest.Builder requestBuilder = new ProcessPoolRequest.Builder()
                .id(processId)
                .type(documentType)
                .category(vehicleCategory)
                .images(images);
        
        if (name != null && !name.trim().isEmpty()) {
            String truncatedName = name.length() > 100 ? name.substring(0, 100) : name;
            requestBuilder.name(truncatedName);
        }
        
        requestBuilder.extractAccuracy(extractAccuracy);
        
        ProcessPoolRequest request = requestBuilder.build();
        
        return httpClient.post(
            "/process/pool",
            HttpClient.createBearerHeader(token),
            request,
            ProcessPoolResponse.class
        );
    }
}