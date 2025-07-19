package com.mintitv.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

/**
 * Model for a processed document.
 */
public class ProcessDocument {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("documentId")
    private String documentId;
    
    @JsonProperty("license")
    private License license;
    
    @JsonProperty("technicalCard")
    private TechnicalCard technicalCard;
    
    @JsonProperty("status")
    private ProcessStatus status;
    
    @JsonProperty("createdAt")
    private String createdAt;
    
    @JsonProperty("updatedAt")
    private String updatedAt;
    
    /**
     * Default constructor for Jackson deserialization.
     */
    public ProcessDocument() {
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getDocumentId() {
        return documentId;
    }
    
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    
    public License getLicense() {
        return license;
    }
    
    public void setLicense(License license) {
        this.license = license;
    }
    
    public TechnicalCard getTechnicalCard() {
        return technicalCard;
    }
    
    public void setTechnicalCard(TechnicalCard technicalCard) {
        this.technicalCard = technicalCard;
    }
    
    public ProcessStatus getStatus() {
        return status;
    }
    
    public void setStatus(ProcessStatus status) {
        this.status = status;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}