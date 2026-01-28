package com.eyesecurity.ingestion_service.model;

public class EnrichmentRequest {
    private long id;
    private String asset;
    private String ip;
    private String category;

    public EnrichmentRequest() {}

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
}