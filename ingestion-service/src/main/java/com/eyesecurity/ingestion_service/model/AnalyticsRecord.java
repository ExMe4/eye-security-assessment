package com.eyesecurity.ingestion_service.model;

public class AnalyticsRecord {
    private long id;
    private String asset;
    private String ip;
    private String category;
    private String asn;
    private long correlationId;

    public AnalyticsRecord() {}

    public long getId() {
        return id;
    }

    public String getAsset() {
        return asset;
    }

    public String getIp() {
        return ip;
    }

    public String getCategory() {
        return category;
    }

    public String getAsn() {
        return asn;
    }

    public long getCorrelationId() {
        return correlationId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAsn(String asn) {
        this.asn = asn;
    }

    public void setCorrelationId(long correlationId) {
        this.correlationId = correlationId;
    }
}
