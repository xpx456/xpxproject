package intersky.mail.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import intersky.appbase.entity.Attachment;

public class Mail implements Parcelable {

    public String mRecordId = "";
    public String mMailBoxId = "";
    public String mUserId = "";
    public String mSerialId = "";
    public String mSubject = "";
    public String mSContent = "";
    public String mFrom = "";
    public String mTo = "";
    public String mLcc = "";
    public String mBcc = "";
    public String mCc = "";
    public String mDate = "";
    public String lables = "";
    public String mAttachmentsJson = "";
    public boolean isReaded = true;
    public boolean isRepeat = false;
    public boolean isForwarded = false;
    public boolean isLocal = false;
    public boolean haveAttachment = false;
    public boolean isSelect = false;
    public String mContentHtml = "";
    public String mCatalogueID = "";
    public int state;
    public int mailtype = 0;
    public int sendstate = 0;
    public ArrayList<Attachment> attachments = new ArrayList<Attachment>();

    public Mail() {

    }

    public void setMail(Mail mail) {
        this.mRecordId = mail.mRecordId;
        this.mMailBoxId = mail.mMailBoxId;
        this.mUserId = mail.mUserId;
        this.mSerialId = mail.mSerialId;
        this.mSubject = mail.mSubject;
        this.mSContent = mail.mSContent;
        this.mFrom = mail.mFrom;
        this.mTo = mail.mTo;
        this.mLcc = mail.mLcc;
        this.mBcc = mail.mBcc;
        this.mCc = mail.mCc;
        this.mDate = mail.mDate;
        this.lables = mail.lables;
        this.mAttachmentsJson = mail.mAttachmentsJson;
        this.isReaded = mail.isReaded;
        this.isRepeat = mail.isRepeat;
        this.isForwarded = mail.isForwarded;
        this.isLocal = mail.isLocal;
        this.haveAttachment = mail.haveAttachment;
        this.isSelect = mail.isSelect;
        this.mContentHtml = mail.mContentHtml;
        this.mCatalogueID = mail.mCatalogueID;
        this.state = mail.state;
        this.mailtype = mail.mailtype;
        this.sendstate = mail.sendstate;
    }

    public Mail(String mRecordId, String mMailBoxId, String mUserId
            , String mSerialId, String mSubject, String mSContent, String mFrom, String mTo
            , String mLcc, String mBcc, String mCc, String mDate, String mAttachments, boolean isReaded
            , boolean isLocal, boolean haveAttachment, String mCatalogueID, boolean isRepeat, boolean isForwarded) {
        this.mRecordId = mRecordId;
        this.mMailBoxId = mMailBoxId;
        this.mUserId = mUserId;
        this.mSerialId = mSerialId;
        this.mSubject = mSubject;
        this.mSContent = mSContent;
        this.mFrom = mFrom;
        this.mTo = mTo;
        this.mLcc = mLcc;
        this.mBcc = mBcc;
        this.mCc = mCc;
        this.mDate = mDate;
        this.mAttachmentsJson = mAttachments;
        this.isReaded = isReaded;
        this.isLocal = isLocal;
        this.haveAttachment = haveAttachment;
        this.mCatalogueID = mCatalogueID;
        this.isRepeat = isRepeat;
        this.isForwarded = isForwarded;
    }

    public Mail(String mRecordId, String mMailBoxId, String mUserId
            , String mSerialId, String mSubject, String mSContent, String mFrom, String mTo
            , String mLcc, String mBcc, String mCc, String mDate, String mAttachments, boolean isReaded
            , boolean isLocal, boolean haveAttachment, boolean isSelect, String mContentHtml, String mCatalogueID
            , boolean isRepeat, boolean isForwarded,int state,int mailtype,int sendstate,String lables) {
        this.mRecordId = mRecordId;
        this.mMailBoxId = mMailBoxId;
        this.mUserId = mUserId;
        this.mSerialId = mSerialId;
        this.mSubject = mSubject;
        this.mSContent = mSContent;
        this.mFrom = mFrom;
        this.mTo = mTo;
        this.mLcc = mLcc;
        this.mBcc = mBcc;
        this.mCc = mCc;
        this.mDate = mDate;
        this.mAttachmentsJson = mAttachments;
        this.isReaded = isReaded;
        this.isLocal = isLocal;
        this.haveAttachment = haveAttachment;
        this.isSelect = isSelect;
        this.mContentHtml = mContentHtml;
        this.mCatalogueID = mCatalogueID;
        this.isRepeat = isRepeat;
        this.isForwarded = isForwarded;
        this.state = state;
        this.mailtype = mailtype;
        this.sendstate = sendstate;
        this.lables = lables;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(mRecordId);
        dest.writeString(mMailBoxId);
        dest.writeString(mUserId);
        dest.writeString(mSerialId);
        dest.writeString(mSubject);
        dest.writeString(mSContent);
        dest.writeString(mFrom);
        dest.writeString(mTo);
        dest.writeString(mLcc);
        dest.writeString(mBcc);
        dest.writeString(mCc);
        dest.writeString(mDate);
        dest.writeString(mAttachmentsJson);
        dest.writeString(String.valueOf(isReaded));
        dest.writeString(String.valueOf(isLocal));
        dest.writeString(String.valueOf(haveAttachment));
        dest.writeString(String.valueOf(isSelect));
        dest.writeString(mContentHtml);
        dest.writeString(mCatalogueID);
        dest.writeString(String.valueOf(isRepeat));
        dest.writeString(String.valueOf(isForwarded));
        dest.writeInt(state);
        dest.writeInt(mailtype);
        dest.writeInt(sendstate);
        dest.writeString(lables);
    }

    public static final Creator<Mail> CREATOR = new Creator<Mail>() {
        public Mail createFromParcel(Parcel in) {
            return new Mail(in.readString(), in.readString(), in.readString(),
                    in.readString(), in.readString(), in.readString(), in.readString(),
                    in.readString(), in.readString(), in.readString(), in.readString(),
                    in.readString(), in.readString(), Boolean.valueOf(in.readString()), Boolean.valueOf(in.readString()),
                    Boolean.valueOf(in.readString()), Boolean.valueOf(in.readString()), in.readString(), in.readString(),
                    Boolean.valueOf(in.readString()), Boolean.valueOf(in.readString()),in.readInt(),in.readInt(),in.readInt(),in.readString());
        }

        public Mail[] newArray(int size) {
            return new Mail[size];
        }
    };
}
