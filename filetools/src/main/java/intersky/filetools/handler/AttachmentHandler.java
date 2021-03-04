package intersky.filetools.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.filetools.view.activity.AttachmentActivity;

//00
public class AttachmentHandler extends Handler {

    public static final int DOWNLOAD_FINISH = 3030001;
    public static final int DOWNLOAD_UPTATE = 3030002;
    public static final int DOWNLOAD_FAIL= 3030003;

    public AttachmentActivity theActivity;

    public AttachmentHandler(AttachmentActivity mAttachmentActivity) {
        theActivity = mAttachmentActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case DOWNLOAD_FINISH:
                theActivity.mAttachmentPresenter.doFinishDown((Intent) msg.obj);
                break;
            case DOWNLOAD_UPTATE:
                theActivity.mAttachmentPresenter.doUpdata((Intent) msg.obj);
                break;
            case DOWNLOAD_FAIL:
                theActivity.mAttachmentPresenter.initData();
                break;
        }

    }
}
