package com.dk.dkhome.entity;

import android.view.View;

import intersky.apputils.AppUtils;

public class FoodType {

    public String uid = AppUtils.getguid();
    public String name = "";
    public View view;
    public boolean isselect = false;
    public FoodType() {

    }

    public FoodType(String name) {
        this.name = name;

    }
}
