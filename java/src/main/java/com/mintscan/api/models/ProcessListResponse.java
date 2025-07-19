package com.mintscan.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response model for process list endpoint.
 */
public class ProcessListResponse {
    
    @JsonProperty("processDocuments")
    private List<ProcessDocument> processDocuments;
    
    /**
     * Default constructor for Jackson deserialization.
     */
    public ProcessListResponse() {
    }
    
    public List<ProcessDocument> getProcessDocuments() {
        return processDocuments;
    }
    
    public void setProcessDocuments(List<ProcessDocument> processDocuments) {
        this.processDocuments = processDocuments;
    }
}