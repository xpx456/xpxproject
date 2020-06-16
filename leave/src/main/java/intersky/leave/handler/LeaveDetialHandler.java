package intersky.leave.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.lang.ref.WeakReference;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.leave.LeaveManager;
import intersky.leave.R;
import intersky.leave.asks.LeaveAsks;
import intersky.leave.entity.Leave;
import intersky.leave.prase.LeavePrase;
import intersky.leave.view.activity.LeaveDetialActivity;
import intersky.oa.OaAsks;
import intersky.xpxnet.net.NetObject;

//01
public class LeaveDetialHandler extends Handler {

    public static final int EVENT_DETIAL_UPDATA = 3190000;
    public static final int EVENT_DETIAL_DELETE= 3190001;
    public LeaveDetialActivity theActivity;

    public LeaveDetialHandler(LeaveDetialActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case LeaveAsks.EVENT_GET_DELETE_SUCCESS:
                    theActivity.waitDialog.hide();
                    if(LeavePrase.praseData((NetObject) msg.obj,theActivity)) {
                        Leave leave = (Leave) ((NetObject) msg.obj).item;
                        LeaveManager.getInstance().sendLeaveDelete(leave.lid);
                        Bus.callData(theActivity,"function/updateOahit");
                        LeaveManager.getInstance().upDateHit();
                        Intent intent = new Intent();
                        intent.putExtra("type", Conversation.CONVERSATION_TYPE_LEAVE);
                        intent.putExtra("detialid",leave.lid);
                        Bus.callData(theActivity,"conversation/setdetialdelete",intent);
                        theActivity.finish();
                    }
                    break;
                case LeaveAsks.EVENT_GET_ACCEPT_SUCCESS:
                    theActivity.waitDialog.hide();
                    if(LeavePrase.praseData((NetObject) msg.obj,theActivity)) {
                        AppUtils.showMessage(theActivity,theActivity.getString(R.string.keyword_leave_approve_success));
                        Leave leave = (Leave) ((NetObject) msg.obj).item;
                        leave.is_approval = 2;
                        LeaveManager.getInstance().sendLeaveUpdate(leave.lid);
                        Bus.callData(theActivity,"function/updateOahit");
                        LeaveManager.getInstance().upDateHit();
                        theActivity.finish();
                    }
                    break;
                case LeaveAsks.EVENT_GET_REFUSE_SUCCESS:
                    theActivity.waitDialog.hide();
                    if(LeavePrase.praseData((NetObject) msg.obj,theActivity)) {
                        AppUtils.showMessage(theActivity,theActivity.getString(R.string.keyword_leave_approve_success));
                        Leave leave = (Leave) ((NetObject) msg.obj).item;
                        leave.is_approval = 3;
                        LeaveManager.getInstance().sendLeaveUpdate(leave.lid);
                        Bus.callData(theActivity,"function/updateOahit");
                        LeaveManager.getInstance().upDateHit();
                        theActivity.finish();
                    }
                    break;
                case OaAsks.EVENT_GET_ATTCHMENT_SUCCESS:
                    theActivity.mImageLayer.setVisibility(View.VISIBLE);
                    LeavePrase.praseAddtchment((NetObject) msg.obj,theActivity.mPics,theActivity.mLeave);
                    theActivity.mLeaveDetialPresenter.praseAttahcmentViews();
                    theActivity.waitDialog.hide();
                    break;
                case LeaveAsks.EVENT_GET_DETIAL_SUCCESS:
                    LeavePrase.praseDetial((NetObject) msg.obj,theActivity);
                    theActivity.mLeaveDetialPresenter.initDetial();
                    LeaveManager.getInstance().register.conversationFunctions.Read(LeaveManager.getInstance().register.typeName
                            ,theActivity.mLeave.lid);
                    theActivity.waitDialog.hide();
                    break;
                case EVENT_DETIAL_UPDATA:
                    theActivity.waitDialog.show();
                    Intent intent = (Intent) msg.obj;
                    String id = intent.getStringExtra("leaveid");
                    if(id.equals(theActivity.mLeave.lid))
                    LeaveAsks.getDetial(theActivity.mLeaveDetialPresenter.mLeaveDetialHandler,theActivity,theActivity.mLeave);
                    break;
                case EVENT_DETIAL_DELETE:
                    theActivity.waitDialog.show();
                    Intent intent1 = (Intent) msg.obj;
                    String id1 = intent1.getStringExtra("leaveid");
                    if(id1.equals(theActivity.mLeave.lid))
                        theActivity.finish();
                    break;

            }
            super.handleMessage(msg);
        }
    }
}
