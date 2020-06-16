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
import intersky.task.handler.TaskCreatHandler;
import intersky.task.view.activity.TaskCreatActivity;

/**
 * Created by xpx on 2017/8/4.
 */



public class TaskCreatReceiver extends BaseReceiver {


    public Handler mHandler;

    public TaskCreatReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        this.intentFilter.addAction(TaskCreatActivity.ACTION_CREAT_SET_LEADER);
        this.intentFilter.addAction(TaskCreatActivity.ACTION_CREAT_SET_PARENT);
        this.intentFilter.addAction(TaskCreatActivity.ACTION_CREAT_SET_PROJECT);
        this.intentFilter.addAction(TaskCreatActivity.ACTION_CREAT_SET_STAGE);
        this.intentFilter.addAction(TaskCreatActivity.ACTION_ADD_PICTORE);
        this.intentFilter.addAction(TaskCreatActivity.ACTION_ADD_COPYER);
        intentFilter.addAction(ProjectAsks.ACTION_PROJECT_SET_NEME);
        intentFilter.addAction(ProjectAsks.ACTION_DELETE_PROJECT);
        intentFilter.addAction(TaskAsks.ACTION_DELETE_TASK);
        intentFilter.addAction(ProjectStageAsks.ACTION_PROJECT_STAGE_UPDATE);
        intentFilter.addAction(TaskManager.ACTION_TASK_UPDATE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(TaskCreatActivity.ACTION_CREAT_SET_LEADER)) {
            if(mHandler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = TaskCreatHandler.SET_LEADER;
                mHandler.sendMessage(message);
            }
        }
        else if(intent.getAction().equals(TaskCreatActivity.ACTION_CREAT_SET_PROJECT))
        {
            if(mHandler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = TaskCreatHandler.SET_PROJECT;
                mHandler.sendMessage(message);
            }
        }
        else if (intent.getAction().equals(TaskCreatActivity.ACTION_CREAT_SET_STAGE))
        {
            if(mHandler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = TaskCreatHandler.SET_STAGE;
                mHandler.sendMessage(message);
            }
        }
        else if (intent.getAction().equals(TaskCreatActivity.ACTION_CREAT_SET_PARENT))
        {
            if(mHandler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = TaskCreatHandler.SET_PARENT;
                mHandler.sendMessage(message);
            }
        }
        else if (intent.getAction().equals(TaskCreatActivity.ACTION_ADD_PICTORE))
        {
            if(mHandler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = TaskCreatHandler.EVENT_ADD_PIC;
                mHandler.sendMessage(message);
            }
        }
        else if (intent.getAction().equals(TaskCreatActivity.ACTION_ADD_COPYER))
        {
            if(mHandler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = TaskCreatHandler.EVENT_SET_SEND;
                mHandler.sendMessage(message);
            }
        }
        else if (intent.getAction().equals(ProjectAsks.ACTION_PROJECT_SET_NEME))
        {
            if(mHandler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = TaskCreatHandler.UPDATA_NAME;
                mHandler.sendMessage(message);
            }
        }
        else if (intent.getAction().equals(ProjectAsks.ACTION_DELETE_PROJECT))
        {
            if(mHandler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = TaskCreatHandler.DELETE_PROJECT;
                mHandler.sendMessage(message);
            }
        }
        else if (intent.getAction().equals(ProjectStageAsks.ACTION_PROJECT_STAGE_UPDATE))
        {
            if(mHandler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = TaskCreatHandler.UPDATA_PROJECT;
                mHandler.sendMessage(message);
            }
        }
        else if (intent.getAction().equals(TaskAsks.ACTION_DELETE_TASK))
        {
            if(mHandler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = TaskCreatHandler.UPDATA_TASK;
                mHandler.sendMessage(message);
            }
        }
    }
}
