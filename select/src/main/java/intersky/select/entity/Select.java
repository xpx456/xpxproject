package intersky.select.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import intersky.apputils.CharacterParser;

public class Select implements Parcelable {

    public String mId = "";
    public String mName = "";
    public String mColor = "";
    public String mPingyin = "";
    public int mType = 0;
    public View view;
    public boolean iselect = false;

    public Select(){

    }

    public Select(String mId, String mName)
    {
        this.mId = mId;
        this.mName = mName;
        if(mName.length() > 0)
        this.mPingyin= CharacterParser.getInstance().getSelling(this.mName);
    }

    protected Select(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mPingyin = in.readString();
        mType = in.readInt();
        iselect = in.readByte() != 0;
        mColor = in.readString();
    }

    public static final Creator<Select> CREATOR = new Creator<Select>() {
        @Override
        public Select createFromParcel(Parcel in) {
            return new Select(in);
        }

        @Override
        public Select[] newArray(int size) {
            return new Select[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mPingyin);
        dest.writeInt(mType);
        dest.writeByte((byte) (iselect ? 1 : 0));
        dest.writeString(mColor);
    }
}
