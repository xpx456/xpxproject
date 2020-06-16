package intersky.attendance.bus;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import intersky.attendance.AttendanceManager;
import intersky.attendance.view.activity.AttendanceActivity;
import intersky.appbase.bus.BusObject;

/**
 * @author Zhenhua on 2017/12/8.
 * @email zhshan@ctrip.com ^.^
 */

public class AttendanceBusObject extends BusObject {
    public AttendanceBusObject(String host) {
        super(host);
    }

    @Override
    public Object doDataJob(Context context, String bizName, Object... var3) {
        if(TextUtils.equals(bizName, "attendance/startAttdence")) {
            Intent intent = new Intent(context, AttendanceActivity.class);
            if(AttendanceManager.getInstance().mapManager != null)
            {
//                AttendanceManager.getInstance().mapManager.startLocation();
                context.startActivity(intent);
            }

            return null;
        }



        return null;
    }
}
