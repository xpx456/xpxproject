package intersky.leave.entity;

import android.os.Parcel;
import android.os.Parcelable;

import intersky.appbase.bus.Bus;
import intersky.apputils.TimeUtils;
import intersky.leave.LeaveManager;

/**
 * Created by xpx on 2017/5/22.
 */

public class Leave implements Parcelable {

    public String lid = "";
    public String uid = "";
    public String cid = "";
    public String start = TimeUtils.getDateAndTime();
    public String end = TimeUtils.getDateAndTime(0,0,1,0,0);
    public String count = "";
    public String senders = "";
    public String copyers = "";
    public String nowapprover = "";
    public String leave_type_id = "-1";
    public int is_approval = 0;
    public String create_time = "";
    public String content = "";
    public String name = "";
    public boolean isread = true;
    public int type = 0;
    public String attachs = "";
    public String approverjson = "";
    public String attachjson = "";
    public Leave(String lid, String uid, String cid, String start, String end, String count, String senders, String copyers, String nowapprover
    , String leave_type_id, int is_approval, String create_time, String content)
    {
        this.lid = lid;
        this.uid = uid;
        this.cid = cid;
        this.start = start;
        this.end = end;
        this.count = count;
        this.senders = senders;
        this.copyers = copyers;
        this.nowapprover = nowapprover;
        this.leave_type_id = leave_type_id;
        this.is_approval = is_approval;
        this.create_time = create_time;
        this.content = content;
        this.name = (String) Bus.callData(LeaveManager.getInstance().context,"chat/getContactName",this.uid);
    }

    public Leave() {
        if(LeaveManager.getInstance().defaultType != null)
        {
            leave_type_id = LeaveManager.getInstance().defaultType.mId;
        }
    }

    protected Leave(Parcel in) {
        lid = in.readString();
        uid = in.readString();
        cid = in.readString();
        start = in.readString();
        end = in.readString();
        count = in.readString();
        senders = in.readString();
        copyers = in.readString();
        nowapprover = in.readString();
        leave_type_id = in.readString();
        is_approval = in.readInt();
        create_time = in.readString();
        content = in.readString();
        name = in.readString();
        isread = Boolean.valueOf(in.readString());
        type = in.readInt();
        attachs = in.readString();
        approverjson = in.readString();
        attachjson = in.readString();
    }

    public static final Creator<Leave> CREATOR = new Creator<Leave>() {
        @Override
        public Leave createFromParcel(Parcel in) {
            return new Leave(in);
        }

        @Override
        public Leave[] newArray(int size) {
            return new Leave[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lid);
        dest.writeString(uid);
        dest.writeString(cid);
        dest.writeString(start);
        dest.writeString(end);
        dest.writeString(count);
        dest.writeString(senders);
        dest.writeString(copyers);
        dest.writeString(nowapprover);
        dest.writeString(leave_type_id);
        dest.writeInt(is_approval);
        dest.writeString(create_time);
        dest.writeString(content);
        dest.writeString(name);
        dest.writeString(String.valueOf(isread));
        dest.writeInt(type);
        dest.writeString(attachs);
        dest.writeString(approverjson);
        dest.writeString(attachjson);
    }
}
