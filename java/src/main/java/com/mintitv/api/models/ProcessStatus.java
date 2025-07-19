package com.mintitv.api.models;

/**
 * Enum for document processing status.
 */
public enum ProcessStatus {
    PENDING,
    STRAIGHTENING,
    RECOGNIZING,
    COMPLETED,
    FAILED,
    RETRIEVED,
    ABORTED
}