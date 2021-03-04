package com.interskypad.entity;

import android.os.Parcel;
import android.os.Parcelable;

import intersky.apputils.CharacterParser;

public class Customer implements Parcelable {

    private String name = "";
    public String phone = "";
    public String mobil = "";
    public String mail = "";
    public String address = "";
    public String memo = "";
    public String mPingyin = "";
    public int type = 0;
    public boolean isSelect = false;

    public Customer() {

    }

    public Customer(String s) {
        type = 1;
        name = s;
        mPingyin = CharacterParser.getInstance().getSelling(this.name.toLowerCase());;
    }

    protected Customer(Parcel in) {
        name = in.readString();
        phone = in.readString();
        mobil = in.readString();
        mail = in.readString();
        address = in.readString();
        memo = in.readString();
        mPingyin = in.readString();
        type = in.readInt();
        isSelect = in.readByte() != 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        mPingyin = CharacterParser.getInstance().getSelling(this.name).toLowerCase();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(mobil);
        dest.writeString(mail);
        dest.writeString(address);
        dest.writeString(memo);
        dest.writeString(mPingyin);
        dest.writeInt(type);
        dest.writeByte((byte) (isSelect ? 1 : 0));
    }
}
