package intersky.workreport.bus;

import android.content.Context;
import android.text.TextUtils;

import intersky.appbase.bus.BusObject;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Register;
import intersky.workreport.WorkReportManager;
import intersky.workreport.entity.Report;

/**
 * @author Zhenhua on 2017/12/8.
 * @email zhshan@ctrip.com ^.^
 */

public class WorkreportBusObject extends BusObject {
    public WorkreportBusObject(String host) {
        super(host);
    }

    @Override
    public Object doDataJob(Context context, String bizName, Object... var3) {
         if(TextUtils.equals(bizName, "workreport/startReportMain")) {
            WorkReportManager.getInstance().startReportMain(context);
            return null;
        }
        else if(TextUtils.equals(bizName, "workreport/startDetialConversation")) {
            WorkReportManager.getInstance().startDetial(context,(String)var3[0]);
            return null;
        }
        else if(TextUtils.equals(bizName, "workreport/startDetial")) {
            WorkReportManager.getInstance().startDetial(context,(Report)var3[0]);
            return null;
        }
        else if(TextUtils.equals(bizName, "workreport/sendReportUpdate")) {
            WorkReportManager.getInstance().sendReportUpdate((String)var3[0]);
            return null;
        }
        return null;
    }
}
