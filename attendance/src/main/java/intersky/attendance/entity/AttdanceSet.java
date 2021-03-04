package intersky.attendance.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xpx on 2017/5/19.
 */

public class AttdanceSet implements Parcelable {

    public String aId = "";
    public String uId = "";
    public String cId = "";
    public String name = "";
    public String start = "";
    public String end = "";
    public String day = "";
    public String dayid = "";
    public String copyer = "";
    public double x = 0;
    public double y = 0;
    public AttdanceSet(String aId, String uId, String cId, String name, String start, String end, String day, String dayid)
    {
      this.aId = aId;
        this.uId = uId;
        this.cId = cId;
        this.name = name;
        this.start = start;
        this.end = end;
        this.day = day;
        this.dayid = dayid;
    }

    protected AttdanceSet(Parcel in) {
        aId = in.readString();
        uId = in.readString();
        cId = in.readString();
        name = in.readString();
        start = in.readString();
        end = in.readString();
        day = in.readString();
        dayid = in.readString();
        copyer = in.readString();
        x = in.readDouble();
        y = in.readDouble();
    }

    public AttdanceSet()
    {

    }

    public static final Creator<AttdanceSet> CREATOR = new Creator<AttdanceSet>() {
        @Override
        public AttdanceSet createFromParcel(Parcel in) {
            return new AttdanceSet(in);
        }

        @Override
        public AttdanceSet[] newArray(int size) {
            return new AttdanceSet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(aId);
        dest.writeString(uId);
        dest.writeString(cId);
        dest.writeString(name);
        dest.writeString(start);
        dest.writeString(end);
        dest.writeString(day);
        dest.writeString(dayid);
        dest.writeString(copyer);
        dest.writeDouble(x);
        dest.writeDouble(y);
    }
}
