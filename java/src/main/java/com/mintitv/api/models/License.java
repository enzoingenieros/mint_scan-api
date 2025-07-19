package com.mintitv.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model for vehicle license information.
 */
public class License {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("code")
    private String code;
    
    @JsonProperty("customerId")
    private String customerId;
    
    @JsonProperty("itv")
    private String itv;
    
    /**
     * Default constructor for Jackson deserialization.
     */
    public License() {
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getItv() {
        return itv;
    }
    
    public void setItv(String itv) {
        this.itv = itv;
    }
}