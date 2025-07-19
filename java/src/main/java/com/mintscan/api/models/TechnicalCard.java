package com.mintscan.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model for vehicle technical card.
 */
public class TechnicalCard {
    
    @JsonProperty("type")
    private DocumentType type;
    
    @JsonProperty("category")
    private VehicleCategory category;
    
    @JsonProperty("model")
    private String model;
    
    @JsonProperty("vehicleLicense")
    private String vehicleLicense;
    
    @JsonProperty("vin")
    private String vin;
    
    @JsonProperty("ict")
    private Boolean ict;
    
    @JsonProperty("data")
    private TechnicalCardData data;
    
    /**
     * Default constructor for Jackson deserialization.
     */
    public TechnicalCard() {
    }
    
    public DocumentType getType() {
        return type;
    }
    
    public void setType(DocumentType type) {
        this.type = type;
    }
    
    public VehicleCategory getCategory() {
        return category;
    }
    
    public void setCategory(VehicleCategory category) {
        this.category = category;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getVehicleLicense() {
        return vehicleLicense;
    }
    
    public void setVehicleLicense(String vehicleLicense) {
        this.vehicleLicense = vehicleLicense;
    }
    
    public String getVin() {
        return vin;
    }
    
    public void setVin(String vin) {
        this.vin = vin;
    }
    
    public Boolean getIct() {
        return ict;
    }
    
    public void setIct(Boolean ict) {
        this.ict = ict;
    }
    
    public TechnicalCardData getData() {
        return data;
    }
    
    public void setData(TechnicalCardData data) {
        this.data = data;
    }
}