package com.lxf.ichat.dto;

public class StatusDTO {

    public static final String ONLINE = "在线";
    public static final String OFFLINE = "离线";

    private String id;

    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}