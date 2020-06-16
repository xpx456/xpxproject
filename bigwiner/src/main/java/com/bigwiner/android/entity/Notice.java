package com.bigwiner.android.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Notice implements Parcelable {

    public String classification = "";
    public String content = "";
    public String tital = "";
    public String time = "";
    public String uid = "";
    public String recordid = "";
    public int readcount = 0;


    public Notice() {

    }

    protected Notice(Parcel in) {
        classification = in.readString();
        content = in.readString();
        tital = in.readString();
        time = in.readString();
        uid = in.readString();
        recordid = in.readString();
        readcount = in.readInt();
    }

    public static final Creator<Notice> CREATOR = new Creator<Notice>() {
        @Override
        public Notice createFromParcel(Parcel in) {
            return new Notice(in);
        }

        @Override
        public Notice[] newArray(int size) {
            return new Notice[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(classification);
        dest.writeString(content);
        dest.writeString(tital);
        dest.writeString(time);
        dest.writeString(uid);
        dest.writeString(recordid);
        dest.writeInt(readcount);
    }
}
