package com.interskypad.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import intersky.apputils.TimeUtils;

public class Order implements Parcelable {


    public Boolean isselect = false;
    public Boolean issubmit = false;
    public String id = "";
    public String time = "";
    public String memo = "";
    public String c_name = "";
    public String c_phone = "";
    public String c_mobil = "";
    public String c_address = "";
    public String c_mail = "";
    public String c_memo = "";
    public String productids = "";
    public boolean isedit = false;
    public HashMap<String,Catalog> hashcatalogs = new HashMap<String,Catalog>();
    public int count = 0;
//    public ArrayList<Catalog> catalogs = new ArrayList<Catalog>();

    protected Order(Parcel in) {
        id = in.readString();
        time = in.readString();
        memo = in.readString();
        c_name = in.readString();
        c_phone = in.readString();
        c_mobil = in.readString();
        c_address = in.readString();
        c_mail = in.readString();
        c_memo = in.readString();
        productids = in.readString();
    }

    public Order() {
        id = TimeUtils.getTimeUid();
        time = TimeUtils.getDate()+" "+TimeUtils.getTime();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(time);
        dest.writeString(memo);
        dest.writeString(c_name);
        dest.writeString(c_phone);
        dest.writeString(c_mobil);
        dest.writeString(c_address);
        dest.writeString(c_mail);
        dest.writeString(c_memo);
        dest.writeString(productids);
    }

    public void setProductidsId() {
        productids = "";
        for(HashMap.Entry<String, Catalog> entry: hashcatalogs.entrySet())
        {

            if(productids.length() == 0)
            {
                productids += entry.getKey();
            }
            else
            {
                productids += ","+entry.getKey();
            }
        }
    }

    public void put(Catalog catalog)
    {
        if(hashcatalogs.containsKey(catalog.mSerialID))
        {

        }
        else
        {
            count++;
        }
        hashcatalogs.put(catalog.mSerialID,catalog);
    }

    public Catalog get(String key) {
        return  hashcatalogs.get(key);
    }

    public void remove(String key) {
        if(hashcatalogs.containsKey(key))
        {
            hashcatalogs.remove(key);
            count--;
        }
    }

    public void addAll(List<Catalog> catalogs) {
        for(int i = 0 ; i < catalogs.size() ; i++)
        {
            put(catalogs.get(i));
        }
    }

    public ArrayList<Catalog> getAll() {
        ArrayList<Catalog> catalogs = new ArrayList<Catalog>();
        for(HashMap.Entry<String, Catalog> entry: hashcatalogs.entrySet())
        {
            catalogs.add(entry.getValue());
        }
        return catalogs;
    }
}
