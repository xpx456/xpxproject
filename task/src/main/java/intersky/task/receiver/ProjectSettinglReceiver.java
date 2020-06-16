package intersky.task.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.task.asks.ProjectAsks;
import intersky.task.handler.ProjectSettingHandler;
import intersky.task.view.activity.ProjectSettingActivity;

/**
 * Created by xpx on 2017/8/4.
 */



public class ProjectSettinglReceiver extends BaseReceiver {


    public Handler mHandler;

    public ProjectSettinglReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(ProjectSettingActivity.ACTION_PROJECT_POWER);
        intentFilter.addAction(ProjectSettingActivity.ACTION_PROJECT_CONTRAL);
        intentFilter.addAction(ProjectAsks.ACTION_DELETE_PROJECT);
        intentFilter.addAction(ProjectAsks.ACTION_PROJECT_REMOVE_MEMBER);
        intentFilter.addAction(ProjectAsks.ACTION_PROJECT_SET_LEADER);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ProjectSettingActivity.ACTION_PROJECT_POWER)) {
            Message msg = new Message();
            msg.what = ProjectSettingHandler.PROJECT_POWER;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals(ProjectSettingActivity.ACTION_PROJECT_CONTRAL)) {
            Message msg = new Message();
            msg.what = ProjectSettingHandler.PROJECT_CONTRAL;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals(ProjectAsks.ACTION_DELETE_PROJECT)) {
            Message msg = new Message();
            msg.what = ProjectSettingHandler.PROJECT_DELETE;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals(ProjectAsks.ACTION_PROJECT_REMOVE_MEMBER)) {
            Message msg = new Message();
            msg.what = ProjectSettingHandler.UPDATA_EXIST;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals(ProjectAsks.ACTION_PROJECT_SET_LEADER)) {
            Message msg = new Message();
            msg.what = ProjectSettingHandler.LEADER_CHANGE;
            msg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
    }
}
