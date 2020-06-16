package intersky.notice.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xpx on 2017/5/31.
 */

public class Notice implements Parcelable {

    public String nid = "";
    public String cid = "";
    public String uid = "";
    public String title = "";
    public String content = "";
    public String attachment = "";
    public String sender_id = "";
    public String dept_name = "";
    public String create_time = "";
    public String update_time = "";
    public String username = "";
    public int isread = 0;
    public String read_id = "";
    public String unread_id = "";
    public String replyJson = "";
    public String attachJson = "";
    public Notice(String nid, String uid, String cid, String title, String content, String attachment
            , String sender_id, String dept_name, String create_time, String update_time)
    {
        this.nid = nid;
        this.cid = cid;
        this.uid = uid;
        this.title = title;
        this.content = content;
        this.attachment = attachment;
        this.sender_id = sender_id;
        this.dept_name = dept_name;
        this.create_time = create_time;
        this.update_time = update_time;

    }

    public Notice() {

    }

    protected Notice(Parcel in) {
        nid = in.readString();
        cid = in.readString();
        uid = in.readString();
        title = in.readString();
        content = in.readString();
        attachment = in.readString();
        sender_id = in.readString();
        dept_name = in.readString();
        create_time = in.readString();
        update_time = in.readString();
        username = in.readString();
        isread = in.readInt();
        read_id = in.readString();
        unread_id = in.readString();
        replyJson = in.readString();
        attachJson = in.readString();
    }

    public static final Creator<Notice> CREATOR = new Creator<Notice>() {
        @Override
        public Notice createFromParcel(Parcel in) {
            return new Notice(in);
        }

        @Override
        public Notice[] newArray(int size) {
            return new Notice[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nid);
        dest.writeString(cid);
        dest.writeString(uid);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(attachment);
        dest.writeString(sender_id);
        dest.writeString(dept_name);
        dest.writeString(create_time);
        dest.writeString(update_time);
        dest.writeString(username);
        dest.writeInt(isread);
        dest.writeString(read_id);
        dest.writeString(unread_id);
        dest.writeString(replyJson);
        dest.writeString(attachJson);
    }
}
