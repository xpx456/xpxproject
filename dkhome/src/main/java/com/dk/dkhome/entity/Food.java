package com.dk.dkhome.entity;

import android.view.View;

import intersky.apputils.AppUtils;

public class Food {

    public String uid = AppUtils.getguid();
    public String name = "";
    public String typename = "";
    public String typeid = "";
    public int carl = 0;
    public int weight = 0;
    public int count = 0;
    public View view;

    public Food() {

    }

    public Food(String name,String typename,int carl,int weight,String typeid) {
        this.name = name;
        this.typename = typename;
        this.carl = carl;
        this.weight = weight;
        this.typeid = typeid;
    }
}
