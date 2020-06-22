package com.exhibition.entity;

import com.finger.entity.Finger;

import java.util.ArrayList;

import intersky.apputils.AppUtils;

public class Guest {

    public Guest() {
        rid = AppUtils.getguid();
    }

    public String rid = "";
    public String time = "";
    public String name = "";
    public String type = "";
    public String sex = "";
    public String count = "";
    public String car = "";
    public String card = "";
    public String licence = "";
    public String items = "";
    public String mobil = "";
    public String address = "";
    public String utime = "";
    public ArrayList<Finger> fingers = new ArrayList<>();

}
