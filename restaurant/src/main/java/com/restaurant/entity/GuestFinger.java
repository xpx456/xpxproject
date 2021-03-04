package com.restaurant.entity;

import android.os.Parcel;
import android.os.Parcelable;

import intersky.apputils.AppUtils;

public class GuestFinger implements Parcelable {

    public GuestFinger() {
        rid = AppUtils.getguid();
    }

    public String rid = "";
    public String type = "";
    public String name = "";
    public String finger = "";
    public String licence = "";
    public String canCard = "";
    public String canFinger = "";
    public String fingerName = "";


    protected GuestFinger(Parcel in) {
        rid = in.readString();
        type = in.readString();
        name = in.readString();
        finger = in.readString();
        licence = in.readString();
        canCard = in.readString();
        canFinger = in.readString();
        fingerName = in.readString();
    }

    public static final Creator<GuestFinger> CREATOR = new Creator<GuestFinger>() {
        @Override
        public GuestFinger createFromParcel(Parcel in) {
            return new GuestFinger(in);
        }

        @Override
        public GuestFinger[] newArray(int size) {
            return new GuestFinger[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rid);
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(finger);
        dest.writeString(licence);
        dest.writeString(canCard);
        dest.writeString(canFinger);
        dest.writeString(fingerName);
    }
}
