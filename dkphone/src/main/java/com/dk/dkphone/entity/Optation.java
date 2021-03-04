package com.dk.dkphone.entity;

import android.os.Parcel;
import android.os.Parcelable;

import intersky.apputils.AppUtils;

public class Optation implements Parcelable {

    public String oid = AppUtils.getguid();
    public String name = "";
    public String data = "[]";

    public Optation() {

    }

    protected Optation(Parcel in) {
        oid = in.readString();
        name = in.readString();
        data = in.readString();
    }

    public static final Creator<Optation> CREATOR = new Creator<Optation>() {
        @Override
        public Optation createFromParcel(Parcel in) {
            return new Optation(in);
        }

        @Override
        public Optation[] newArray(int size) {
            return new Optation[size];
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
        parcel.writeString(data);
    }
}
