package com.eyesecurity.cli.model;

public class SecurityLogRecord {

    private long id;
    private String assetName;
    private String ip;
    private String createdUtc;
    private String source;
    private String category;

    public SecurityLogRecord(long id, String assetName, String ip,
                             String createdUtc, String source, String category) {
        this.id = id;
        this.assetName = assetName;
        this.ip = ip;
        this.createdUtc = createdUtc;
        this.source = source;
        this.category = category;
    }

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
}