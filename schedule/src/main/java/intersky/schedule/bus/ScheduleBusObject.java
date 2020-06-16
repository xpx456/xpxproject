package intersky.schedule.bus;

import android.content.Context;
import android.text.TextUtils;

import intersky.appbase.bus.BusObject;
import intersky.appbase.entity.Account;
import intersky.schedule.ScheduleManager;

/**
 * @author Zhenhua on 2017/12/8.
 * @email zhshan@ctrip.com ^.^
 */

public class ScheduleBusObject extends BusObject {
    public ScheduleBusObject(String host) {
        super(host);
    }

    @Override
    public Object doDataJob(Context context, String bizName, Object... var3) {
        if(TextUtils.equals(bizName, "schedule/startMain")) {
            ScheduleManager.getInstance().startScheduleMain(context);
            return null;
        }
        else if(TextUtils.equals(bizName, "schedule/startEvent")) {
            ScheduleManager.getInstance().startEvent(context);
            return null;
        }
        return null;
    }
}
