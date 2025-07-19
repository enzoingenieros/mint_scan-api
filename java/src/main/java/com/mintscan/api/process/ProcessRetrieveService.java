package com.mintscan.api.process;

import com.mintscan.api.exceptions.MintApiException;
import com.mintscan.api.models.ProcessDocument;
import com.mintscan.api.utils.HttpClient;

/**
 * Service for retrieving specific processed documents.
 */
public class ProcessRetrieveService {
    
    private final HttpClient httpClient;
    
    /**
     * Constructs a new ProcessRetrieveService.
     */
    public ProcessRetrieveService() {
        this.httpClient = new HttpClient();
    }
    
    /**
     * Retrieves a specific processed document by ID.
     *
     * @param token the JWT authentication token
     * @param processId the UUID of the process to retrieve
     * @return the processed document
     * @throws MintApiException if the request fails or document is not found
     */
    public ProcessDocument retrieveProcessedDocument(String token, String processId) 
            throws MintApiException {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("El token es requerido");
        }
        
        if (processId == null || processId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del proceso es requerido");
        }
        
        // Validate UUID format
        try {
            java.util.UUID.fromString(processId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El ID del proceso debe ser un UUID válido");
        }
        
        return httpClient.get(
            "/process/" + processId,
            HttpClient.createBearerHeader(token),
            ProcessDocument.class
        );
    }
    
    /**
     * Checks if a document is ready for retrieval.
     *
     * @param document the document to check
     * @return true if the document is completed and ready
     */
    public boolean isDocumentReady(ProcessDocument document) {
        return document != null && 
               document.getStatus() == com.mintscan.api.models.ProcessStatus.COMPLETED;
    }
    
    /**
     * Checks if a document has failed processing.
     *
     * @param document the document to check
     * @return true if the document processing has failed
     */
    public boolean isDocumentFailed(ProcessDocument document) {
        return document != null && 
               (document.getStatus() == com.mintscan.api.models.ProcessStatus.FAILED ||
                document.getStatus() == com.mintscan.api.models.ProcessStatus.ABORTED);
    }
    
    /**
     * Checks if a document is still being processed.
     *
     * @param document the document to check
     * @return true if the document is still being processed
     */
    public boolean isDocumentProcessing(ProcessDocument document) {
        return document != null && 
               (document.getStatus() == com.mintscan.api.models.ProcessStatus.PENDING ||
                document.getStatus() == com.mintscan.api.models.ProcessStatus.STRAIGHTENING ||
                document.getStatus() == com.mintscan.api.models.ProcessStatus.RECOGNIZING);
    }
}