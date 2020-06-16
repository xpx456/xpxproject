package intersky.appbase.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;

import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;

public class Conversation  implements Parcelable {

    public static final String CONVERSATION_TYPE_MESSAGE = "msg_message";
    public static final String CONVERSATION_TYPE_NOTICE = "msg_notices";
    public static final String CONVERSATION_TYPE_TASK = "msg_task";
    public static final String CONVERSATION_TYPE_LEAVE = "msg_leave";
    public static final String CONVERSATION_TYPE_REPORT = "msg_report";
    public static final String CONVERSATION_TYPE_SCHDULE = "msg_schdule";
    public static final String CONVERSATION_TYPE_VOTE = "msg_vote";
    public static final String CONVERSATION_TYPE_GROP_MESSAGE = "msg_grop_message";
    public static final String CONVERSATION_TYPE_IWEB = "msg_iweb";
    public static final String CONVERSATION_TYPE_IWEB_MAIL = "msg_iweb_mail";
    public static final String CONVERSATION_TYPE_IWEB_APPROVE = "msg_iweb_approve";
    public static final String CONVERSATION_TYPE_NEWS = "msg_news";
    public static final String CONVERSATION_TYPE_MEETING = "msg_meeting";
    public static final String CONVERSATION_TYPE_RESOURCE = "msg_resource";
    public static final String CONVERSATION_TYPE_TITLE = "msg_title";
    public static final String CONVERSATION_TYPE_CONTACT = "msg_contact";
    public static final String CONVERSATION_TYPE_COMPANY = "msg_company";
    public static final String CONVERSATION_TYPE_DOCUMENT= "msg_document";

    public static final int MESSAGE_TYPE_NOMAL = 0;
    public static final int MESSAGE_TYPE_VOICE = 2;
    public static final int MESSAGE_TYPE_IMAGE = 3;
    public static final int MESSAGE_TYPE_FILE = 1;
    public static final int MESSAGE_TYPE_MAP = 4;
    public static final int MESSAGE_TYPE_CARD = 5;

    public static final int MESSAGE_STATAUE_SENDING = 0;
    public static final int MESSAGE_STATAUE_FAIL = -1;
    public static final int MESSAGE_STATAUE_SUCCESS = 1;

    public String messageId = "";
    public String mRecordId = "";
    public String mType = "";
    public String mTitle = "";
    public String mSubject = "";
    public String mTime = TimeUtils.getDateAndTime();
    public String detialId = "";
    public String sourceId = "";
    public String sourceName = "";
    public String sourcePath = "";
    public String mIcon = "";
    public String iconType = "";
    public boolean isSendto = true;
    public boolean isRead = false;
    public int issend = 0;
    public int sourceType = MESSAGE_TYPE_NOMAL;
    public int mHit = 0;
    public long sourceSize = 0;

    public String mTime2 = "";
    public int mHit2 = 0;
    public String mTitle2 = "";
    public long finishSize = 0;
    public boolean isplay = false;
    public boolean finish = false;
    public View image;
    public int mPriority = 0;

    protected Conversation(Parcel in) {
        mRecordId = in.readString();
        mType = in.readString();
        mTitle = in.readString();
        mSubject = in.readString();
        mTime = in.readString();
        detialId = in.readString();
        sourceId = in.readString();
        sourceName = in.readString();
        sourcePath = in.readString();
        sourceType = in.readInt();
        sourceSize = in.readLong();
        isRead = Boolean.valueOf(in.readString());
        isplay = Boolean.valueOf(in.readString());
        isSendto = Boolean.valueOf(in.readString());
        issend = in.readInt();
        mHit = in.readInt();
        mHit2 = in.readInt();
        mTitle2 = in.readString();
        mTime2 = in.readString();
        messageId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mRecordId);
        dest.writeString(mType);
        dest.writeString(mTitle);
        dest.writeString(mSubject);
        dest.writeString(mTime);
        dest.writeString(detialId);
        dest.writeString(sourceId);
        dest.writeString(sourceName);
        dest.writeString(sourcePath);
        dest.writeInt(sourceType);
        dest.writeLong(sourceSize);
        dest.writeString(String.valueOf(isRead));
        dest.writeString(String.valueOf(isplay));
        dest.writeString(String.valueOf(isSendto));
        dest.writeInt(issend);
        dest.writeInt(mHit);
        dest.writeInt(mHit2);
        dest.writeString(mTitle2);
        dest.writeString(mTime2);
        dest.writeString(messageId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Conversation> CREATOR = new Creator<Conversation>() {
        @Override
        public Conversation createFromParcel(Parcel in) {
            return new Conversation(in);
        }

        @Override
        public Conversation[] newArray(int size) {
            return new Conversation[size];
        }
    };

    public Conversation()
    {
        mRecordId = AppUtils.getguid();
    }

    public Conversation(Conversation info)
    {
        mType = info.mType;
        mRecordId = info.mRecordId;
        mTime = info.mTime;
        mTime2 = info.mTime2;
        mTitle = info.mTitle;
        mTitle2 = info.mTitle2;
        mSubject = info.mSubject;
        mHit = info.mHit;
        detialId = info.detialId;
        sourceId = info.sourceId;
        sourceType = info.sourceType;
        sourceName = info.sourceName;
        sourcePath = info.sourcePath;
        sourceSize = info.sourceSize;
        isRead = info.isRead;
        isSendto = info.isSendto;
        mHit2 = info.mHit2;
        finishSize = info.finishSize;
        isRead = info.isRead;
        isplay = info.isplay;
        image = info.image;
        issend = info.issend;
        messageId = info.messageId;
    }

    public void copy(Conversation info)
    {
        mType = info.mType;
        mRecordId = info.mRecordId;
        mTime = info.mTime;
        mTime2 = info.mTime2;
        mTitle = info.mTitle;
        mTitle2 = info.mTitle2;
        mSubject = info.mSubject;
        mHit = info.mHit;
        detialId = info.detialId;
        sourceId = info.sourceId;
        sourceType = info.sourceType;
        sourceName = info.sourceName;
        sourcePath = info.sourcePath;
        sourceSize = info.sourceSize;
        isRead = info.isRead;
        isSendto = info.isSendto;
        mHit2 = info.mHit2;
        finishSize = info.finishSize;
        isRead = info.isRead;
        isplay = info.isplay;
        issend = info.issend;
        messageId = info.messageId;
    }

    public static String getMessageType(String type) {
        if(type.endsWith("Report")) {
            return CONVERSATION_TYPE_REPORT;
        }
        else if(type.endsWith("Leave")) {
            return CONVERSATION_TYPE_LEAVE;
        }
        else if(type.endsWith("Schedule")) {
            return CONVERSATION_TYPE_SCHDULE;
        }
        else if(type.endsWith("Notice")) {
            return CONVERSATION_TYPE_NOTICE;
        }
        else if(type.endsWith("Vote")) {
            return CONVERSATION_TYPE_VOTE;
        }
        else if(type.endsWith("Task")) {
            return CONVERSATION_TYPE_TASK;
        }
        else if(type.endsWith("iweb[workflow]") || type.endsWith("iCloud[workflow]")) {
            return CONVERSATION_TYPE_IWEB_APPROVE;
        }
        else if(type.endsWith("iweb[mail]") || type.endsWith("iCloud[mail]")) {
            return CONVERSATION_TYPE_IWEB_MAIL;
        }
        else if(type.endsWith("message")) {
            return CONVERSATION_TYPE_MESSAGE;
        }
        return "";
    }
}
