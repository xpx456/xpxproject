package intersky.task.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import intersky.appbase.BaseReceiver;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.handler.TaskManagerHandler;

/**
 * Created by xpx on 2017/8/4.
 */



public class TaskManagerReceiver extends BaseReceiver {


    public Handler mHandler;

    public TaskManagerReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(ProjectAsks.ACTION_UPDATA_PROJECT_LIST_DATA);
        intentFilter.addAction(ProjectAsks.ACTION_PROJECT_SET_NEME);
        intentFilter.addAction(ProjectAsks.ACTION_PROJECT_SET_LEADER);
        intentFilter.addAction(ProjectAsks.ACTION_CREAT_PROJECT);
        intentFilter.addAction(ProjectAsks.ACTION_DELETE_PROJECT);

        intentFilter.addAction(TaskAsks.ACTION_TASK_NAME_SUCCESS);
        intentFilter.addAction(TaskAsks.ACTION_DELETE_TASK);
        intentFilter.addAction(TaskAsks.ACTION_TASK_FINSH);
        intentFilter.addAction(TaskAsks.ACTION_TASK_ADD);
        intentFilter.addAction(TaskAsks.ACTION_TASK_LEADER);
        intentFilter.addAction(TaskAsks.ACTION_TASK_CHANGE_STAGE);
        intentFilter.addAction(TaskAsks.ACTION_TASK_CHANGE_PROJECT);
        intentFilter.addAction(TaskAsks.ACTION_TASK_PARENT);
        intentFilter.addAction(TaskAsks.ACTION_TASK_TAG);
        intentFilter.addAction(TaskAsks.ACTION_TASK_DES_SUCCESS);
        intentFilter.addAction(TaskAsks.ACTION_TASK_TIME);
        intentFilter.addAction(TaskAsks.ACTION_TASK_LOCK);
        intentFilter.addAction(TaskAsks.ACTION_TASK_STARE);
        intentFilter.addAction(TaskAsks.ACTION_EXIT_TASK);
        intentFilter.addAction(TaskAsks.ACTION_TASK_ADD_MEMBER);
        intentFilter.addAction(TaskManager.ACTION_TASK_UPDATE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(ProjectAsks.ACTION_UPDATA_PROJECT_LIST_DATA)) {
            if(mHandler != null) {
                mHandler.sendEmptyMessage(TaskManagerHandler.UPDATA_PROJECT_LIST_VIEW);
            }
        }
        else if(intent.getAction().equals(TaskAsks.ACTION_TASK_NAME_SUCCESS)||intent.getAction().equals(TaskAsks.ACTION_DELETE_TASK)
                ||intent.getAction().equals(TaskAsks.ACTION_TASK_FINSH)||intent.getAction().equals(TaskAsks.ACTION_TASK_ADD)
                ||intent.getAction().equals(TaskAsks.ACTION_TASK_LEADER)||intent.getAction().equals(TaskAsks.ACTION_TASK_CHANGE_STAGE)
                ||intent.getAction().equals(TaskAsks.ACTION_TASK_CHANGE_PROJECT)||intent.getAction().equals(TaskAsks.ACTION_TASK_PARENT)
                ||intent.getAction().equals(TaskAsks.ACTION_TASK_TAG)||intent.getAction().equals(TaskAsks.ACTION_TASK_DES_SUCCESS)
                ||intent.getAction().equals(TaskAsks.ACTION_TASK_TIME)||intent.getAction().equals(TaskAsks.ACTION_TASK_LOCK)
                ||intent.getAction().equals(TaskAsks.ACTION_TASK_LOCK)||intent.getAction().equals(TaskAsks.ACTION_TASK_STARE)
                ||intent.getAction().equals(TaskAsks.ACTION_EXIT_TASK)||intent.getAction().equals(TaskAsks.ACTION_TASK_ADD_MEMBER)
                ||intent.getAction().equals(TaskManager.ACTION_TASK_UPDATE))
        {
            if(mHandler != null) {
                mHandler.sendEmptyMessage(TaskManagerHandler.UPDATA_TASK_LIST_DATA);
            }
        }
        else if(intent.getAction().equals(ProjectAsks.ACTION_UPDATA_PROJECT_LIST_DATA)||intent.getAction().equals(ProjectAsks.ACTION_PROJECT_SET_NEME)
                ||intent.getAction().equals(ProjectAsks.ACTION_PROJECT_SET_LEADER)||intent.getAction().equals(ProjectAsks.ACTION_CREAT_PROJECT)
                ||intent.getAction().equals(ProjectAsks.ACTION_DELETE_PROJECT))
        {
            if(mHandler != null) {
                mHandler.sendEmptyMessage(TaskManagerHandler.UPDATA_PROJECT_LIST_DATA);
            }
        }

    }
}
