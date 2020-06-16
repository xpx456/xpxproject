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
import intersky.task.asks.TaskListAsks;
import intersky.task.asks.TaskReplyAsks;
import intersky.task.handler.TaskDetialHandler;
import intersky.task.view.activity.TaskDetialActivity;

/**
 * Created by xpx on 2017/8/4.
 */



public class TaskDetialReceiver extends BaseReceiver {


    public Handler mHandler;

    public TaskDetialReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(TaskDetialActivity.ACTION_TASK_TAG);
        intentFilter.addAction(TaskDetialActivity.ACTION_SET_DES);
        intentFilter.addAction(TaskDetialActivity.ACTION_SET_LEADER);
        intentFilter.addAction(TaskDetialActivity.ACTION_ADD_TASK_PICTURE);

        intentFilter.addAction(ProjectAsks.ACTION_PROJECT_SET_NEME);
        intentFilter.addAction(ProjectAsks.ACTION_DELETE_PROJECT);
        intentFilter.addAction(ProjectStageAsks.ACTION_PROJECT_STAGE_UPDATE);

        intentFilter.addAction(TaskAsks.ACTION_TASK_NAME_SUCCESS);
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
        intentFilter.addAction(TaskAsks.ACTION_TASK_ADD_MEMBER);
        intentFilter.addAction(TaskListAsks.ACTION_TASK_LIST_UPDATA);
        intentFilter.addAction(TaskReplyAsks.ACTION_TASK_REPLY_UPDATA);
        intentFilter.addAction(TaskAsks.ACTION_EXIT_TASK);
        intentFilter.addAction(TaskAsks.ACTION_DELETE_TASK);
        intentFilter.addAction(TaskAsks.ACTION_TASK_CONTRAL);
        intentFilter.addAction(TaskAsks.ACTION_TASK_ATTACHMENT);
        intentFilter.addAction(TaskManager.ACTION_TASK_UPDATE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(TaskDetialActivity.ACTION_TASK_TAG)) {
            Message msg = new Message();
            msg.what = TaskDetialHandler.SET_TAG;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        } else if (intent.getAction().equals(TaskDetialActivity.ACTION_SET_DES)) {
            Message msg = new Message();
            msg.what = TaskDetialHandler.SET_DES;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals(TaskDetialActivity.ACTION_SET_LEADER)) {
            Message msg = new Message();
            msg.what = TaskDetialHandler.SET_LEADER;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals(TaskDetialActivity.ACTION_ADD_TASK_PICTURE)) {
            Message msg = new Message();
            msg.what = TaskDetialHandler.ADD_TASK_PIC;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals(ProjectAsks.ACTION_PROJECT_SET_NEME)) {
            Message msg = new Message();
            msg.what = TaskDetialHandler.UPDATA_NAME;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals(ProjectAsks.ACTION_DELETE_PROJECT)) {
            Message msg = new Message();
            msg.what = TaskDetialHandler.PROJECT_DELETE;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals(ProjectStageAsks.ACTION_PROJECT_STAGE_UPDATE)) {
            Message msg = new Message();
            msg.what = TaskDetialHandler.PROJECT_DETIAL_UPDATA;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals(TaskAsks.ACTION_TASK_NAME_SUCCESS) ||intent.getAction().equals(TaskAsks.ACTION_TASK_FINSH)
                ||intent.getAction().equals(TaskAsks.ACTION_TASK_ADD) ||intent.getAction().equals(TaskAsks.ACTION_TASK_LEADER)
                ||intent.getAction().equals(TaskAsks.ACTION_TASK_CHANGE_STAGE) ||intent.getAction().equals(TaskAsks.ACTION_TASK_CHANGE_PROJECT)
                ||intent.getAction().equals(TaskAsks.ACTION_TASK_PARENT) ||intent.getAction().equals(TaskAsks.ACTION_TASK_TAG)
                ||intent.getAction().equals(TaskAsks.ACTION_TASK_DES_SUCCESS) ||intent.getAction().equals(TaskAsks.ACTION_TASK_TIME)
                ||intent.getAction().equals(TaskAsks.ACTION_TASK_LOCK)||intent.getAction().equals(TaskAsks.ACTION_TASK_STARE)
                ||intent.getAction().equals(TaskAsks.ACTION_TASK_ADD_MEMBER)||intent.getAction().equals(TaskListAsks.ACTION_TASK_LIST_UPDATA)
                ||intent.getAction().equals(TaskAsks.ACTION_TASK_CONTRAL)||intent.getAction().equals(TaskAsks.ACTION_TASK_ATTACHMENT)
                ||intent.getAction().equals(TaskManager.ACTION_TASK_UPDATE)) {
            Message msg = new Message();
            msg.what = TaskDetialHandler.TASK_DETIAL_UPDATA;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals(TaskAsks.ACTION_EXIT_TASK)) {
            Message msg = new Message();
            msg.what = TaskDetialHandler.TASK_EXIST;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals(TaskAsks.ACTION_DELETE_TASK)) {
            Message msg = new Message();
            msg.what = TaskDetialHandler.TASK_DELETE;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals(TaskReplyAsks.ACTION_TASK_REPLY_UPDATA)) {
            Message msg = new Message();
            msg.what = TaskDetialHandler.UPDATA_REPLY;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
    }
}
