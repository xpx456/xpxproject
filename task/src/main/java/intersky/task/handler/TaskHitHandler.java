package intersky.task.handler;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.AppActivityManager;
import intersky.appbase.BaseActivity;
import intersky.apputils.AppUtils;
import intersky.task.TaskManager;
import intersky.task.asks.TaskAsks;
import intersky.task.entity.Task;
import intersky.task.prase.TaskPrase;
import intersky.xpxnet.net.NetObject;

//07
public class TaskHitHandler extends Handler {

    public Context context;

    public TaskHitHandler(Context context){
        this.context = context;
    }
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case TaskAsks.TASK_HIT_SUCCESS:
                TaskPrase.praseTaskHint((NetObject) msg.obj);
                AppUtils.sendSampleBroadCast(TaskManager.getInstance().context,TaskManager.GET_TASK_HIT);
                break;
            case TaskAsks.TASK_DETIAL_SUCCESS:
                BaseActivity baseActivity = (BaseActivity) AppActivityManager.getInstance().getCurrentActivity();
                baseActivity.waitDialog.hide();
                boolean exist = TaskPrase.praseTaskDetial2((NetObject) msg.obj,context);
                Task task = (Task) ((NetObject) msg.obj).item;
                if(exist = true)
                    TaskManager.getInstance().startDetial(AppActivityManager.getInstance().getCurrentActivity(),task);
                break;
        }

    }
}
