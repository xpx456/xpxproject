package intersky.leave.bus;

import android.content.Context;
import android.text.TextUtils;

import intersky.appbase.bus.BusObject;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Register;
import intersky.leave.LeaveManager;
import intersky.oa.OaUtils;
import intersky.xpxnet.net.Service;

/**
 * @author Zhenhua on 2017/12/8.
 * @email zhshan@ctrip.com ^.^
 */

public class LeaveBusObject extends BusObject {
    public LeaveBusObject(String host) {
        super(host);
    }

    @Override
    public Object doDataJob(Context context, String bizName, Object... var3) {
        if(TextUtils.equals(bizName, "leave/startLeave")) {
            LeaveManager.getInstance().startLeave(context);
            return null;
        }
        else if(TextUtils.equals(bizName, "leave/startDetialConversation")) {
            LeaveManager.getInstance().startDetial(context,(String)var3[0]);
            return null;
        }
        else if(TextUtils.equals(bizName, "leave/startNewLeave")) {
            LeaveManager.getInstance().newLeave(context);
            return null;
        }
        else if(TextUtils.equals(bizName, "leave/sendUpdate")) {
            LeaveManager.getInstance().sendLeaveUpdate((String)var3[0]);
            return null;
        }
        return null;
    }
}
