package intersky.appbase.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;


public class Attachment implements Parcelable {

    public static final int STATA_NONE = 0;
    public static final int STATA_NOSTART = 1;
    public static final int STATA_DWONLODING = 2;
    public static final int STATA_DOWNLOADFIAL = 3;
    public static final int STATA_DOWNLOADFINISH = 4;

    public String mRecordid = "";
    public String mName = "";
    public String mPath = "";
    public String mUrl = "";
    public String mUrltemp = "";
    public long mSize = 0;
    public long mFinishSize = 0;
    public String mDete = "";
    public String mPath2 = "";
    public String taskattid = "";
    public String taskuserid = "";
    public int stata = STATA_NONE;
    public JSONObject mailUploadJson = null;
    public boolean select = false;
    public Attachment() {

    };

    public Attachment(String mRecordid, String mName, String mPath, String mUrl, long mSize, long mFinishSize, String mDete) {
        this.mRecordid = mRecordid;
        this.mName = mName;
        this.mPath = mPath;
        this.mUrl = mUrl;
        this.mSize = mSize;
        this.mFinishSize = mFinishSize;
        this.mDete = mDete;
    }

    public Attachment(String mRecordid, String mName, String mPath, String mUrl, long mSize, long mFinishSize, String mDete,String mUrltemp) {
        this.mRecordid = mRecordid;
        this.mName = mName;
        this.mPath = mPath;
        this.mUrl = mUrl;
        this.mSize = mSize;
        this.mFinishSize = mFinishSize;
        this.mDete = mDete;
        this.mUrltemp = mUrltemp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mRecordid);
        dest.writeString(mName);
        dest.writeString(mPath);
        dest.writeString(mUrl);
        dest.writeLong(mSize);
        dest.writeLong(mFinishSize);
        dest.writeString(mDete);
        dest.writeString(mUrltemp);
    }



    public static final Parcelable.Creator<Attachment> CREATOR = new Parcelable.Creator<Attachment>() {
        public Attachment createFromParcel(Parcel in) {
            return new Attachment(in.readString(), in.readString(), in.readString(), in.readString(),
                    in.readLong(), in.readLong(), in.readString(), in.readString());
        }

        public Attachment[] newArray(int size) {
            return new Attachment[size];
        }
    };
}
