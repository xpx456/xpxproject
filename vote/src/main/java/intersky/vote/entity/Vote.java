package intersky.vote.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xpx on 2017/6/21.
 */

public class Vote implements Parcelable {

    public String voteid = "";
    public String creatTime = "";
    public String endTime = "";
    public String mContent = "";
    public String userid = "";
    public String userName = "";
    public int is_incognito = 0;
    public int is_close = 0;
    public int type = 0;
    public int personCount = 0;
    public String myvoteid = "";
    public String selectJson = "";
    public String voteJson = "";
    public String replyJson = "";
    public String voterid = "";
    public int totalCount = 0;
    public boolean visiable = false;
    public Vote(String voteid, String userid, String creatTime, String endTime, String mContent, int is_incognito, int is_close,int type)
    {
        this.voteid = voteid;
        this.creatTime = creatTime;
        this.endTime = endTime;
        this.mContent = mContent;
        this.userid = userid;
        this.is_incognito = is_incognito;
        this.is_close = is_close;
        this.type = type;
    }

    public Vote() {

    }

    protected Vote(Parcel in) {
        voteid = in.readString();
        creatTime = in.readString();
        endTime = in.readString();
        mContent = in.readString();
        userid = in.readString();
        userName = in.readString();
        is_incognito = in.readInt();
        is_close = in.readInt();
        type = in.readInt();
        personCount = in.readInt();
        myvoteid = in.readString();
        selectJson = in.readString();
        replyJson = in.readString();
        voterid = in.readString();
        totalCount = in.readInt();
        visiable = Boolean.valueOf(in.readString());
        voteJson = in.readString();
    }

    public static final Creator<Vote> CREATOR = new Creator<Vote>() {
        @Override
        public Vote createFromParcel(Parcel in) {
            return new Vote(in);
        }

        @Override
        public Vote[] newArray(int size) {
            return new Vote[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(voteid);
        dest.writeString(creatTime);
        dest.writeString(endTime);
        dest.writeString(mContent);
        dest.writeString(userid);
        dest.writeString(userName);
        dest.writeInt(is_incognito);
        dest.writeInt(is_close);
        dest.writeInt(type);
        dest.writeInt(personCount);
        dest.writeString(myvoteid);
        dest.writeString(selectJson);
        dest.writeString(replyJson);
        dest.writeString(voterid);
        dest.writeInt(totalCount);
        dest.writeString(String.valueOf(visiable));
        dest.writeString(voteJson);
    }
}
