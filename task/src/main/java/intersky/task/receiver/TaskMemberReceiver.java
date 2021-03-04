package intersky.task.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.handler.TaskMemberHandler;
import intersky.task.view.activity.TaskMemberActivity;

/**
 * Created by xpx on 2017/8/4.
 */



public class TaskMemberReceiver extends BaseReceiver {


    public Handler mHandler;

    public TaskMemberReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        this.intentFilter.addAction(TaskMemberActivity.ACTION_ADD_MEMBER);
        intentFilter.addAction(ProjectAsks.ACTION_PROJECT_SET_LEADER);
        intentFilter.addAction(ProjectAsks.ACTION_PROJECT_REMOVE_MEMBER);
        intentFilter.addAction(ProjectAsks.ACTION_MEMBER_ACCESS);
        intentFilter.addAction(ProjectAsks.ACTION_PROJECT_ADD_MEMBER);
        intentFilter.addAction(ProjectAsks.ACTION_DELETE_PROJECT);
        intentFilter.addAction(TaskAsks.ACTION_TASK_LEADER);
        intentFilter.addAction(TaskAsks.ACTION_TASK_ADD_MEMBER);
        intentFilter.addAction(TaskAsks.ACTION_EXIT_TASK);
        intentFilter.addAction(TaskManager.ACTION_TASK_UPDATE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(TaskMemberActivity.ACTION_ADD_MEMBER))
        {
            Message smsg = new Message();
            smsg.what = TaskMemberHandler.ADD_MEMBER;
            smsg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(smsg);
        }
        else if(intent.getAction().equals(TaskAsks.ACTION_TASK_LEADER) || intent.getAction().equals(TaskAsks.ACTION_TASK_ADD_MEMBER)
                || intent.getAction().equals(TaskManager.ACTION_TASK_UPDATE))
        {
            Message smsg = new Message();
            smsg.what = TaskMemberHandler.UPDATA_TASK;
            smsg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(smsg);
        }
        else if(intent.getAction().equals(TaskAsks.ACTION_EXIT_TASK))
        {
            Message smsg = new Message();
            smsg.what = TaskMemberHandler.TASK_EXIST;
            smsg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(smsg);
        }
        else if(intent.getAction().equals(ProjectAsks.ACTION_PROJECT_SET_LEADER)
                ||intent.getAction().equals(ProjectAsks.ACTION_MEMBER_ACCESS)||intent.getAction().equals(ProjectAsks.ACTION_PROJECT_ADD_MEMBER)
                ||intent.getAction().equals(ProjectAsks.ACTION_DELETE_PROJECT))
        {
            Message smsg = new Message();
            smsg.what = TaskMemberHandler.UPDATA_PROJECT;
            smsg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(smsg);
        }
        else if(intent.getAction().equals(ProjectAsks.ACTION_PROJECT_REMOVE_MEMBER))
        {
            Message smsg = new Message();
            smsg.what = TaskMemberHandler.PROJECT_EXIST;
            smsg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(smsg);
        }
    }
}
