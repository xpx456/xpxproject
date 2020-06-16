package intersky.sign.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;



import intersky.apputils.AppUtils;
import intersky.oa.OaUtils;
import intersky.sign.R;
import intersky.sign.SignManager;
import intersky.sign.asks.SignAsks;
import intersky.sign.prase.SignPrase;
import intersky.sign.view.activity.SignDetialActivity;
import intersky.xpxnet.net.NetObject;

//01
public class SignDetialHandler extends Handler {

    public static final int EVENT_ADD_PIC = 3230100;
    public static final int EVENT_DELETE_PIC = 3230101;

    public SignDetialActivity theActivity;

    public SignDetialHandler(SignDetialActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case SignAsks.EVENT_SAVE_SIGN_SUCCESS:
                    theActivity.waitDialog.hide();
                    theActivity.issub = true;
                    if(SignPrase.praseData((NetObject) msg.obj,theActivity)) {
                        AppUtils.showMessage(theActivity,theActivity.getString(R.string.xml_save_attendance_success));
                        SignManager.getInstance().getSignHit(theActivity);
                        theActivity.finish();
                    }
                    break;
                case EVENT_ADD_PIC:
                    theActivity.mSignDetialPresenter.setpic((Intent) msg.obj);
                    break;
                case EVENT_DELETE_PIC:
                    theActivity.mSignDetialPresenter.deletePic((Intent) msg.obj);
                    break;
                case OaUtils.EVENT_UPLOAD_FILE_RESULT:
                    theActivity.waitDialog.hide();
                    theActivity.mSignDetialPresenter.doSave((String) msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
