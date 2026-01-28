package com.eyesecurity.ingestion_service.model;

public class EnrichmentResponse {
    private String asn;
    private String category;
    private long correlationId;

    public EnrichmentResponse() {}

    public String getAsn() {
        return asn;
    }

    public String getCategory() {
        return category;
    }

    public long getCorrelationId() {
        return correlationId;
    }

    public void setAsn(String asn) {
        this.asn = asn;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCorrelationId(long correlationId) {
        this.correlationId = correlationId;
    }
}
