package intersky.task.bus;

import android.content.Context;
import android.text.TextUtils;

import intersky.appbase.bus.BusObject;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Register;
import intersky.task.TaskManager;

/**
 * @author Zhenhua on 2017/12/8.
 * @email zhshan@ctrip.com ^.^
 */

public class TaskBusObject extends BusObject {
    public TaskBusObject(String host) {
        super(host);
    }

    @Override
    public Object doDataJob(Context context, String bizName, Object... var3) {

        if(TextUtils.equals(bizName, "task/getTaskHit")) {
            TaskManager.getInstance().getTaskHit();
            return null;
        }
        else if (TextUtils.equals(bizName, "task/getTaskHitCount")) {
            if(TaskManager.getInstance() != null)
            {
                return TaskManager.getInstance().taskHit;
            }
            else
            {
                return 0;
            }

        }

        else if (TextUtils.equals(bizName, "task/updataTask")) {
            TaskManager.getInstance().sendTaskUpdate((String) var3[0]);
        }
        else if (TextUtils.equals(bizName, "task/startManager")) {
            TaskManager.getInstance().startTaskManager(context);
        }
        else if (TextUtils.equals(bizName, "task/startTasknew")) {
            TaskManager.getInstance().startTaskNew(context);
        }
        else if (TextUtils.equals(bizName, "task/startAddProject")) {
            TaskManager.getInstance().startAddProject(context);
        }
        return null;
    }
}
