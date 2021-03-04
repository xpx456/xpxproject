package intersky.document.entity;

import android.os.Parcel;
import android.os.Parcelable;

import intersky.document.DocumentManager;


public class DocumentNet implements Parcelable
{
	public String mOwnerType = "";
	public String mOwnerID = "";
	public String mName = "";
	public long mSize = 0;
	public long mFinishSize = 0;
	public int mState = 0;
	public String mDate= "";
	public String mRecordID = "";
	public boolean mShared = false;
	public boolean isSelect = false;
	public String mCatalogueID = "";
	public String parentid = "";
	public String mUrl = "";
	public String mPath = "";
	public long speed = 0;
	public int mType = DocumentManager.TYPE_DOWNLOAD_NOMAL;

	public DocumentNet() {

	}

	public void copy(DocumentNet documentNet) {
		this.mOwnerType = documentNet.mOwnerType;
		this.mName = documentNet.mName;
		this.mSize = documentNet.mSize;
		this.mDate = documentNet.mDate;
		this.mOwnerID = documentNet.mOwnerID;
		this.mRecordID = documentNet.mRecordID;
		this.mShared = documentNet.mShared;
		this.mType = documentNet.mType;
		this.parentid = documentNet.parentid;
		this.mFinishSize = documentNet.mFinishSize;
		this.mState = documentNet.mState;
		this.mCatalogueID = documentNet.mCatalogueID;
		this.mUrl = documentNet.mUrl;
		this.mPath = documentNet.mPath;
	}

	public DocumentNet(String OwnerType, String mName, long mSize, String mDate, String mOwnerID, String mRecordID, boolean Shared, int mType, String parentid, long mFinishSize , int mState
	, String mCatalogueID, String mUrl, String mPath)
	{
		this.mOwnerType = OwnerType;
		this.mName = mName;
		this.mSize = mSize;
		this.mDate = mDate;
		this.mOwnerID = mOwnerID;
		this.mRecordID = mRecordID;
		this.mShared = Shared;
		this.mType = mType;
        this.parentid = parentid;
        this.mFinishSize = mFinishSize;
        this.mState = mState;
        this.mCatalogueID = mCatalogueID;
		this.mUrl = mUrl;
		this.mPath = mPath;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags )
	{
		dest.writeString( mOwnerType );
		dest.writeString(mName);
		dest.writeLong( mSize );
		dest.writeString( mDate );
		dest.writeString(mOwnerID);
		dest.writeString( mRecordID );
		dest.writeString(String.valueOf(mShared));
		dest.writeInt( mType );
        dest.writeString( parentid );
		dest.writeLong( mFinishSize );
		dest.writeInt(mState);
		dest.writeString( mCatalogueID );
		dest.writeString( mUrl );
		dest.writeString( mPath );
	}

	public static final Creator<DocumentNet> CREATOR = new Creator<DocumentNet>()
	{
		public DocumentNet createFromParcel( Parcel in )
		{
			return new DocumentNet(in.readString(),
					in.readString(),
					in.readLong(),
					in.readString(),
					in.readString(),
					in.readString(),
					Boolean.valueOf(in.readString()),
					in.readInt(),
                    in.readString(),
					in.readLong(),
					in.readInt(),
					in.readString(),
					in.readString(),
					in.readString());
		}

		public DocumentNet[] newArray( int size )
		{
			return new DocumentNet[size];
		}
	};

	@Override
	public int describeContents()
	{
		return 0;
	}



}
