package intersky.task.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.ProjectStageAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.handler.TaskStructureHandler;
import intersky.task.view.activity.TaskStructureActivity;

/**
 * Created by xpx on 2017/8/4.
 */



public class TaskStructureReceiver extends BaseReceiver {

    public Handler mHandler;

    public TaskStructureReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        this.intentFilter.addAction(TaskStructureActivity.ACTION_SET_PROJECT);
        this.intentFilter.addAction(TaskStructureActivity.ACTION_SET_STAGE);
        this.intentFilter.addAction(TaskStructureActivity.ACTION_SET_PARENT);
        intentFilter.addAction(ProjectAsks.ACTION_PROJECT_SET_NEME);
        intentFilter.addAction(ProjectStageAsks.ACTION_PROJECT_STAGE_UPDATE);

        intentFilter.addAction(TaskAsks.ACTION_TASK_NAME_SUCCESS);
        intentFilter.addAction(TaskAsks.ACTION_TASK_FINSH);
        intentFilter.addAction(TaskAsks.ACTION_TASK_ADD);
        intentFilter.addAction(TaskAsks.ACTION_TASK_LEADER);
        intentFilter.addAction(TaskAsks.ACTION_TASK_CHANGE_STAGE);
        intentFilter.addAction(TaskAsks.ACTION_TASK_CHANGE_PROJECT);
        intentFilter.addAction(TaskAsks.ACTION_TASK_PARENT);


        intentFilter.addAction(ProjectAsks.ACTION_DELETE_PROJECT);


        intentFilter.addAction(TaskAsks.ACTION_EXIT_TASK);
        intentFilter.addAction(TaskAsks.ACTION_DELETE_TASK);
        intentFilter.addAction(TaskManager.ACTION_TASK_UPDATE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(TaskStructureActivity.ACTION_SET_PROJECT))
        {
            if(mHandler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = TaskStructureHandler.SET_PROJECT;
                mHandler.sendMessage(message);
            }
        }
        else if (intent.getAction().equals(TaskStructureActivity.ACTION_SET_STAGE))
        {
            if(mHandler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = TaskStructureHandler.SET_STAGE;
                mHandler.sendMessage(message);
            }
        }
        else if (intent.getAction().equals(TaskStructureActivity.ACTION_SET_PARENT))
        {
            if(mHandler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = TaskStructureHandler.SET_PARENT;
                mHandler.sendMessage(message);
            }
        }
        else if (intent.getAction().equals(TaskAsks.ACTION_TASK_NAME_SUCCESS)|| intent.getAction().equals(TaskAsks.ACTION_TASK_FINSH)
                || intent.getAction().equals(TaskAsks.ACTION_TASK_ADD)|| intent.getAction().equals(TaskAsks.ACTION_TASK_LEADER)
                || intent.getAction().equals(TaskAsks.ACTION_TASK_CHANGE_STAGE)|| intent.getAction().equals(TaskAsks.ACTION_TASK_CHANGE_PROJECT)
                || intent.getAction().equals(TaskAsks.ACTION_TASK_PARENT) || intent.getAction().equals(TaskManager.ACTION_TASK_UPDATE))
        {
            if(mHandler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = TaskStructureHandler.UPDATA_TASK;
                mHandler.sendMessage(message);
            }
        }
        else if (intent.getAction().equals(ProjectAsks.ACTION_PROJECT_SET_NEME)||intent.getAction().equals(ProjectStageAsks.ACTION_PROJECT_STAGE_UPDATE))
        {
            if(mHandler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = TaskStructureHandler.UPDATA_PROJECT;
                mHandler.sendMessage(message);
            }
        }
        else if (intent.getAction().equals(TaskAsks.ACTION_EXIT_TASK))
        {
            if(mHandler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = TaskStructureHandler.TASK_DELETE;
                mHandler.sendMessage(message);
            }
        }

    }
}
