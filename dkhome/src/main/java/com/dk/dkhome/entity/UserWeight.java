package com.dk.dkhome.entity;

import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;

public class UserWeight {

    public UserWeight()
    {

    }

    public UserWeight(String uid,String weight)
    {
        this.uid = uid;
        this.date = TimeUtils.getDate();
        this.weight = weight;
    }
    public String id = AppUtils.getguid();
    public String uid = "";
    public String date = "";
    public String weight = "";

}
