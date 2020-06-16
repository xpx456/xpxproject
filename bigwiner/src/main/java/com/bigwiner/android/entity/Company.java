package com.bigwiner.android.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.ModuleDetial;

public class Company implements Parcelable {

    public String name = "";
    public String statue = "";
    public String id = "";
    public String country = "";
    public String city = "";
    public String province = "";
    public String ename = "";
    public String phone = "";
    public String fax = "";
    public String mail = "";
    public String web = "";
    public String characteristic = "";
    public String address = "";
    public int leavel = 0;
    public String jointype = "";
    public String icon = "";
    public String bg = "";
    public String marginamount = "";
    public String issail = "";
    public String ischarge = "";
    public String sailno = "";
    public String taxno = "";
    public int year = 0;
    public ModuleDetial moduleDetial = new ModuleDetial();
    public ArrayList<Contacts> contacts = new ArrayList<Contacts>();

    public Company() {

    }

    public void copy(Company company) {
        this.name = company.name;
        this.statue = company.statue;
        this.id = company.id;
        this.country = company.country;
        this.city = company.city;
        this.province = company.province;
        this.ename = company.ename;
        this.phone = company.phone;
        this.fax = company.fax;
        this.mail = company.mail;
        this.web = company.web;
        this.characteristic = company.characteristic;
        this.address = company.address;
        this.leavel = company.leavel;
        this.jointype = company.jointype;
        this.icon = company.icon;
        this.bg = company.bg;
        this.marginamount = company.marginamount;
        this.issail = company.issail;
        this.ischarge = company.ischarge;
        this.sailno = company.sailno;
        this.taxno = company.taxno;
        this.year = company.year;
    }

    protected Company(Parcel in) {
        name = in.readString();
        id = in.readString();
        country = in.readString();
        city = in.readString();
        phone = in.readString();
        fax = in.readString();
        mail = in.readString();
        characteristic = in.readString();
        address = in.readString();
        icon = in.readString();
        ename = in.readString();
        issail = in.readString();
        ischarge = in.readString();
        marginamount = in.readString();
        province = in.readString();
        bg = in.readString();
        sailno = in.readString();
        taxno = in.readString();
        web = in.readString();
        statue = in.readString();
        year = in.readInt();
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(phone);
        dest.writeString(fax);
        dest.writeString(mail);
        dest.writeString(characteristic);
        dest.writeString(address);
        dest.writeString(icon);
        dest.writeString(ename);
        dest.writeString(issail);
        dest.writeString(ischarge);
        dest.writeString(marginamount);
        dest.writeString(province);
        dest.writeString(bg);
        dest.writeString(sailno);
        dest.writeString(taxno);
        dest.writeString(web);
        dest.writeString(statue);
        dest.writeInt(year);
    }
}
