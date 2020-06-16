package com.bigwiner.android.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class SourceData implements Parcelable {

    public String id = "";
    public String name = "";
    public String type = "";
    public String area = "";
    public String port = "";
    public String memo = "";
    public String start = "";
    public String end = "";
    public String lable = "";
    public String publicetime = "";
    public String collectcount = "";
    public String viewcount = "";
    public String sourcetype = "";
    public String mobile = "";
    public String photo = "";
    public String userid = "";
    public String username = "";
    public String company = "";
    public String position = "";
    public String state = "";
    public int iscollcet = 0;
    public boolean isfriend = false;

    public SourceData() {

    }
    public SourceData(String type) {
        this.sourcetype = type;
    }

    public void updata(SourceData sourceData) {
        this.id = sourceData.id;
        this.name = sourceData.name;
        this.type = sourceData.type;
        this.area = sourceData.area;
        this.port = sourceData.port;
        this.memo = sourceData.memo;
        this.start = sourceData.start;
        this.end = sourceData.end;
        this.lable = sourceData.lable;
        this.publicetime = sourceData.publicetime;
        this.collectcount = sourceData.collectcount;
        this.viewcount = sourceData.viewcount;
        this.sourcetype = sourceData.sourcetype;
        this.mobile = sourceData.mobile;
        this.photo = sourceData.photo;
        this.userid = sourceData.userid;
        this.username = sourceData.username;
        this.company = sourceData.company;
        this.position = sourceData.position;
        this.iscollcet = sourceData.iscollcet;
        this.isfriend = sourceData.isfriend;
        this.state = sourceData.state;

    }

    protected SourceData(Parcel in) {
        id = in.readString();
        name = in.readString();
        type = in.readString();
        area = in.readString();
        port = in.readString();
        memo = in.readString();
        start = in.readString();
        end = in.readString();
        lable = in.readString();
        publicetime = in.readString();
        collectcount = in.readString();
        viewcount = in.readString();
        sourcetype = in.readString();
        mobile = in.readString();
        photo = in.readString();
        userid = in.readString();
        username = in.readString();
        company = in.readString();
        position = in.readString();
        iscollcet = in.readInt();
        isfriend = in.readByte() != 0;
        state = in.readString();
    }

    public static final Creator<SourceData> CREATOR = new Creator<SourceData>() {
        @Override
        public SourceData createFromParcel(Parcel in) {
            return new SourceData(in);
        }

        @Override
        public SourceData[] newArray(int size) {
            return new SourceData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(area);
        dest.writeString(port);
        dest.writeString(memo);
        dest.writeString(start);
        dest.writeString(end);
        dest.writeString(lable);
        dest.writeString(publicetime);
        dest.writeString(collectcount);
        dest.writeString(viewcount);
        dest.writeString(sourcetype);
        dest.writeString(mobile);
        dest.writeString(photo);
        dest.writeString(userid);
        dest.writeString(username);
        dest.writeString(company);
        dest.writeString(position);
        dest.writeInt(iscollcet);
        dest.writeByte((byte) (isfriend ? 1 : 0));
        dest.writeString(state);
    }
}
