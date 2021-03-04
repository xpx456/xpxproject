package intersky.task.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import intersky.apputils.AppUtils;
import intersky.task.R;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.ProjectStageAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.entity.Stage;
import intersky.task.entity.Task;
import intersky.task.prase.ProjectPrase;
import intersky.task.prase.ProjectStagePrase;
import intersky.task.prase.TaskPrase;
import intersky.task.view.activity.ProjectStageViewActivity;
import intersky.task.view.adapter.StageViewAdapter;
import intersky.xpxnet.net.NetObject;

//04
public class ProjectStageViewHandler extends Handler {

    public static final int  UPDATA_POSITION = 3250403;
    public static final int START_DRAGE = 3250400;
    public static final int START_DETIAL = 3250401;
    public static final int UPDATA_PROJECT = 3250404;
    public static final int UPDATA_DELETE = 32504035;
    public static final int UPDATA_EXIST = 3250406;
    public static final int UPDATA_ALL = 3250402;
    public static final int CHANGE_STAGE = 3250407;
    public ProjectStageViewActivity theActivity;

    public ProjectStageViewHandler(ProjectStageViewActivity mProjectStageViewActivity) {
        theActivity = mProjectStageViewActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case TaskAsks.TASK_LIST_SUCCESS:
                theActivity.waitDialog.hide();
                TaskPrase.praseTask((NetObject) msg.obj,theActivity,theActivity.mProject,theActivity.expentTask);
                theActivity.mStageViewAdapter.notifyDataSetChanged();
                break;
            case ProjectAsks.PROJECT_STAGE_SUCCESS:
                theActivity.waitDialog.hide();
                if(ProjectPrase.praseStage((NetObject) msg.obj,theActivity))
                {
                    Stage Stage1 = new Stage();
                    Stage1.stageId = "add";
                    Stage1.type = 1;
                    Stage1.name = theActivity.getString(R.string.project_task_view_add);
                    theActivity.mProject.mStages.add(Stage1);
                    theActivity.mStageViewAdapter.mStages.clear();
                    theActivity.mStageViewAdapter.mStages.addAll(theActivity.mProject.mStages);
                    theActivity.mProjectStageViewPresenter.getTask();
                }
                break;
            case START_DRAGE:
                theActivity.mProjectStageViewPresenter.creatDrageview((View) msg.obj);
                break;
            case START_DETIAL:
                theActivity.mProjectStageViewPresenter.startDetial((Task) msg.obj);
                break;
            case ProjectStageAsks.CREAT_STAGE_SUCCESS:
                theActivity.waitDialog.hide();
                if(ProjectStagePrase.praseStageSet((NetObject) msg.obj,theActivity))
                {
                    if(theActivity.mProject.projectId.length() > 0 && !theActivity.mProject.projectId.equals("0")) {
                        intent = new Intent(ProjectStageAsks.ACTION_PROJECT_STAGE_UPDATE);
                        intent.putExtra("projectid",theActivity.mProject.projectId);
                        theActivity.sendBroadcast(intent);
                    }
                }
                break;
            case TaskAsks.CHANGE_STAGE_SUCCESS:
                theActivity.waitDialog.hide();
                Task task = ProjectStagePrase.praseStageTaskSet((NetObject) msg.obj,theActivity);
                if(task != null)
                {
                    theActivity.mProjectStageViewPresenter.initTask(task);
                    task.stageId = theActivity.stage1.stageId;
                    if(task.taskId.length() > 0 && !task.taskId.equals("0")) {
                        if(theActivity.mProject.projectId.length() > 0 && !theActivity.mProject.projectId.equals("0")) {
                            intent = new Intent(TaskAsks.ACTION_TASK_CHANGE_STAGE);
                            intent.putExtra("taskid",task.taskId);
                            intent.putExtra("project",task.projectId);
                            intent.putExtra("stageid",task.stageId);
                            theActivity.sendBroadcast(intent);
                        }
                    }
                }
                break;
            case ProjectStageAsks.DELETE_STAGE_SUCCESS:
                theActivity.waitDialog.hide();
                Stage stage = ProjectStagePrase.praseStage((NetObject) msg.obj,theActivity);
                if(stage != null)
                {
                    if(theActivity.mProject.projectId.length() > 0 && !theActivity.mProject.projectId.equals("0")) {
                        intent = new Intent(ProjectStageAsks.ACTION_PROJECT_STAGE_UPDATE);
                        intent.putExtra("projectid",theActivity.mProject.projectId);
                        theActivity.sendBroadcast(intent);
                    }
                    theActivity.mProjectStageViewPresenter.deleteStage(stage);
                }
                break;
            case ProjectStageAsks.RENAME_STAGE_SUCCESS:
                NetObject object4 = (NetObject) msg.obj;
                theActivity.waitDialog.hide();
                ProjectStagePrase.praseStageName((NetObject) msg.obj,theActivity);
                theActivity.mStageViewAdapter.notifyDataSetChanged();
                theActivity.mProjectStageViewPresenter.praseStage((Stage) object4.item,object4.result);
                if(theActivity.mProject.projectId.length() > 0 && !theActivity.mProject.projectId.equals("0")) {
                    intent = new Intent(ProjectStageAsks.ACTION_PROJECT_STAGE_UPDATE);
                    intent.putExtra("projectid",theActivity.mProject.projectId);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case StageViewAdapter.EVENT_DO_CHANGE:
                theActivity.mProjectStageViewPresenter.onChange();
                break;
            case ProjectStageAsks.CHANGE_STAGE_SUCCESS:
                theActivity.waitDialog.hide();
                if(ProjectStagePrase.praseStageChange((NetObject) msg.obj,theActivity))
                {
                    theActivity.mProjectStageViewPresenter.changeSuccess();
                }
                else
                {
                    theActivity.mProjectStageViewPresenter.changeSuccess();
                }
                break;
            case TaskAsks.TASK_ADD_SUCCESS:
                theActivity.waitDialog.hide();
                Task task1 = TaskPrase.praseAdd((NetObject) msg.obj,theActivity);
                if(task1 != null)
                {
                    if(task1.taskId.length() > 0 && !task1.taskId.equals("0")) {
                        intent = new Intent(TaskAsks.ACTION_TASK_ADD);
                        intent.putExtra("taskid",task1.taskId);
                        theActivity.sendBroadcast(intent);
                    }
                    AppUtils.showMessage(theActivity,theActivity.getString(R.string.creat_sccess));
                }
                break;
            case UPDATA_POSITION:
                theActivity.mProjectStageViewPresenter.updataPositon(msg.arg1,msg.arg2);
                break;
            case UPDATA_PROJECT:
                theActivity.mProjectStageViewPresenter.updataProject((Intent) msg.obj);
                break;
            case ProjectAsks.PROJECT_ITEM_DETIAL_SUCCESS:
                ProjectPrase.praseProjectItemDetial((NetObject) msg.obj,theActivity);
                theActivity.waitDialog.show();
                ProjectAsks.getStage(theActivity, theActivity.mProjectStageViewPresenter.mProjectStageViewHandler,theActivity.mProject);
                break;
            case UPDATA_EXIST:
                intent = (Intent) msg.obj;
                String id = intent.getStringExtra("projectid");
                if(theActivity.mProject.projectId.equals("id"))
                theActivity.finish();
                break;
            case UPDATA_DELETE:
                intent = (Intent) msg.obj;
                String id1 = intent.getStringExtra("projectid");
                if(theActivity.mProject.projectId.equals(id1))
                    theActivity.finish();
                break;
            case UPDATA_ALL:
                intent = (Intent) msg.obj;
                String id2 = intent.getStringExtra("projectid");
                if(theActivity.mProject.projectId.equals(id2))
                    theActivity.mProjectStageViewPresenter.updataAll();
                break;
            case CHANGE_STAGE:
                intent = (Intent) msg.obj;
                String projectid = intent.getStringExtra("projectid");
                String taskid = intent.getStringExtra("taskid");
                String stageid = intent.getStringExtra("stageid");
                Stage stage1 = theActivity.mProject.mStageHashs.get(stageid);
                if(theActivity.mProject.projectId.equals(projectid))
                {
                    if(!stage1.mTaskHashs.containsKey(taskid))
                    {
                        theActivity.mProjectStageViewPresenter.updataAll();
                    }
                }
                break;

        }

    }
}
