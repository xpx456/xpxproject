package intersky.attendance.handler;

import android.os.Handler;
import android.os.Message;

import intersky.attendance.AttendanceManager;
import intersky.attendance.R;
import intersky.attendance.asks.AttdenanceAsks;
import intersky.attendance.prase.AttdencePrase;
import intersky.attendance.view.activity.NewSetAttendanceActivity;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;

//01
public class NewSetAttendanceHandler extends Handler {

    public static final int EVENT_SET_USER = 3160100;
    public static final int EVENT_SET_REMIND = 3160101;
    public NewSetAttendanceActivity theActivity;

    public NewSetAttendanceHandler(NewSetAttendanceActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case AttdenanceAsks.EVENT_SAVE_SUCCESS:
                    theActivity.waitDialog.hide();
                    if(AttdencePrase.praseData((NetObject) msg.obj,theActivity)) {
                        AppUtils.showMessage(theActivity,theActivity.getString(R.string.keyword_savesuccess));
                        AppUtils.sendSampleBroadCast(theActivity, AttendanceManager.ACTION_UPDATE_ATTANDENCE_SET_LIST);
                        theActivity.finish();
                    }
                    break;
                case EVENT_SET_USER:
                    theActivity.mNewSetAttendancePresenter.setCopyer();
                    break;
                case EVENT_SET_REMIND:
                    theActivity.mNewSetAttendancePresenter.setRemind();
                    break;
                case AttdenanceAsks.EVENT_GET_SET_DETILA:
                    theActivity.waitDialog.hide();
                    AttdencePrase.praseDetial((NetObject) msg.obj,theActivity,theActivity.mReminds);
                    theActivity.mNewSetAttendancePresenter.initDetial();
                    break;
                case AttdenanceAsks.EVENT_SET_DELETE_SUCCESS:
                    theActivity.waitDialog.hide();
                    if(AttdencePrase.praseData((NetObject) msg.obj,theActivity)) {
                        AppUtils.showMessage(theActivity,theActivity.getString(R.string.keyword_savesuccess));
                        AppUtils.sendSampleBroadCast(theActivity, AttendanceManager.ACTION_UPDATE_ATTANDENCE_SET_LIST);
                        theActivity.finish();
                    }
                    break;
                case AttdenanceAsks.EVENT_SET_SAVE_SUCCESS:
                    theActivity.waitDialog.hide();
                    if(AttdencePrase.praseData((NetObject) msg.obj,theActivity)) {
                        AppUtils.showMessage(theActivity,theActivity.getString(R.string.keyword_savesuccess));
                        AppUtils.sendSampleBroadCast(theActivity, AttendanceManager.ACTION_UPDATE_ATTANDENCE_SET_LIST);
                        theActivity.finish();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
