package intersky.xpxnet.net;

import android.os.Parcel;
import android.os.Parcelable;

public class Service implements Parcelable {

    public String sName = "";
    public String sRecordId = "";
    public String sAddress = "";
    public String sPort = "";
    public String sCode = "";
    public boolean sType = true;
    public boolean https = false;

    public Service() {

    }

    public void copy(Service service) {
        this.sName = service.sName;
        this.sRecordId = service.sRecordId;
        this.sAddress = service.sAddress;
        this.sPort = service.sPort;
        this.sCode = service.sCode;
        this.sType = service.sType;
        this.https = service.https;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public Service(String name, String address, String port, String sCode, String recordid, boolean sType) {

        this.sName = name;
        this.sAddress = address;
        this.sPort = port;
        this.sCode = sCode;
        this.sRecordId = recordid;
        this.sType = sType;
    }

    public Service(String name, String address, String port, String sCode, String recordid, boolean sType,boolean https) {

        this.sName = name;
        this.sAddress = address;
        this.sPort = port;
        this.sCode = sCode;
        this.sRecordId = recordid;
        this.sType = sType;
        this.https = https;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sName);
        dest.writeString(sAddress);
        dest.writeString(sPort);
        dest.writeString(sCode);
        dest.writeString(sRecordId);
        dest.writeString(String.valueOf(sType));
        dest.writeString(String.valueOf(https));
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        public Service createFromParcel(Parcel in) {
            return new Service(in.readString(), in.readString(), in.readString(),in.readString(), in.readString(),
                    Boolean.valueOf(in.readString()),Boolean.valueOf(in.readString()));
        }

        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    @Override
    public String toString()
    {
        return sName;
    }
}