package com.mintitv.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model for image object in process pool requests.
 */
public class ImageObject {
    
    @JsonProperty("base64")
    private final String base64;
    
    @JsonProperty("fileName")
    private final String fileName;
    
    @JsonProperty("fileType")
    private final String fileType;
    
    /**
     * Constructs a new ImageObject.
     *
     * @param base64 the base64 encoded image data
     * @param fileName the file name
     * @param fileType the MIME type (image/jpeg, application/pdf, image/tiff, image/png)
     */
    public ImageObject(String base64, String fileName, String fileType) {
        this.base64 = base64;
        this.fileName = fileName;
        this.fileType = fileType;
    }
    
    public String getBase64() {
        return base64;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public String getFileType() {
        return fileType;
    }
    
    /**
     * Builder for ImageObject.
     */
    public static class Builder {
        private String base64;
        private String fileName;
        private String fileType;
        
        public Builder base64(String base64) {
            this.base64 = base64;
            return this;
        }
        
        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }
        
        public Builder fileType(String fileType) {
            this.fileType = fileType;
            return this;
        }
        
        public ImageObject build() {
            return new ImageObject(base64, fileName, fileType);
        }
    }
}