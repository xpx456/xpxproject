package com.restaurant.model.bean;

import lombok.Data;

@Data
public class VeinInfo {
    private String rid;
    private String fingerName;

    public VeinInfo() {}
    public VeinInfo(String rid, String fingerName) {
        this.fingerName = fingerName;
        this.rid = rid;
    }

    public String getFingerName() {
        return fingerName;
    }
}
