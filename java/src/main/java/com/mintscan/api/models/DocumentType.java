package com.mintscan.api.models;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum for document types.
 */
public enum DocumentType {
    COC("coc"),
    TITV_OLD("titv-old"),
    TITV_NEW("titv-new"),
    REDUCED("reduced"),
    SINGLE_APPROVAL("single-approval"),
    CDC("cdc"),
    MANUAL("manual");
    
    private final String value;
    
    DocumentType(String value) {
        this.value = value;
    }
    
    @JsonValue
    public String getValue() {
        return value;
    }
    
    public static DocumentType fromValue(String value) {
        for (DocumentType type : DocumentType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown document type: " + value);
    }
}