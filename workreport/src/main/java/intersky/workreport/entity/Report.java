package intersky.workreport.entity;

import android.os.Parcel;
import android.os.Parcelable;

import intersky.appbase.bus.Bus;
import intersky.workreport.WorkReportManager;

/**
 * Created by xpx on 2016/10/12.
 */

public class Report implements Parcelable {

    public String mRecordid = "";
    public String mBegainTime = "";
    public String mEndTime = "";
    public String mCreatTime = "";
    public int mType = 1;
    public String mUserid = "";
    public String mSenderto = "";
    public boolean isread = false;
    public String userName;
    public String mComplete = "";
    public String mSummery = "";
    public String nextProject = "";
    public String mHelp = "";
    public String mRemark = "";
    public String mAttence = "";
    public String mSenders = "";
    public String mCopyers = "";
    public int mNtype = 1;
    public String replyJson = "";
    public String attachJson = "";
    public boolean select = false;
    public Report(String mRecordid, String mBegainTime, String mEndTime, String mCreatTime, String mUserid, int mType, String mSenderto) {
        this.mRecordid = mRecordid;
        this.mBegainTime = mBegainTime;
        this.mEndTime = mEndTime;
        this.mCreatTime = mCreatTime;
        this.mUserid = mUserid;
        this.mType = mType;
        this.mSenderto = mSenderto;
        this.userName = (String) Bus.callData(WorkReportManager.getInstance().context,"chat/getContactName",mUserid);
    }

    public Report(String mRecordid, String mBegainTime, String mEndTime, String mCreatTime, String mUserid, String mSenderto, boolean isread) {
        this.mRecordid = mRecordid;
        this.mBegainTime = mBegainTime;
        this.mEndTime = mEndTime;
        this.mCreatTime = mCreatTime;
        this.mUserid = mUserid;
        this.mSenderto = mSenderto;
        this.isread = isread;
        this.userName = (String) Bus.callData(WorkReportManager.getInstance().context,"chat/getContactName",mUserid);
    }

    public Report(String mRecordid, String mBegainTime, String mEndTime, String mCreatTime, String mUserid, int mType, String mSenderto, boolean isread) {
        this.mRecordid = mRecordid;
        this.mBegainTime = mBegainTime;
        this.mEndTime = mEndTime;
        this.mCreatTime = mCreatTime;
        this.mUserid = mUserid;
        this.mType = mType;
        this.mSenderto = mSenderto;
        this.isread = isread;
        this.userName = (String) Bus.callData(WorkReportManager.getInstance().context,"chat/getContactName",mUserid);
    }

    public Report(String mRecordid, String mBegainTime, String mEndTime, String mCreatTime, String mUserid, int mType, String mSenderto, boolean isread
            , String mComplete, String mSummery, String nextProject, String mHelp, String mRemark, String mAttence, String mSenders, String mCopyers, int mNtype
            , String userName,String replyJson,String attachJson) {
        this.mRecordid = mRecordid;
        this.mBegainTime = mBegainTime;
        this.mEndTime = mEndTime;
        this.mCreatTime = mCreatTime;
        this.mUserid = mUserid;
        this.mType = mType;
        this.mSenderto = mSenderto;
        this.isread = isread;
        this.mComplete = mComplete;
        this.mSummery = mSummery;
        this.nextProject = nextProject;
        this.mHelp = mHelp;
        this.mRemark = mRemark;
        this.mAttence = mAttence;
        this.mSenders = mSenders;
        this.mCopyers = mCopyers;
        this.mNtype = mNtype;
        this.userName = userName;
        this.replyJson = replyJson;
        this.attachJson = attachJson;
        this.userName = (String) Bus.callData(WorkReportManager.getInstance().context,"chat/getContactName",mRecordid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(mRecordid);
        dest.writeString(mBegainTime);
        dest.writeString(mEndTime);
        dest.writeString(mCreatTime);
        dest.writeString(mUserid);
        dest.writeInt(mType);
        dest.writeString(mSenderto);
        dest.writeString(String.valueOf(isread));
        dest.writeString(mComplete);
        dest.writeString(mSummery);
        dest.writeString(nextProject);
        dest.writeString(mHelp);
        dest.writeString(mRemark);
        dest.writeString(mAttence);
        dest.writeString(mSenders);
        dest.writeString(mCopyers);
        dest.writeInt(mNtype);
        dest.writeString(userName);
        dest.writeString(replyJson);
        dest.writeString(attachJson);
    }

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        public Report createFromParcel(Parcel in) {
            return new Report(in.readString(), in.readString(), in.readString(), in.readString(),
                    in.readString(), in.readInt(), in.readString(), Boolean.valueOf(in.readString()),
                    in.readString(),in.readString(),in.readString(),in.readString(),in.readString(),
                    in.readString(),in.readString(),in.readString(),in.readInt(),in.readString(),in.readString()
                    ,in.readString());
        }

        public Report[] newArray(int size) {
            return new Report[size];
        }
    };


    public Report() {

    }


}