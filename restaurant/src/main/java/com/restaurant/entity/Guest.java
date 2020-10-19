package com.restaurant.entity;


import com.restaurant.model.bean.VeinInfo;

import java.util.concurrent.ConcurrentHashMap;

import intersky.apputils.AppUtils;

public class Guest {

    public Guest() {
        rid = AppUtils.getguid();
    }
    public void set(GuestFinger guestFinger)
    {
        rid = guestFinger.rid;
        name = guestFinger.name;
        type = guestFinger.type;
        licence = guestFinger.licence;
        cancard = guestFinger.canCard;
        canfinger = guestFinger.canFinger;
    }

    public String rid = "";
    public String time = "";
    public String name = "";
    public String type = "";
    public String sex = "";
    public String count = "";
    public String cancard = "";
    public String canfinger = "";
    public String licence = "";
    public String items = "";
    public String mobil = "";
    public String address = "";
    public String utime = "";
    public ConcurrentHashMap<String, VeinInfo> fingers = new ConcurrentHashMap<>();

}
