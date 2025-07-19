package com.mintscan.api.core.validation;

import com.mintscan.common.Messages;
import com.mintscan.api.utils.Base64Utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Validates files for processing.
 * Centralizes file validation logic used across the application.
 */
public class FileValidator {
    
    /**
     * Result of file validation containing valid files and errors.
     */
    public static class ValidationResult {
        private final List<String> validFiles;
        private final List<String> errors;
        
        public ValidationResult(List<String> validFiles, List<String> errors) {
            this.validFiles = validFiles;
            this.errors = errors;
        }
        
        public List<String> getValidFiles() {
            return validFiles;
        }
        
        public List<String> getErrors() {
            return errors;
        }
        
        public boolean hasErrors() {
            return !errors.isEmpty();
        }
        
        public boolean hasValidFiles() {
            return !validFiles.isEmpty();
        }
    }
    
    /**
     * Validates a list of file paths.
     *
     * @param filePaths the file paths to validate
     * @return validation result containing valid files and errors
     */
    public ValidationResult validateFiles(List<String> filePaths) {
        List<String> validFiles = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        if (filePaths == null || filePaths.isEmpty()) {
            errors.add(Messages.AT_LEAST_ONE_FILE_REQUIRED);
            return new ValidationResult(validFiles, errors);
        }
        
        for (String filePath : filePaths) {
            ValidationResult singleResult = validateFile(filePath);
            if (singleResult.hasValidFiles()) {
                validFiles.addAll(singleResult.getValidFiles());
            }
            if (singleResult.hasErrors()) {
                errors.addAll(singleResult.getErrors());
            }
        }
        
        return new ValidationResult(validFiles, errors);
    }
    
    /**
     * Validates a single file path.
     *
     * @param filePath the file path to validate
     * @return validation result
     */
    public ValidationResult validateFile(String filePath) {
        List<String> validFiles = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        if (filePath == null || filePath.trim().isEmpty()) {
            errors.add("Empty file path provided");
            return new ValidationResult(validFiles, errors);
        }
        
        try {
            Path path = Paths.get(filePath);
            
            if (!Files.exists(path)) {
                errors.add(String.format(Messages.FILE_NOT_FOUND, filePath));
            } else if (!Files.isRegularFile(path)) {
                errors.add(String.format(Messages.NOT_A_FILE, filePath));
            } else if (!Base64Utils.isSupportedFileType(filePath)) {
                errors.add(String.format(Messages.UNSUPPORTED_FILE_TYPE, filePath));
            } else {
                validFiles.add(filePath);
            }
        } catch (Exception e) {
            errors.add(String.format(Messages.ERROR_PROCESSING_FILE, filePath, e.getMessage()));
        }
        
        return new ValidationResult(validFiles, errors);
    }
    
    /**
     * Checks if a file exists and is readable.
     *
     * @param filePath the file path to check
     * @return true if file exists and is readable
     */
    public boolean isFileAccessible(String filePath) {
        if (filePath == null) {
            return false;
        }
        
        try {
            Path path = Paths.get(filePath);
            return Files.exists(path) && Files.isRegularFile(path) && Files.isReadable(path);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Gets the file size in bytes.
     *
     * @param filePath the file path
     * @return file size in bytes, or -1 if error
     */
    public long getFileSize(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.size(path);
        } catch (Exception e) {
            return -1;
        }
    }
    
    /**
     * Validates that a file does not exceed a size limit.
     *
     * @param filePath the file path
     * @param maxSizeBytes maximum allowed size in bytes
     * @return true if file is within size limit
     */
    public boolean isWithinSizeLimit(String filePath, long maxSizeBytes) {
        long fileSize = getFileSize(filePath);
        return fileSize > 0 && fileSize <= maxSizeBytes;
    }
}