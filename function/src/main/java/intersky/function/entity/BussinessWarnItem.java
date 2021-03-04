package intersky.function.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class BussinessWarnItem implements Parcelable {

    public String serialID;
    public String caption;
    public String module;
    public String parentID;
    public String subject;
    public String memo;
    public String startTime;

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags )
    {
        dest.writeString(serialID);
        dest.writeString(caption);
        dest.writeString(module);
        dest.writeString( parentID );
        dest.writeString( subject );
        dest.writeString( memo );
        dest.writeString( startTime );
    }

    public static final Creator<BussinessWarnItem> CREATOR = new Creator<BussinessWarnItem>()
    {
        public BussinessWarnItem createFromParcel(Parcel in )
        {
            return new BussinessWarnItem(in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString());
        }

        public BussinessWarnItem[] newArray(int size )
        {
            return new BussinessWarnItem[size];
        }
    };


    public BussinessWarnItem(String serialID, String caption,
                             String module, String parentID, String subject, String memo,
                             String startTime)
    {
        super();
        this.serialID = serialID;
        this.caption = caption;
        this.module = module;
        this.parentID = parentID;
        this.subject = subject;
        this.memo = memo;
        this.startTime = startTime;
    }
}
