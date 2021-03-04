package com.dk.dkhome.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.dk.dkhome.view.DkhomeApplication;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import intersky.apputils.AppUtils;

public class User implements Parcelable{

    public String uid = "";
    public String name = "";
    public String age = "";
    public int sex = 0;
    public String headpath = "";
    public String bgpath = "";
    public int tall = 0;
    public int lastweight = 0;
    public int eat = 0;
    public int goal = 0;
    public String creat = "";
    public boolean islogin = false;

    public User() {

    }

    protected User(Parcel in) {
        uid = in.readString();
        name = in.readString();
        age = in.readString();
        sex = in.readInt();
        headpath = in.readString();
        bgpath = in.readString();
        tall = in.readInt();
        lastweight = in.readInt();
        eat = in.readInt();
        goal = in.readInt();
        creat = in.readString();
        islogin = in.readByte() != 0;
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeString(age);
        dest.writeInt(sex);
        dest.writeString(headpath);
        dest.writeString(bgpath);
        dest.writeInt(tall);
        dest.writeInt(lastweight);
        dest.writeInt(eat);
        dest.writeInt(goal);
        dest.writeString(creat);
        dest.writeByte((byte) (islogin ? 1 : 0));
    }
}
