package com.exhibition.entity;

import com.finger.entity.Finger;

import java.util.ArrayList;
import java.util.Random;

import intersky.apputils.AppUtils;
import intersky.apputils.SystemUtil;
import intersky.apputils.TimeUtils;

public class Guest {

    public Guest() {
        Random random = new Random();
        rid = TimeUtils.getDateAndTimeCode()+ String.format("%05d",random.nextInt(99999));
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
