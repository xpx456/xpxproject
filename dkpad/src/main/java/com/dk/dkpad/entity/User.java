package com.dk.dkpad.entity;

import android.os.Parcel;
import android.os.Parcelable;

import intersky.apputils.AppUtils;

public class User implements Parcelable {

    public String uid = AppUtils.getguid();
    public String wid = "";
    public String name = "";
    public String age = "";
    public String sex = "";
    public String toll = "";
    public String headpath = "";
    public boolean select = false;

    public User()
    {

    }

    protected User(Parcel in) {
        uid = in.readString();
        wid = in.readString();
        name = in.readString();
        age = in.readString();
        sex = in.readString();
        toll = in.readString();
        headpath = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeString(wid);
        parcel.writeString(name);
        parcel.writeString(age);
        parcel.writeString(sex);
        parcel.writeString(toll);
        parcel.writeString(headpath);
    }
}
