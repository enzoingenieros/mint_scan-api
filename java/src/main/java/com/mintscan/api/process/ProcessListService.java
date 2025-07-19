package com.mintscan.api.process;

import com.mintscan.api.exceptions.MintApiException;
import com.mintscan.api.models.*;
import com.mintscan.api.utils.HttpClient;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for listing and filtering processed documents.
 */
public class ProcessListService {
    
    private final HttpClient httpClient;
    
    /**
     * Constructs a new ProcessListService.
     */
    public ProcessListService() {
        this.httpClient = new HttpClient();
    }
    
    /**
     * Lists all processed documents.
     *
     * @param token the JWT authentication token
     * @return list of processed documents
     * @throws MintApiException if the request fails
     */
    public List<ProcessDocument> listProcessedDocuments(String token) throws MintApiException {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("El token es requerido");
        }
        
        ProcessListResponse response = httpClient.get(
            "/process",
            HttpClient.createBearerHeader(token),
            ProcessListResponse.class
        );
        
        return response.getProcessDocuments();
    }
    
    /**
     * Filters documents by status.
     *
     * @param documents list of documents to filter
     * @param status the status to filter by
     * @return filtered list of documents
     */
    public List<ProcessDocument> filterByStatus(List<ProcessDocument> documents, ProcessStatus status) {
        return documents.stream()
                .filter(doc -> doc.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    /**
     * Filters documents by document type.
     *
     * @param documents list of documents to filter
     * @param type the document type to filter by
     * @return filtered list of documents
     */
    public List<ProcessDocument> filterByType(List<ProcessDocument> documents, DocumentType type) {
        return documents.stream()
                .filter(doc -> doc.getTechnicalCard() != null && doc.getTechnicalCard().getType() == type)
                .collect(Collectors.toList());
    }
    
    /**
     * Filters documents by vehicle category.
     *
     * @param documents list of documents to filter
     * @param category the vehicle category to filter by
     * @return filtered list of documents
     */
    public List<ProcessDocument> filterByCategory(List<ProcessDocument> documents, VehicleCategory category) {
        return documents.stream()
                .filter(doc -> doc.getTechnicalCard() != null && doc.getTechnicalCard().getCategory() == category)
                .collect(Collectors.toList());
    }
    
    /**
     * Filters documents by ITV station.
     *
     * @param documents list of documents to filter
     * @param itv the ITV station code to filter by
     * @return filtered list of documents
     */
    public List<ProcessDocument> filterByItv(List<ProcessDocument> documents, String itv) {
        return documents.stream()
                .filter(doc -> doc.getLicense() != null && itv.equals(doc.getLicense().getItv()))
                .collect(Collectors.toList());
    }
    
    /**
     * Sorts documents by date.
     *
     * @param documents list of documents to sort
     * @param sortByCreatedAt true to sort by creation date, false to sort by update date
     * @param descending true for descending order (newest first), false for ascending
     * @return sorted list of documents
     */
    public List<ProcessDocument> sortByDate(List<ProcessDocument> documents, 
                                          boolean sortByCreatedAt, 
                                          boolean descending) {
        Comparator<ProcessDocument> comparator = sortByCreatedAt 
            ? Comparator.comparing(ProcessDocument::getCreatedAt)
            : Comparator.comparing(ProcessDocument::getUpdatedAt);
            
        if (descending) {
            comparator = comparator.reversed();
        }
        
        return documents.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets document statistics.
     *
     * @param documents list of documents to analyze
     * @return statistics object
     */
    public DocumentStatistics getStatistics(List<ProcessDocument> documents) {
        return new DocumentStatistics(documents);
    }
    
    /**
     * Statistics for a collection of documents.
     */
    public static class DocumentStatistics {
        private final int total;
        private final java.util.Map<ProcessStatus, Long> statusCounts;
        private final java.util.Map<DocumentType, Long> typeCounts;
        private final java.util.Map<String, Long> itvCounts;
        
        public DocumentStatistics(List<ProcessDocument> documents) {
            this.total = documents.size();
            
            this.statusCounts = documents.stream()
                    .collect(Collectors.groupingBy(
                        ProcessDocument::getStatus,
                        Collectors.counting()
                    ));
            
            this.typeCounts = documents.stream()
                    .filter(doc -> doc.getTechnicalCard() != null && doc.getTechnicalCard().getType() != null)
                    .collect(Collectors.groupingBy(
                        doc -> doc.getTechnicalCard().getType(),
                        Collectors.counting()
                    ));
            
            this.itvCounts = documents.stream()
                    .filter(doc -> doc.getLicense() != null && doc.getLicense().getItv() != null)
                    .collect(Collectors.groupingBy(
                        doc -> doc.getLicense().getItv(),
                        Collectors.counting()
                    ));
        }
        
        public int getTotal() {
            return total;
        }
        
        public java.util.Map<ProcessStatus, Long> getStatusCounts() {
            return statusCounts;
        }
        
        public java.util.Map<DocumentType, Long> getTypeCounts() {
            return typeCounts;
        }
        
        public java.util.Map<String, Long> getItvCounts() {
            return itvCounts;
        }
    }
}