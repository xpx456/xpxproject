package intersky.schedule.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {

    public String mTime = "";
    public String mContent = "";
    public int mStatus = 0;
    public String mScheduleid = "";
    public String mUserid = "";
    public String mCompanyid = "";
    public String mReminds = "";


    public Event(String mTime, String mContent, int mStatus, String mScheduleid, String mUserid, String mCompanyid, String mReminds)
    {
        this.mTime = mTime;
        this.mContent = mContent;
        this.mStatus = mStatus;
        this.mScheduleid = mScheduleid;
        this.mUserid = mUserid;
        this.mCompanyid = mCompanyid;
        this.mReminds = mReminds;
    }

    public Event() {

    }

  @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTime);
        dest.writeString(mContent);
        dest.writeInt(mStatus);
        dest.writeString(mScheduleid);
        dest.writeString(mUserid);
        dest.writeString(mCompanyid);
        dest.writeString(mReminds);
    }



    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in.readString(), in.readString(), in.readInt(), in.readString(),
                    in.readString(), in.readString(), in.readString());
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

}
