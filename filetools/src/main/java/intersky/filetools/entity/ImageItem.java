package intersky.filetools.entity;

import android.os.Parcel;
import android.os.Parcelable;


public class ImageItem implements Parcelable {
	public String imageId;
	public String imagePath;
	public long size;
	public boolean isSelected = false;

	public ImageItem() {

	}

	protected ImageItem(Parcel in) {
		imageId = in.readString();
		imagePath = in.readString();
		isSelected = in.readByte() != 0;
		size = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(imageId);
		dest.writeString(imagePath);
		dest.writeByte((byte) (isSelected ? 1 : 0));
		dest.writeLong(size);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
		@Override
		public ImageItem createFromParcel(Parcel in) {
			return new ImageItem(in);
		}

		@Override
		public ImageItem[] newArray(int size) {
			return new ImageItem[size];
		}
	};
}
