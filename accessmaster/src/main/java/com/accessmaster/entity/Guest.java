package com.accessmaster.entity;

import java.util.HashMap;

import intersky.apputils.AppUtils;

public class Guest{

    public Guest() {
        rid = AppUtils.getguid();
    }
    public void set(GuestFinger guestFinger)
    {
        rid = guestFinger.rid;
        name = guestFinger.name;
        type = guestFinger.type;
        licence = guestFinger.licence;
        fingers.put(guestFinger.finger,guestFinger.rid);
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
    public HashMap<String,String> fingers = new HashMap<String,String>();

}
