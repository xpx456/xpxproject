package intersky.attendance.handler;

import android.os.Handler;
import android.os.Message;

import intersky.attendance.asks.AttdenanceAsks;
import intersky.attendance.prase.AttdencePrase;
import intersky.attendance.view.activity.SetAttendanceActivity;
import intersky.xpxnet.net.NetObject;

//02
public class SetAttendanceHandler extends Handler {

    public static final int EVENT_SET_LIST_UP = 3160200;

    public SetAttendanceActivity theActivity;

    public SetAttendanceHandler(SetAttendanceActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case AttdenanceAsks.EVENT_GET_SET_LIST_SUCCESS:
                    theActivity.waitDialog.hide();
                    AttdencePrase.praseSetList((NetObject) msg.obj,theActivity,theActivity.mAttdanceSets);
                    theActivity.mAttdanceSetAdapter.notifyDataSetChanged();
                    break;
                case EVENT_SET_LIST_UP:
                    theActivity.mAttdanceSets.clear();
                    AttdenanceAsks.getSetList(theActivity.mSetAttendancePresenter.mSetAttendanceHandler,theActivity);
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
