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
import intersky.task.handler.ProjectStageDetialHandler;

/**
 * Created by xpx on 2017/8/4.
 */



public class ProjectStageDetialReceiver extends BaseReceiver {

    public Handler mHandler;

    public ProjectStageDetialReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        intentFilter = new IntentFilter();
        intentFilter.addAction(ProjectAsks.ACTION_PROJECT_SET_NEME);
        intentFilter.addAction(ProjectAsks.ACTION_DELETE_PROJECT);
        intentFilter.addAction(ProjectStageAsks.ACTION_PROJECT_STAGE_UPDATE);
        intentFilter.addAction(TaskAsks.ACTION_TASK_NAME_SUCCESS);
        intentFilter.addAction(TaskAsks.ACTION_DELETE_TASK);
        intentFilter.addAction(TaskAsks.ACTION_TASK_FINSH);
        intentFilter.addAction(TaskAsks.ACTION_TASK_ADD);
        intentFilter.addAction(TaskAsks.ACTION_TASK_LEADER);
        intentFilter.addAction(TaskAsks.ACTION_TASK_CHANGE_STAGE);
        intentFilter.addAction(TaskAsks.ACTION_TASK_CHANGE_PROJECT);
        intentFilter.addAction(TaskAsks.ACTION_TASK_PARENT);
        intentFilter.addAction(TaskAsks.ACTION_TASK_TAG);
        intentFilter.addAction(TaskListAsks.ACTION_TASK_LIST_UPDATA);

        intentFilter.addAction(ProjectAsks.ACTION_PROJECT_REMOVE_MEMBER);
        intentFilter.addAction(TaskManager.ACTION_TASK_UPDATE);


    }

    @Override
    public void onReceive(Context context, Intent intent) {

         if (intent.getAction().equals(ProjectAsks.ACTION_PROJECT_SET_NEME))
        {
            if(mHandler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = ProjectStageDetialHandler.UPDATA_NAME;
                mHandler.sendMessage(message);
            }
        }
        else if (intent.getAction().equals(ProjectStageAsks.ACTION_PROJECT_STAGE_UPDATE)
                 ||intent.getAction().equals(TaskAsks.ACTION_TASK_NAME_SUCCESS)||intent.getAction().equals(TaskAsks.ACTION_DELETE_TASK)
                 ||intent.getAction().equals(TaskAsks.ACTION_TASK_FINSH)||intent.getAction().equals(TaskAsks.ACTION_TASK_ADD)
                 ||intent.getAction().equals(TaskAsks.ACTION_TASK_LEADER)||intent.getAction().equals(TaskAsks.ACTION_TASK_CHANGE_STAGE)
                 ||intent.getAction().equals(TaskAsks.ACTION_TASK_CHANGE_PROJECT)||intent.getAction().equals(TaskAsks.ACTION_TASK_PARENT)
                 ||intent.getAction().equals(TaskAsks.ACTION_TASK_TAG)||intent.getAction().equals(TaskListAsks.ACTION_TASK_LIST_UPDATA)
                 ||intent.getAction().equals(TaskManager.ACTION_TASK_UPDATE))
        {
            if(mHandler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = ProjectStageDetialHandler.UPDATA_PROJECT;
                mHandler.sendMessage(message);
            }
        }
         else if (intent.getAction().equals(ProjectAsks.ACTION_PROJECT_REMOVE_MEMBER))
         {
             if(mHandler != null) {
                 Message message = new Message();
                 message.obj = intent;
                 message.what = ProjectStageDetialHandler.UPDATA_EXIST;
                 mHandler.sendMessage(message);
             }
         }
         else if (intent.getAction().equals(ProjectAsks.ACTION_DELETE_PROJECT))
         {
             if(mHandler != null) {
                 Message message = new Message();
                 message.obj = intent;
                 message.what = ProjectStageDetialHandler.UPDATA_DELETE;
                 mHandler.sendMessage(message);
             }
         }
    }
}
