package intersky.sign.entity;
import android.os.Parcel;
import android.os.Parcelable;

import intersky.sign.SignManager;


public class Sign implements Parcelable {

	public String mSigId = "";
	public double mAltitude = 0;
	public double mLongitude = 0;
	public String mImage = "";
	public String mName = "";
	public String mTitle = "";
	public String mDate = "";
	public String mTime = "";
	public String mAddress = "";
	public String mReason = "";
	public String mRemark = "";

	
	public Sign(String mSigId, String mName, double mAltitude, double mLongitude, String mTitle, String mDate, String mTime
			, String mAddress, String mReason, String mRemark, String mImage)
	{
		this.mSigId = mSigId;
		this.mName = SignManager.getInstance().oaUtils.mContactManager.mOrganization.getContactName(mName);
		this.mAltitude = mAltitude;
		this.mLongitude = mLongitude;
		this.mTitle = mTitle;
		this.mDate = mDate;
		this.mTime = mTime;
		this.mAddress = mAddress;
		this.mReason = mReason;
		this.mRemark = mRemark;
		this.mImage = mImage;
	}

	public Sign() {

	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(mSigId);
		dest.writeString(mName);
		dest.writeDouble(mAltitude);
		dest.writeDouble(mLongitude);
		dest.writeString(mTitle);
		dest.writeString(mDate);
		dest.writeString(mTime);
		dest.writeString(mAddress);
		dest.writeString(mReason);
		dest.writeString(mRemark);
		dest.writeString(mImage);
	}

	public static final Creator<Sign> CREATOR = new Creator<Sign>() {
		public Sign createFromParcel(Parcel in) {
			return new Sign(in.readString(),in.readString(), in.readDouble(), in.readDouble(), in.readString(),
					in.readString(), in.readString(), in.readString(),in.readString(), in.readString(), in.readString());
		}

		public Sign[] newArray(int size) {
			return new Sign[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}




}
