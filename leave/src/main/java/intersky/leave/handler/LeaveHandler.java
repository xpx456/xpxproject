package intersky.leave.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.appbase.bus.Bus;
import intersky.apputils.AppUtils;
import intersky.leave.LeaveManager;
import intersky.leave.asks.LeaveAsks;
import intersky.leave.entity.Leave;
import intersky.leave.prase.LeavePrase;
import intersky.leave.view.activity.LeaveActivity;
import intersky.oa.OaUtils;
import intersky.xpxnet.net.NetObject;

//02
public class LeaveHandler extends Handler {

    public static final int EVENT_SET_TYPE = 3190203;
    public static final int EVENT_SET_SEND = 3190200;
    public static final int EVENT_SET_COPY = 3190201;
    public static final int EVENT_ADD_PIC = 3190202;
    public static final int EVENT_LEAVE_SET_CONTENT = 3130004;
    public LeaveActivity theActivity;

    public LeaveHandler(LeaveActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case LeaveAsks.EVENT_GET_SAVE_SUCCESS:
                    theActivity.waitDialog.hide();
                    theActivity.issub = false;
                    if(LeavePrase.praseData((NetObject) msg.obj,theActivity)) {
                        theActivity.finish();
                        NetObject netObject = (NetObject) msg.obj;
                        Leave leave = (Leave) ((NetObject) msg.obj).item;
                        if(leave.lid.length() == 0)
                        {
                            Bus.callData(theActivity,"function/updateOahit");
                            LeaveManager.getInstance().upDateHit();
                            LeaveManager.getInstance().sendLeaveAdd();
                        }
                        else
                        {
                            LeaveManager.getInstance().sendLeaveUpdate(leave.lid);
                        }
                        theActivity.finish();
                    }
                    break;
                case OaUtils.EVENT_UPLOAD_FILE_RESULT:
                    theActivity.mLeavePresenter.doSave((String) msg.obj);
                    theActivity.waitDialog.hide();
                    break;
                case OaUtils.EVENT_UPLOAD_FILE_RESULT_FAIL:
                    theActivity.waitDialog.hide();
                    AppUtils.showMessage(theActivity, (String) msg.obj);
                    break;
                case EVENT_SET_COPY:
                    theActivity.mLeavePresenter.setCopyer();
                    break;
                case EVENT_SET_SEND:
                    theActivity.mLeavePresenter.setSender();
                    break;
                case EVENT_SET_TYPE:
                    theActivity.mLeavePresenter.settype();
                    break;
                case EVENT_ADD_PIC:
                    theActivity.waitDialog.hide();
                    theActivity.mLeavePresenter.setpic((Intent) msg.obj);
                    break;
                case EVENT_LEAVE_SET_CONTENT:
                    theActivity.waitDialog.hide();
                    theActivity.mReasonText.setText((String) msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
