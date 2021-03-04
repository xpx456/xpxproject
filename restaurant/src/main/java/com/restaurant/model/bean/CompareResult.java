package com.restaurant.model.bean;

import lombok.Data;

@Data
public class CompareResult {
    private float diffLevel;
    private String rid;
    private String fingerIndex;

    public CompareResult(float diffLevel, String rid, String fingerIndex) {
        this.diffLevel = diffLevel;
        this.rid = rid;
        this.fingerIndex = fingerIndex;
    }

    public int compareTo(Object o) {
        if(o instanceof CompareResult) {
            CompareResult c = (CompareResult) o;
            return diffLevel > c.getDiffLevel() ? 1 : -1;
        }
        return 0;
    }

    public float getDiffLevel() {
        return diffLevel;
    }

    public String getRid() {
        return rid;
    }

    public String getFingerIndex() {
        return fingerIndex;
    }
}
