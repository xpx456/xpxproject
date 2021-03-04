package intersky.appbase.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import intersky.apputils.CharacterParser;

public class Contacts implements Parcelable {

    public static final int TYPE_LETTER = 1;
    public static final int TYPE_CLASS = 2;
    public static final int TYPE_PERSON = 0;
    public static final int TYPE_TOP = 3;
    public String mId = "";
    public String mName = "";
    public String mRName = "";
    public String mRecordid = "";
    public String mSex = "";
    public String mMobile = "";
    public String mPhone = "";
    public String mPhone2 = "";
    public String mFax = "";
    public String eMail = "";
    public String mDepartMent = "";
    private String mPingyin = "";
    public String mPosition = "";
    public int mType = TYPE_PERSON;
    public int mPersonCount = 0;
    public int colorhead = -1;
    public boolean isselect = false;
    public boolean isadmin = false;
    public boolean isapply = false;
    public boolean islocal = false;
    public ArrayList<Contacts> mMyContacts = new ArrayList<Contacts>();
    public ArrayList<Contacts> mContacts = new ArrayList<Contacts>();
    public ArrayList<Contacts> mHeadContacts = new ArrayList<Contacts>();
    public String des = "";
    public String typevalue = "";
    public String typearea = "";
    public int sex = 0;
    public String confrim = "";
    public String location = "";
    public int leaves = 0;
    public String company = "";
    public String companyid = "";
    public String address = "";
    public String icon = "";
    public String cover = "";
    public String province = "";
    public String city = "";
    public String staue = "";
    public String vip = "";
    public String complaint = "";
    public Object object = null;
    public boolean issail = false;
    public boolean isadd = true;
    public boolean typebooleans[] = new boolean[27];

    protected Contacts(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mRName = in.readString();
        mRecordid = in.readString();
        mSex = in.readString();
        mMobile = in.readString();
        mPhone = in.readString();
        mPhone2 = in.readString();
        mFax = in.readString();
        eMail = in.readString();
        mDepartMent = in.readString();
        mPingyin = in.readString();
        mPosition = in.readString();
        mType = in.readInt();
        mPersonCount = in.readInt();
        colorhead = in.readInt();
        isselect = in.readByte() != 0;
        isadmin = in.readByte() != 0;
        isapply = in.readByte() != 0;
        islocal = in.readByte() != 0;
        mMyContacts = in.createTypedArrayList(Contacts.CREATOR);
        mContacts = in.createTypedArrayList(Contacts.CREATOR);
        mHeadContacts = in.createTypedArrayList(Contacts.CREATOR);
        des = in.readString();
        typevalue = in.readString();
        typearea = in.readString();
        sex = in.readInt();
        confrim = in.readString();
        location = in.readString();
        leaves = in.readInt();
        company = in.readString();
        companyid = in.readString();
        address = in.readString();
        icon = in.readString();
        cover = in.readString();
        province = in.readString();
        city = in.readString();
        staue = in.readString();
        vip = in.readString();
        complaint = in.readString();
        issail = in.readByte() != 0;
        isadd = in.readByte() != 0;
        typebooleans = in.createBooleanArray();
    }

    public static final Creator<Contacts> CREATOR = new Creator<Contacts>() {
        @Override
        public Contacts createFromParcel(Parcel in) {
            return new Contacts(in);
        }

        @Override
        public Contacts[] newArray(int size) {
            return new Contacts[size];
        }
    };

    public void setName(String mName) {
        if(mName != null)
        {
            this.mName = mName;
//            this.mPingyin= CharacterParser.getInstance().getSelling(this.mName);
//            this.mPingyin=this.mPingyin.toLowerCase();
        }
        else
        {
            this.mName = "";
//            this.mPingyin= CharacterParser.getInstance().getSelling(this.mName);
//            this.mPingyin=this.mPingyin.toLowerCase();
        }

    }

    public String getName()
    {
        if(mName == null)
        {
            mName = "";
        }
        if(mName.length() > 0)
        return this.mName;
        else
            return this.mRName;
    }
    public String getmRName()
    {
        if(mRName.length() != 0)
        return this.mRName;
        else
            return this.mName;
    }

    public Contacts(String id, String name)
    {
        mId = id;
        mName = name;
    }

    public Contacts(String id, int type)
    {
        mId = id;
        mType = type;
    }

    public Contacts()
    {

    }

    public Contacts(Account account)
    {
        this.mName = account.mUserName;
        this.mRName = account.mRealName;
        this.mRecordid = account.mRecordId;
    }

