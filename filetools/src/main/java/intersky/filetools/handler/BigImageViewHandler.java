package intersky.filetools.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.filetools.view.activity.BigImageViewActivity;
//01
public class BigImageViewHandler extends Handler {

    public static final int HID_EDIT = 3030100;


    public BigImageViewActivity theActivity;

    public BigImageViewHandler(BigImageViewActivity mBigImageViewActivity) {
        theActivity = mBigImageViewActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case HID_EDIT:
                theActivity.mBigImageViewPresenter.hitEdit();
                break;
        }

    }
}
