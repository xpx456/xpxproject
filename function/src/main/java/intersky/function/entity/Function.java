package intersky.function.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Function implements Parcelable {

    public static final String BLACK = "black";
    public static final String WARM = "warm";
    public static final String EXAMINE = "examine";
    public static final String MIX = "mix";
    public static final String GRID = "grid";
    public static final String LABEL = "label";
    public static final String NOTICE = "notice";
    public static final String VOTE = "vote";
    public static final String DATE = "date";
    public static final String LEAVE = "leave";
    public static final String CIRCLE = "circle";
    public static final String WEB = "web";
    public static final String WORKATTDENCE = "workattdence";
    public static final String SIGN = "sign";
    public static final String WORKREPORT = "workreport";
    public static final String NEWTASK = "newtask";
    public static final String NEWPROJECT = "newproject";
    public static final String NEWMAIL = "newmail";
    public static final String TASK = "task";
    public static final String CARD = "card";
    public static final String SIGN_M = "sign_m";
    public static final String WORKREPORT_M = "workreport_m";
    public static final String WORKATTDENCE_M  = "workattdence_m";
    public static final String NOTICE_M = "notice_m";
    public static final String VOTE_M = "vote_m";
    public static final String DATE_M = "date_m";
    public static final String LEAVE_M = "leave_m";
    public static final String COLUMNS = "columns";
    public static final String COLUMN = "column";
    public static final String PIE = "pie";
    public static final String LINE = "linechart";
    public static final String FUNNEL = "funnelchart";
    public static final String BAR = "barchart";

    public String mCaption = "";
    public String typeName = MIX;
    public String iconName = "";
    public int hintCount = 0;
    public String mCatalogueName = "";
    public String modulflag = "";
    public String mName = "";
    public String mGrop = "";
    public String mRecordId = "";
    public boolean showAction = false;
    public boolean isSecond = false;
    public boolean canEdit = false;
    public boolean isWorkFlowDetial = false;
    public String series = "";
    public String mRowName = "";
    public String mColName = "";
    public String mCellValue = "";
    public static final Creator<Function> CREATOR = new Creator<Function>() {
        @Override
        public Function createFromParcel(Parcel in) {
            return new Function(in);
        }

        @Override
        public Function[] newArray(int size) {
            return new Function[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags )
    {
        dest.writeString(mCaption);
        dest.writeString(typeName);
        dest.writeString(iconName);
        dest.writeString(mName);
        dest.writeString(mGrop);
        dest.writeInt( hintCount );
        dest.writeString( mCatalogueName );
        dest.writeString(modulflag);
        dest.writeString(mRecordId);
        dest.writeString(String.valueOf(showAction));
        dest.writeString(String.valueOf(isSecond));
        dest.writeString(String.valueOf(canEdit));
        dest.writeString(String.valueOf(isWorkFlowDetial));
        dest.writeString( mRowName );
        dest.writeString(mColName);
        dest.writeString(mCellValue);

    }

    private Function(Parcel in )
    {
        mCaption = in.readString();
        typeName = in.readString();
        iconName = in.readString();
        mName = in.readString();
        mGrop = in.readString();
        hintCount = in.readInt();
        mCatalogueName = in.readString();
        modulflag = in.readString();
        mRecordId = in.readString();
        showAction = Boolean.valueOf(in.readString());
        isSecond = Boolean.valueOf(in.readString());
        canEdit = Boolean.valueOf(in.readString());
        isWorkFlowDetial = Boolean.valueOf(in.readString());
        mRowName = in.readString();
        mColName = in.readString();
        mCellValue = in.readString();
    }

    public Function()
    {

    }

    public Function(String typeName)
    {
        this.typeName = typeName;
    }
}
