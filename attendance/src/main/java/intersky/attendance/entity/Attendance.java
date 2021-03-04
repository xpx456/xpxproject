package intersky.attendance.entity;
public class Attendance {

	private int mStatus = 0;
	private double mAltitude = 0;
	private double mLongitude = 0;
	private String mAttenceId = "";
	private String mUserId = "";
	private String mCompanyId = "";
	private String mGroupId = "";
	private String mATime = "";
	private String mCTime = "";
	private String mAddress = "";
	private String subText = "";
	private int type = 1;

	public Attendance(int mStatus, String mAttenceId, double mAltitude, double mLongitude, String mUserId, String mCompanyId, String mGroupId
			, String mAddress, String mATime, String mCTime, String subText)
	{
		this.mStatus = mStatus;
		this.mAttenceId = mAttenceId;
		this.mAltitude = mAltitude;
		this.mLongitude = mLongitude;
		this.mUserId = mUserId;
		this.mCompanyId = mCompanyId;
		this.mGroupId = mGroupId;
		this.mAddress = mAddress;
		this.mATime = mATime;
		this.mCTime = mCTime;
		this.subText = subText;
	}

	public Attendance(int mStatus)
	{
		this.mStatus = mStatus;
		this.type = 0;
	}

	public int getmStatus() {
		return mStatus;
	}

	public void setmStatus(int mStatus) {
		this.mStatus = mStatus;
	}

	public double getmAltitude() {
		return mAltitude;
	}

	public void setmAltitude(double mAltitude) {
		this.mAltitude = mAltitude;
	}

	public double getmLongitude() {
		return mLongitude;
	}

	public void setmLongitude(double mLongitude) {
		this.mLongitude = mLongitude;
	}

	public String getmAttenceId() {
		return mAttenceId;
	}

	public void setmAttenceId(String mAttenceId) {
		this.mAttenceId = mAttenceId;
	}

	public String getmUserId() {
		return mUserId;
	}

	public void setmUserId(String mUserId) {
		this.mUserId = mUserId;
	}

	public String getmCompanyId() {
		return mCompanyId;
	}

	public void setmCompanyId(String mCompanyId) {
		this.mCompanyId = mCompanyId;
	}

	public String getmGroupId() {
		return mGroupId;
	}

	public void setmGroupId(String mGroupId) {
		this.mGroupId = mGroupId;
	}

	public String getmATime() {
		return mATime;
	}

	public void setmATime(String mATime) {
		this.mATime = mATime;
	}

	public String getmCTime() {
		return mCTime;
	}

	public void setmCTime(String mCTime) {
		this.mCTime = mCTime;
	}

	public String getmAddress() {
		return mAddress;
	}

	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	public String getSubText() {
		return subText;
	}

	public void setSubText(String subText) {
		this.subText = subText;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