    public Contacts(String letter)
    {
        this.mName = letter;
        this.mPingyin= CharacterParser.getInstance().getSelling(this.mName);
        this.mPingyin=this.mPingyin.toLowerCase();
        this.mType = TYPE_LETTER;
    }


//    public static final Creator<Contacts> CREATOR = new Creator<Contacts>() {
//        @Override
//        public Contacts createFromParcel(Parcel in) {
//            return new Contacts(in);
//        }
//
//        @Override
//        public Contacts[] newArray(int size) {
//            return new Contacts[size];
//        }
//    };
//
//    @Override
//    public int describeContents()
//    {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags )
//    {
//        dest.writeString(mId);
//        dest.writeString(mName);
//        dest.writeString(mRName);
//        dest.writeString(mRecordid);
//        dest.writeString( mMobile );
//        dest.writeString(mPhone);
//        dest.writeString(mFax);
//        dest.writeString(eMail);
//        dest.writeString(mDepartMent);
//        dest.writeString(mPingyin);
//        dest.writeString(mPosition);
//        dest.writeString(des);
//        dest.writeString(typevalue);
//        dest.writeString(typearea);
//        dest.writeInt(sex);
//        dest.writeString(location);
//        dest.writeString(confrim);
//        dest.writeInt(leaves);
//        dest.writeString(String.valueOf(isadd));
//        dest.writeString(company);
//        dest.writeString(address);
//        dest.writeString(companyid);
//        dest.writeString(icon);
//        dest.writeString(province);
//        dest.writeString(city);
//        dest.writeString(mPhone2);
//        dest.writeString(staue);
//        dest.writeString(complaint);
//        dest.writeString(cover);
//        dest.writeString(vip);
//        dest.writeString(String.valueOf(issail));
//    }
//
//    private Contacts(Parcel in )
//    {
//        mId = in.readString();
//        mName = in.readString();
//        mRName = in.readString();
//        mRecordid = in.readString();
//        mMobile = in.readString();
//        mPhone = in.readString();
//        mFax = in.readString();
//        eMail = in.readString();
//        mDepartMent = in.readString();
//        mPingyin = in.readString();
//        mPosition = in.readString();
//        des = in.readString();
//        typevalue = in.readString();
//        typearea = in.readString();
//        sex = in.readInt();
//        location = in.readString();
//        confrim = in.readString();
//        leaves = in.readInt();
//        isadd = Boolean.valueOf(in.readString());
//        company = in.readString();
//        address = in.readString();
//        companyid = in.readString();
//        icon = in.readString();
//        province = in.readString();
//        city = in.readString();
//        mPhone2 = in.readString();
//        staue = in.readString();
//        complaint = in.readString();
//        cover = in.readString();
//        vip = in.readString();
//        issail = Boolean.valueOf(in.readString());
//    }

    public String getmPingyin()
    {
        if(mPingyin.length() == 0)
        {
            if(this.mRName.length() > 0)
            {
                mPingyin = CharacterParser.getInstance().getSelling(this.mRName).toLowerCase();
            }
            else
            {
                mPingyin = CharacterParser.getInstance().getSelling(this.mName).toLowerCase();
            }
        }
        return mPingyin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mRName);
        dest.writeString(mRecordid);
        dest.writeString(mSex);
        dest.writeString(mMobile);
        dest.writeString(mPhone);
        dest.writeString(mPhone2);
        dest.writeString(mFax);
        dest.writeString(eMail);
        dest.writeString(mDepartMent);
        dest.writeString(mPingyin);
        dest.writeString(mPosition);
        dest.writeInt(mType);
        dest.writeInt(mPersonCount);
        dest.writeInt(colorhead);
        dest.writeByte((byte) (isselect ? 1 : 0));
        dest.writeByte((byte) (isadmin ? 1 : 0));
        dest.writeByte((byte) (isapply ? 1 : 0));
        dest.writeByte((byte) (islocal ? 1 : 0));
        dest.writeTypedList(mMyContacts);
        dest.writeTypedList(mContacts);
        dest.writeTypedList(mHeadContacts);
        dest.writeString(des);
        dest.writeString(typevalue);
        dest.writeString(typearea);
        dest.writeInt(sex);
        dest.writeString(confrim);
        dest.writeString(location);
        dest.writeInt(leaves);
        dest.writeString(company);
        dest.writeString(companyid);
        dest.writeString(address);
        dest.writeString(icon);
        dest.writeString(cover);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(staue);
        dest.writeString(vip);
        dest.writeString(complaint);
        dest.writeByte((byte) (issail ? 1 : 0));
        dest.writeByte((byte) (isadd ? 1 : 0));
        dest.writeBooleanArray(typebooleans);
    }
}
