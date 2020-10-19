package jx.vein.javajar.vein.subject;

import jx.vein.javajar.JXVeinJavaAPI;

public class FeatureBean {
    private byte[] feat;
    private int score;

    public FeatureBean() {
        this.feat = new byte[JXVeinJavaAPI.veinFeatSize];
        this.score = 0;
    }
}
