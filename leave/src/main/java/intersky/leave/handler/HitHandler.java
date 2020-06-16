package intersky.leave.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.AppActivityManager;
import intersky.appbase.BaseActivity;
import intersky.apputils.AppUtils;
import intersky.leave.LeaveManager;
import intersky.leave.asks.LeaveAsks;
import intersky.leave.entity.Leave;
import intersky.leave.prase.LeavePrase;
import intersky.xpxnet.net.NetObject;

//00
public class HitHandler extends Handler {

    public Context context;

    public HitHandler(Context context){
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {

            switch (msg.what) {
                case LeaveAsks.EVENT_GET_HIT_SUCCESS:
                    LeavePrase.praseHit((NetObject) msg.obj);
                    AppUtils.sendSampleBroadCast(context, LeaveManager.ACTION_LEAVE_UPDATA_HIT);
                    break;
                case LeaveAsks.EVENT_GET_LEAVETYPE_SUCCESS:
                    LeavePrase.praseType((NetObject) msg.obj);
                    AppUtils.sendSampleBroadCast(context, LeaveManager.ACTION_LEAVE_UPDATA_TYPE);
                    break;
                case LeaveAsks.EVENT_GET_DETIAL_SUCCESS:
                    BaseActivity baseActivity = (BaseActivity) AppActivityManager.getInstance().getCurrentActivity();
                    baseActivity.waitDialog.hide();
                    boolean exist = LeavePrase.praseDetial((NetObject) msg.obj, context);
                    Leave leave = (Leave) ((NetObject) msg.obj).item;
                    if(exist == true)
                        LeaveManager.getInstance().startDetial(AppActivityManager.getInstance().getCurrentActivity(),leave);
                    break;
            }
            super.handleMessage(msg);

    }
}
