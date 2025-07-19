package com.mintscan.api.utils;

import com.mintscan.api.models.ImageObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for Base64 encoding and file type detection.
 */
public class Base64Utils {
    
    private static final Map<String, String> FILE_TYPE_MAP = new HashMap<>();
    
    static {
        FILE_TYPE_MAP.put(".jpg", "image/jpeg");
        FILE_TYPE_MAP.put(".jpeg", "image/jpeg");
        FILE_TYPE_MAP.put(".png", "image/png");
        FILE_TYPE_MAP.put(".pdf", "application/pdf");
        FILE_TYPE_MAP.put(".tiff", "image/tiff");
        FILE_TYPE_MAP.put(".tif", "image/tiff");
    }
    
    /**
     * Private constructor to prevent instantiation.
     */
    private Base64Utils() {
    }
    
    /**
     * Encodes a file to Base64 string.
     *
     * @param filePath the path to the file
     * @return Base64 encoded string
     * @throws IOException if file cannot be read
     */
    public static String encodeFileToBase64(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] fileContent = Files.readAllBytes(path);
        return Base64.getEncoder().encodeToString(fileContent);
    }
    
    /**
     * Creates an ImageObject from a file path.
     *
     * @param filePath the path to the image file
     * @return ImageObject ready for API submission
     * @throws IOException if file cannot be read
     * @throws IllegalArgumentException if file type is not supported
     */
    public static ImageObject createImageObject(String filePath) throws IOException {
        return createImageObject(filePath, null);
    }
    
    /**
     * Creates an ImageObject from a file path with custom filename.
     *
     * @param filePath the path to the image file
     * @param customFileName custom filename to use (optional)
     * @return ImageObject ready for API submission
     * @throws IOException if file cannot be read
     * @throws IllegalArgumentException if file type is not supported
     */
    public static ImageObject createImageObject(String filePath, String customFileName) throws IOException {
        Path path = Paths.get(filePath);
        String fileName = customFileName != null ? customFileName : path.getFileName().toString();
        
        // Determine file type
        String extension = getFileExtension(fileName).toLowerCase();
        String fileType = FILE_TYPE_MAP.get(extension);
        
        if (fileType == null) {
            throw new IllegalArgumentException("Tipo de archivo no soportado: " + extension);
        }
        
        // Encode file to Base64
        String base64Content = encodeFileToBase64(filePath);
        
        return new ImageObject(base64Content, fileName, fileType);
    }
    
    /**
     * Gets the file extension from a filename.
     *
     * @param fileName the filename
     * @return the extension including the dot (e.g., ".jpg")
     */
    private static String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return "";
        }
        return fileName.substring(lastIndexOfDot);
    }
    
    /**
     * Checks if a file type is supported.
     *
     * @param fileName the filename to check
     * @return true if the file type is supported
     */
    public static boolean isSupportedFileType(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        return FILE_TYPE_MAP.containsKey(extension);
    }
    
    /**
     * Gets the MIME type for a filename.
     *
     * @param fileName the filename
     * @return the MIME type, or null if not supported
     */
    public static String getMimeType(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        return FILE_TYPE_MAP.get(extension);
    }
}