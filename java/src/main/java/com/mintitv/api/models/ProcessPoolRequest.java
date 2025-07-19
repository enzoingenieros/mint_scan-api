package com.mintitv.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Request model for process pool endpoint.
 */
public class ProcessPoolRequest {
    
    @JsonProperty("id")
    private final String id;
    
    @JsonProperty("type")
    private final DocumentType type;
    
    @JsonProperty("category")
    private final VehicleCategory category;
    
    @JsonProperty("images")
    private final List<ImageObject> images;
    
    @JsonProperty("name")
    private final String name;
    
    @JsonProperty("extractAccuracy")
    private final boolean extractAccuracy;
    
    /**
     * Constructs a new ProcessPoolRequest using builder pattern.
     */
    private ProcessPoolRequest(Builder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.category = builder.category;
        this.images = builder.images;
        this.name = builder.name;
        this.extractAccuracy = builder.extractAccuracy;
    }
    
    public String getId() {
        return id;
    }
    
    public DocumentType getType() {
        return type;
    }
    
    public VehicleCategory getCategory() {
        return category;
    }
    
    public List<ImageObject> getImages() {
        return images;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean getExtractAccuracy() {
        return extractAccuracy;
    }
    
    /**
     * Builder for ProcessPoolRequest.
     */
    public static class Builder {
        private String id;
        private DocumentType type;
        private VehicleCategory category;
        private List<ImageObject> images;
        private String name;
        private boolean extractAccuracy = false;
        
        public Builder id(String id) {
            this.id = id;
            return this;
        }
        
        public Builder type(DocumentType type) {
            this.type = type;
            return this;
        }
        
        public Builder category(VehicleCategory category) {
            this.category = category;
            return this;
        }
        
        public Builder images(List<ImageObject> images) {
            this.images = images;
            return this;
        }
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder extractAccuracy(boolean extractAccuracy) {
            this.extractAccuracy = extractAccuracy;
            return this;
        }
        
        public ProcessPoolRequest build() {
            if (id == null || type == null || category == null || images == null || images.isEmpty()) {
                throw new IllegalStateException("id, type, category, and images are required");
            }
            return new ProcessPoolRequest(this);
        }
    }
}