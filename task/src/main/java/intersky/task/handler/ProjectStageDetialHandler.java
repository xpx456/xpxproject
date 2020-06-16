package intersky.task.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.prase.ProjectPrase;
import intersky.task.prase.TaskPrase;
import intersky.task.view.activity.ProjectStageDetialActivity;
import intersky.xpxnet.net.NetObject;
import xpx.com.toolbar.utils.ToolBarHelper;

//03
public class ProjectStageDetialHandler extends Handler {
    public ProjectStageDetialActivity theActivity;

    public static final int UPDATA_PROJECT = 3250300;
    public static final int UPDATA_NAME = 3250301;
    public static final int UPDATA_EXIST = 3250302;
    public static final int UPDATA_DELETE = 3250303;
    public ProjectStageDetialHandler(ProjectStageDetialActivity mProjectStageDetialActivity) {
        theActivity = mProjectStageDetialActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case ProjectAsks.PROJECT_STAGE_SUCCESS:
                theActivity.waitDialog.hide();
                ProjectPrase.praseStage((NetObject) msg.obj,theActivity);
                theActivity.mProjectStageDetialPresenter.initStageView();
                theActivity.mProjectStageDetialPresenter.getTask();
                break;
            case TaskAsks.TASK_LIST_SUCCESS:
                theActivity.waitDialog.hide();
                TaskPrase.praseTask((NetObject) msg.obj,theActivity,theActivity.mProject,theActivity.expentTask);
                theActivity.mProjectStageDetialPresenter.initTaskView();
                break;
            case UPDATA_PROJECT:
                theActivity.mProjectStageDetialPresenter.updataProject((Intent) msg.obj);
                break;
            case UPDATA_NAME:
                theActivity.mProjectStageDetialPresenter.updataName((Intent) msg.obj);
                break;
            case ProjectAsks.PROJECT_ITEM_DETIAL_SUCCESS:
                theActivity.waitDialog.hide();
                ProjectPrase.praseProjectItemDetial((NetObject) msg.obj,theActivity);
                ToolBarHelper.setTitle(theActivity.mActionBar,theActivity.mProject.mName);
                theActivity.waitDialog.show();
                ProjectAsks.getStage(theActivity, theActivity.mProjectStageDetialPresenter.mProjectStageDetialHandler,theActivity.mProject);
                break;
            case UPDATA_EXIST:
                intent = (Intent) msg.obj;
                String id = intent.getStringExtra("projectid");
                String cid1 = intent.getStringExtra("cid");
                if(theActivity.mProject.projectId.equals(id) && TaskManager.getInstance().oaUtils.mAccount.mRecordId.equals(cid1))
                    theActivity.finish();
                break;
            case UPDATA_DELETE:
                intent = (Intent) msg.obj;
                String id1 = intent.getStringExtra("projectid");
                if(theActivity.mProject.projectId.equals(id1))
                    theActivity.finish();
                break;
        }

    }
}
