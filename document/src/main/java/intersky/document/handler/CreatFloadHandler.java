package intersky.document.handler;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.document.DocumentManager;
import intersky.document.R;
import intersky.document.asks.DocumentAsks;
import intersky.document.view.activity.CreatFloadActivity;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;

public class CreatFloadHandler extends Handler {

    public CreatFloadActivity theActivity;

    public CreatFloadHandler(CreatFloadActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        NetObject object;
        // AppUtils.dissMissDialog();
        if (theActivity != null) {
            switch (msg.what) {
                case DocumentAsks.DOCUMENT_CREAT_SUCCESS:
                    theActivity.waitDialog.hide();
                    AppUtils.showMessage(theActivity, theActivity.getString(R.string.error_get_data_fail));
                    AppUtils.sendSampleBroadCast(theActivity,DocumentManager.ACTION_CREAT_DOCUMENT_FINISH);
                    theActivity.finish();
                    break;
            }
            super.handleMessage(msg);
        }
    }
};
