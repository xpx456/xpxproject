package intersky.task.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.util.ArrayList;

import intersky.apputils.AppUtils;
import intersky.oa.OaUtils;
import intersky.select.SelectManager;
import intersky.select.SelectReceiver;
import intersky.select.entity.Select;
import intersky.task.R;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.ProjectFileAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.entity.Project;
import intersky.task.entity.Task;
import intersky.task.prase.ProjectPrase;
import intersky.task.prase.TaskPrase;
import intersky.task.view.activity.TaskCreatActivity;
import intersky.xpxnet.net.NetObject;
//05
public class TaskCreatHandler extends Handler {

    public static final int SET_LEADER = 3250503;
    public static final int SET_PROJECT = 3250500;
    public static final int SET_STAGE = 3250501;
    public static final int SET_PARENT = 3250502;
    public static final int EVENT_SET_SEND = 3250504;
    public static final int EVENT_ADD_PIC = 3250505;
    public static final int UPDATA_PROJECT = 3250506;
    public static final int UPDATA_TASK = 3250507;
    public static final int UPDATA_NAME = 3250508;
    public static final int DELETE_PROJECT = 3250509;
    TaskCreatActivity theActivity;

    public TaskCreatHandler(TaskCreatActivity mTaskCreatActivity) {
        theActivity = mTaskCreatActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {

            case ProjectAsks.PROJECT_LIST_SUCCESS:
                theActivity.waitDialog.hide();
                ArrayList<Project> heads = ProjectPrase.praseProjects((NetObject) msg.obj,theActivity);
                if(heads.size() > 0 )
                {
                    ProjectFileAsks.getFileNames(theActivity,theActivity.mTaskCreatPresenter.mTaskCreatHandler,heads);
                    theActivity.waitDialog.show();
                }
                theActivity.sendBroadcast(new Intent(ProjectAsks.ACTION_UPDATA_PROJECT_LIST_DATA));
                theActivity.sendBroadcast(new Intent(SelectReceiver.ACTION_UPDATA_SELECT_LIST));
                break;
            case SET_LEADER:
                theActivity.mTaskCreatPresenter.setDuty((Intent) msg.obj);
                break;
            case SET_PROJECT:
                if(SelectManager.getInstance().mCustomSignal == null)
                {
                    theActivity.mTask.projectId = "";
                    theActivity.mTask.projectName = "";
                    theActivity.mTask.stageId = "";
                    theActivity.mTask.stageName = "";
                    theActivity.mProjectItemModel = null;
                    theActivity.mselectmStage = null;
                    theActivity.proJect.setText(theActivity.getString(R.string.task_creat_project));
                    theActivity.stageArea.setVisibility(View.GONE);
                }
                else
                {
                    Project project = (Project) SelectManager.getInstance().mCustomSignal.object;
                    if(!theActivity.mTask.projectId.equals(project.projectId)) {
                        theActivity.mProjectItemModel = project;
                        theActivity.mTask.projectId = theActivity.mProjectItemModel.projectId;
                        theActivity.mTask.projectName = theActivity.mProjectItemModel.mName;
                        theActivity.proJect.setText(theActivity.mProjectItemModel.mName);
                        theActivity.stageArea.setVisibility(View.VISIBLE);
                        theActivity.waitDialog.show();
                        ProjectAsks.getProjectItemDetial(theActivity,theActivity.mTaskCreatPresenter.mTaskCreatHandler,theActivity.mProjectItemModel);
                    }
                }
                break;
            case SET_STAGE:
                Select stage = SelectManager.getInstance().mSignal;
                if(!theActivity.mTask.stageId.equals(stage.mId)) {
                    theActivity.mTask.stageId = stage.mId;
                    theActivity.mTask.stageName = theActivity.mTaskCreatPresenter.getStageName(theActivity.mTask.stageId);
                    theActivity.stage.setText(theActivity.mTask.stageName);
                    if (theActivity.stage.getVisibility() == View.GONE) {
                        theActivity.stage.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case SET_PARENT:
                Task parentTask = (Task) SelectManager.getInstance().mCustomSignal.object;
                if(!theActivity.mTask.parentId.equals(parentTask.taskId)) {
                    theActivity.parentTask = parentTask;
                }
                break;
            case ProjectAsks.PROJECT_ITEM_DETIAL_SUCCESS:
                ProjectPrase.praseProjectItemDetial((NetObject) msg.obj,theActivity);
                theActivity.mTaskCreatPresenter.initStages();
                theActivity.waitDialog.hide();
                break;
            case EVENT_ADD_PIC:
                theActivity.waitDialog.hide();
                theActivity.mTaskCreatPresenter.setpic((Intent) msg.obj);
                break;
            case OaUtils.EVENT_UPLOAD_FILE_RESULT:
                theActivity.mTaskCreatPresenter.doAddTask((String) msg.obj);
                theActivity.waitDialog.hide();
                break;
            case OaUtils.EVENT_UPLOAD_FILE_RESULT_FAIL:
                theActivity.waitDialog.hide();
                AppUtils.showMessage(theActivity, (String) msg.obj);
                break;
            case TaskAsks.TASK_ADD_SUCCESS:
                theActivity.waitDialog.hide();
                Task task = TaskPrase.praseAdd((NetObject) msg.obj,theActivity);
                if(task != null) {
                    intent = new Intent(TaskAsks.ACTION_TASK_ADD);
                    intent.putExtra("taskid",task.taskId);
                    intent.putExtra("projectid",task.projectId);
                    theActivity.sendBroadcast(intent);
                    AppUtils.showMessage(theActivity,theActivity.getString(R.string.creat_sccess));
                    theActivity.finish();
                }
                break;
            case EVENT_SET_SEND:
                theActivity.mTaskCreatPresenter.setCopyer();
                break;
            case UPDATA_PROJECT:
                theActivity.mTaskCreatPresenter.updataProject((Intent) msg.obj);
                break;
            case UPDATA_TASK:
                theActivity.mTaskCreatPresenter.updataTask((Intent) msg.obj);
                break;
            case TaskAsks.TASK_DETIAL_SUCCESS:
                TaskPrase.praseTaskDetial((NetObject) msg.obj,theActivity);
                break;
            case UPDATA_NAME:
                theActivity.mTaskCreatPresenter.updataName((Intent) msg.obj);
                break;
            case DELETE_PROJECT:
                intent = (Intent) msg.obj;
                String id1 = intent.getStringExtra("projectid");
                if(theActivity.mProjectItemModel.projectId.equals(id1))
                {
                    theActivity.mTask.projectId = "";
                    theActivity.mProjectItemModel.projectId = "";
                    theActivity.stageArea.setVisibility(View.GONE);
                }
                break;

        }

    }
}
