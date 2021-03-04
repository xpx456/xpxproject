package intersky.filetools.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.filetools.R;
import intersky.filetools.entity.AlbumItem;
import intersky.filetools.view.activity.PhotoSelectActivity;
//03
public class PhotoSelectHandler extends Handler {

    public static final int INIT_PHOTO_VIEW = 3030300;
    public static final int UPDATA_PHOTO_VIEW = 3030301;
    public static final int SET_POSITION = 30303102;
    public PhotoSelectActivity theActivity;

    public PhotoSelectHandler(PhotoSelectActivity mPhotoSelectActivity) {
        theActivity = mPhotoSelectActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case INIT_PHOTO_VIEW:
                theActivity.waitDialog.hide();
                theActivity.mPhotoSelectPresenter.initPhoto();
                break;
            case UPDATA_PHOTO_VIEW:
                theActivity.waitDialog.hide();
                theActivity.title.setText(theActivity.getString(R.string.select_now)+String.format("%d", AlbumItem.getInstance().adds.size())+theActivity.getString(R.string.files));
                theActivity.mPhotoDetialGrideAdapter.notifyDataSetChanged();
                break;
            case SET_POSITION:
                theActivity.mPhotoSelectPresenter.setPosition((Intent) msg.obj);
                break;
        }

    }
}
