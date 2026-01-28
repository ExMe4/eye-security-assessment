package com.eyesecurity.ingestion_service.model;

public class SecurityLogRecord {

    private long id;
    private String assetName;
    private String ip;
    private String createdUtc;
    private String source;
    private String category;

    public SecurityLogRecord() {}

    public long getId() {
        return id;
    }

    public String getAssetName() {
        return assetName;
    }

    public String getIp() {
        return ip;
    }

    public String getCreatedUtc() {
        return createdUtc;
    }

    public String getSource() {
        return source;
    }

    public String getCategory() {
        return category;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setCreatedUtc(String createdUtc) {
        this.createdUtc = createdUtc;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}