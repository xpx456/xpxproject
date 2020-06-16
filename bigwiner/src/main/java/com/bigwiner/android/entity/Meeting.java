package com.bigwiner.android.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Meeting implements Parcelable {

    public String name = "";
    public String recordid = "";
    public String contractor = "";
    public String sponsor = "";
    public String companyid = "";
    public String prise1 = "";
    public String prise2 = "";
    public String address = "";
    public String timebegin = "";
    public String timeend = "";
    public String des = "";
    public String stute = "";
    public String joinCompanys = "";
    public String photo = "";
    public String contactor = "";
    public String phone = "";
    public boolean isjionin = false;
    public boolean issign = false;
    public int max = 1000;
    public int count = 567;
    public ArrayList<Company> companies = new ArrayList<Company>();
    public Meeting() {

    }

    protected Meeting(Parcel in) {
        name = in.readString();
        recordid = in.readString();
        contractor = in.readString();
        companyid = in.readString();
        prise1 = in.readString();
        prise2 = in.readString();
        address = in.readString();
        timebegin = in.readString();
        timeend = in.readString();
        des = in.readString();
        max = in.readInt();
        count = in.readInt();
        joinCompanys = in.readString();
        sponsor = in.readString();
        photo = in.readString();
        isjionin = Boolean.valueOf(in.readString());
        issign = Boolean.valueOf(in.readString());
        contactor = in.readString();
        phone = in.readString();
        stute = in.readString();
    }

    public static final Creator<Meeting> CREATOR = new Creator<Meeting>() {
        @Override
        public Meeting createFromParcel(Parcel in) {
            return new Meeting(in);
        }

        @Override
        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(recordid);
        dest.writeString(contractor);
        dest.writeString(companyid);
        dest.writeString(prise1);
        dest.writeString(prise2);
        dest.writeString(address);
        dest.writeString(timebegin);
        dest.writeString(timeend);
        dest.writeString(des);
        dest.writeInt(max);
        dest.writeInt(count);
        dest.writeString(joinCompanys);
        dest.writeString(sponsor);
        dest.writeString(photo);
        dest.writeString(String.valueOf(isjionin));
        dest.writeString(String.valueOf(issign));
        dest.writeString(contactor);
        dest.writeString(phone);
        dest.writeString(stute);
    }
}
