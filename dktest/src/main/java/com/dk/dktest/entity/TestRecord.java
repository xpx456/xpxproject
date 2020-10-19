package com.dk.dktest.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class TestRecord implements Parcelable {

    public String name = "";
    public String day = "";
    public String time = "";
    public String rid = "";
    public String data = "";
    public int during = 0;

    public TestRecord() {

    }


    protected TestRecord(Parcel in) {
        name = in.readString();
        day = in.readString();
        time = in.readString();
        rid = in.readString();
        data = in.readString();
        during = in.readInt();
    }

    public static final Creator<TestRecord> CREATOR = new Creator<TestRecord>() {
        @Override
        public TestRecord createFromParcel(Parcel in) {
            return new TestRecord(in);
        }

        @Override
        public TestRecord[] newArray(int size) {
            return new TestRecord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(day);
        parcel.writeString(time);
        parcel.writeString(rid);
        parcel.writeString(data);
        parcel.writeInt(during);
    }
}
