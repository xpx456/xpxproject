package intersky.function.entity;

import android.os.Parcel;
import android.os.Parcelable;

import intersky.appbase.bus.Bus;
import intersky.function.FunctionUtils;

public class WorkFlowItem implements Parcelable {

    public String taskID = "";
    public String subject = "";
    public String content = "";
    public String module = "";
    public String recordID = "";
    public String InstanceID = "";
    public String receiveTime = "";
    public String userName = "";
    public int state = 0;

    public WorkFlowItem(String taskID, String recordID,
                        String subject, String receiveTime, String userName,
                        String InstanceID, String module,int state)
    {
        super();
        this.taskID = taskID;
        this.recordID = recordID;
        this.subject = subject;
        this.InstanceID = InstanceID;
        this.receiveTime = receiveTime;
        this.userName = (String)Bus.callData(FunctionUtils.getInstance().context,"chat/getContactName",userName);
        this.module = module;
        this.state = state;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags )
    {
        dest.writeString( taskID );
        dest.writeString(recordID);
        dest.writeString(subject);
        dest.writeString( receiveTime );
        dest.writeString( userName );
        dest.writeString(InstanceID);
        dest.writeString( module );
        dest.writeInt( state );
    }

    public static final Creator<WorkFlowItem> CREATOR = new Creator<WorkFlowItem>()
    {
        public WorkFlowItem createFromParcel(Parcel in )
        {
            return new WorkFlowItem(in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readInt());
        }

        public WorkFlowItem[] newArray(int size )
        {
            return new WorkFlowItem[size];
        }
    };
}
