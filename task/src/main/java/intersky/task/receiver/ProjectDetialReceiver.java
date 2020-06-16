package intersky.task.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.ProjectReplyAsks;
import intersky.task.handler.ProjectDetialHandler;
import intersky.task.view.activity.ProjectDetialActivity;
import intersky.task.view.activity.TaskDetialActivity;

/**
 * Created by xpx on 2017/8/4.
 */



public class ProjectDetialReceiver extends BaseReceiver {

    public Handler mHandler;

    public ProjectDetialReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(ProjectDetialActivity.ACTION_SET_PROJECT_DES);
        intentFilter.addAction(ProjectDetialActivity.ACTION_SET_PROJECT_LEADER);

        intentFilter.addAction(ProjectAsks.ACTION_PROJECT_SET_NEME);
        intentFilter.addAction(ProjectAsks.ACTION_PROJECT_SET_LEADER);
        intentFilter.addAction(ProjectAsks.ACTION_SET_DES);
        intentFilter.addAction(ProjectAsks.ACTION_PROJECT_ADD_MEMBER);
        intentFilter.addAction(ProjectAsks.ACTION_SET_PROJECT_OTHRE);
        intentFilter.addAction(ProjectAsks.ACTION_PROJECT_REMOVE_MEMBER);
        intentFilter.addAction(ProjectAsks.ACTION_DELETE_PROJECT);
        intentFilter.addAction(ProjectReplyAsks.ACTION_PROJECT_REPLY);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(TaskDetialActivity.ACTION_SET_DES)) {
            Message msg = new Message();
            msg.what = ProjectDetialHandler.SET_DES;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals(TaskDetialActivity.ACTION_SET_LEADER)) {
            Message msg = new Message();
            msg.what = ProjectDetialHandler.SET_LEADER;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals(ProjectAsks.ACTION_PROJECT_SET_NEME)||
                intent.getAction().equals(ProjectAsks.ACTION_PROJECT_SET_LEADER)||
                intent.getAction().equals(ProjectAsks.ACTION_SET_DES)||
                intent.getAction().equals(ProjectAsks.ACTION_PROJECT_ADD_MEMBER) ||
                intent.getAction().equals(ProjectAsks.ACTION_SET_PROJECT_OTHRE)) {
            Message msg = new Message();
            msg.what = ProjectDetialHandler.UPDATA_DETIAL;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals(ProjectAsks.ACTION_DELETE_PROJECT)) {
            Message msg = new Message();
            msg.what = ProjectDetialHandler.PROJECT_DELETE;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals(ProjectAsks.ACTION_PROJECT_REMOVE_MEMBER)) {
            Message msg = new Message();
            msg.what = ProjectDetialHandler.PROJECT_EXIST;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals(ProjectReplyAsks.ACTION_PROJECT_REPLY)) {
            Message msg = new Message();
            msg.what = ProjectDetialHandler.UPDATA_REPLY;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
    }
}
