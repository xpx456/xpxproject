package com.dk.dkhome.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import org.json.JSONArray;

import java.util.ArrayList;

import intersky.apputils.AppUtils;
import intersky.mywidget.ClendarEvent;

public class Course extends ClendarEvent implements Parcelable {

    public String oid = AppUtils.getguid();
    public String name = "";
    public int during = 0;
    public int current = 0;
    public String creat = "";
    public String userid = "";
    public String path = "";
    public String url = "";
    public String videoname = "";
    public double dis = 0;
    public double totalCarl = 0;
    public int videotime = 0;
    public String img = "";
    public String time = "";
    public String cid = "";
    public View view;
    public JSONArray leavels = new JSONArray();
    public JSONArray herts = new JSONArray();
    public JSONArray speeds = new JSONArray();
    public JSONArray rpm = new JSONArray();
    public JSONArray carl = new JSONArray();
    public double nowhert = 0;
    public double nowspeed = 0;
    public double topSpeed = 0;

    public Course() {

    }

    public void setPlan(Course course) {
        oid = course.oid;
        name = course.name;
        during = course.during;
        current = course.current;
        creat = course.creat;
        userid = course.userid;
        path = course.path;
        url = course.url;
        videoname = course.videoname;
        leavels = course.leavels;
        herts = course.herts;
        speeds = course.speeds;
        rpm = course.rpm;
        carl = course.carl;
        cid = course.cid;
        img = course.img;
        videotime = course.videotime;
        dis = course.dis;
        totalCarl = course.totalCarl;
    }

    protected Course(Parcel in) {
        oid = in.readString();
        name = in.readString();
        during = in.readInt();
        current = in.readInt();
        creat = in.readString();
        userid = in.readString();
        path = in.readString();
        url = in.readString();
        videoname = in.readString();
        EquipData.praseStringData(this,in.readString());
        cid = in.readString();
        img = in.readString();
        videotime = in.readInt();
        dis = in.readDouble();
        totalCarl = in.readDouble();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(oid);
        parcel.writeString(name);
        parcel.writeInt(during);
        parcel.writeInt(current);
        parcel.writeString(creat);
        parcel.writeString(userid);
        parcel.writeString(path);
        parcel.writeString(url);
        parcel.writeString(videoname);
        parcel.writeString(EquipData.initStringData(this));
        parcel.writeString(cid);
        parcel.writeString(img);
        parcel.writeInt(videotime);
        parcel.writeDouble(dis);
        parcel.writeDouble(totalCarl);
    }

    public double getAvgSpeed() {
        if(current > 0)
        {
            return dis/current;
        }
        else
        {
            return 0;
        }
    }
}
