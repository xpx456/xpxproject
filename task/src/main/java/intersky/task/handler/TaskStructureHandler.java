package intersky.task.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.select.SelectManager;
import intersky.select.SelectReceiver;
import intersky.select.entity.Select;
import intersky.task.R;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.ProjectFileAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.entity.Project;
import intersky.task.entity.Stage;
import intersky.task.entity.Task;
import intersky.task.prase.ProjectFilePrase;
import intersky.task.prase.ProjectPrase;
import intersky.task.prase.ProjectStagePrase;
import intersky.task.prase.TaskPrase;
import intersky.task.view.activity.TaskStructureActivity;
import intersky.xpxnet.net.NetObject;
//09
public class TaskStructureHandler extends Handler {


    public static final int SET_PROJECT = 3250900;
    public static final int SET_STAGE = 3250901;
    public static final int SET_PARENT = 3250902;
    public static final int UPDATA_TASK = 3250903;
    public static final int UPDATA_PROJECT = 3250904;
    public static final int EXIST_TASK = 3250905;
    public static final int TASK_DELETE = 3250906;
    public TaskStructureActivity theActivity;

    public TaskStructureHandler(TaskStructureActivity mTaskStructureActivity) {
        theActivity = mTaskStructureActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        Intent intent = new Intent();
        switch (msg.what) {
            case TaskAsks.GET_TASK_SETPARENT_SUCCESS:
                if(TaskPrase.prasePraentTask((NetObject) msg.obj,theActivity,theActivity.praentSetTasks) == false)
                {
                    theActivity.taskpage++;
                }
                else {
                    theActivity.taskall = true;
                }
                theActivity.waitDialog.hide();
                theActivity.sendBroadcast(new Intent(SelectReceiver.ACTION_UPDATA_SELECT_LIST));
                break;
            case ProjectFileAsks.PROJECT_FILE_RENAME_SUCCESS:
                theActivity.waitDialog.hide();
                ProjectFilePrase.praseRename((NetObject) msg.obj);
                break;
            case ProjectAsks.PROJECT_LIST_SUCCESS:
                theActivity.waitDialog.hide();
                ArrayList<Project> heads = ProjectPrase.praseProjects((NetObject) msg.obj,theActivity);
                if(heads.size() > 0 )
                {
                    ProjectFileAsks.getFileNames(theActivity,theActivity.mTaskStructurePresenter.mTaskStructureHandler,heads);
                    theActivity.waitDialog.show();
                }
                theActivity.sendBroadcast(new Intent(ProjectAsks.ACTION_UPDATA_PROJECT_LIST_DATA));
                theActivity.sendBroadcast(new Intent(SelectReceiver.ACTION_UPDATA_SELECT_LIST));
                break;
            case ProjectAsks.PROJECT_ITEM_DETIAL_SUCCESS:
                ProjectPrase.praseProjectItemDetial((NetObject) msg.obj,theActivity);
                if(theActivity.mTask.projectId.length() > 0)
                {
                    if(theActivity.mProject != null)
                    theActivity.projectName.setText(theActivity.mProject.mName);
                }
                theActivity.mTaskStructurePresenter.initStages();
                theActivity.waitDialog.hide();
                break;
            case ProjectAsks.PROJECT_DETIAL_SUCCESS:
                ArrayList<Contacts> contacts = new ArrayList<Contacts>();
                ProjectPrase.MemberDetial memberDetial = ProjectPrase.praseProjectDetial((NetObject) msg.obj,theActivity,contacts);
                if(memberDetial != null)
                {
                    if(memberDetial.leavlType <= 3)
                        theActivity.mTaskStructurePresenter.startProject();
                    else
                        theActivity.mTaskStructurePresenter.startAddProject();
                }
                theActivity.waitDialog.hide();
                break;
            case TaskAsks.TASK_DETIAL_SUCCESS:
                Task task = TaskPrase.praseTaskDetial((NetObject) msg.obj,theActivity);
                if(task.taskId.equals(theActivity.mTask.taskId))
                {
                    if(!task.projectId.equals(theActivity.mProject.projectId))
                    {
                        theActivity.mProject.projectId = task.projectId;
                        ProjectAsks.getProjectItemDetial(theActivity,theActivity.mTaskStructurePresenter.mTaskStructureHandler,theActivity.mProject);
                    }
                }
                else if(task.taskId.equals(theActivity.mTask.parentId))
                {
                    if(task.show)
                    {
                        task.show = false;
                        theActivity.mTaskStructurePresenter.startTaskDetial(task);
                    }
                    else
                    {
                        theActivity.parentName.setText(task.parentName);
                    }

                }
                theActivity.waitDialog.hide();
                break;
            case TaskAsks.GET_SON_SUCCESS:
                theActivity.waitDialog.hide();
                theActivity.sonList = TaskPrase.praseSon((NetObject) msg.obj,theActivity);
                theActivity.mTaskStructurePresenter.praseSonView();
                break;
            case TaskAsks.DELETE_TASK_SUCCESS:
                if(TaskPrase.praseDelete((NetObject) msg.obj,theActivity))
                {
                    TaskAsks.getSon(theActivity,theActivity.mTaskStructurePresenter.mTaskStructureHandler,theActivity.mTask);
                    theActivity.waitDialog.show();
                }
                break;
            case TaskAsks.TASK_ADD_SUCCESS:
                theActivity.waitDialog.hide();
                Task task1 = TaskPrase.praseAdd((NetObject) msg.obj,theActivity);
                if(task1 != null) {
                    if(task1.taskId.length() > 0 && !task1.taskId.equals("0")) {
                        intent = new Intent(TaskAsks.ACTION_TASK_ADD);
                        intent.putExtra("taskid",task1.taskId);
                        theActivity.sendBroadcast(intent);
                    }
                    AppUtils.showMessage(theActivity,theActivity.getString(R.string.creat_sccess));
                }
                break;
            case SET_PROJECT:
                if(SelectManager.getInstance().mCustomSignal != null)
                {
                    Project project = (Project) SelectManager.getInstance().mCustomSignal.object;
                    if(!theActivity.mTask.projectId.equals(project.projectId)) {
                        TaskAsks.setTaskProject(theActivity,theActivity.mTaskStructurePresenter.mTaskStructureHandler,theActivity.mTask,project);
                    }
                }
                break;
            case SET_STAGE:
                Select stage = SelectManager.getInstance().mSignal;
                if(!theActivity.mTask.stageId.equals(stage.mId)) {
                    Stage stage1 = new Stage();
                    stage1.stageId = stage.mId;
                    stage1.name = stage.mName;
                    TaskAsks.changeTaskStage(theActivity,theActivity.mTaskStructurePresenter.mTaskStructureHandler,theActivity.mTask,stage1);
                }
                break;
            case SET_PARENT:
                if(SelectManager.getInstance().mCustomSignal != null)
                {
                    Task parentTask = (Task) SelectManager.getInstance().mCustomSignal.object;
                    if(!theActivity.mTask.parentId.equals(parentTask.taskId)) {
                        TaskAsks.setTaskParent(theActivity,theActivity.mTaskStructurePresenter.mTaskStructureHandler,theActivity.mTask,parentTask);
                    }
                }
                break;
            case TaskAsks.SET_TASK_PROJECT_SUCCESS:
                Project project1 = TaskPrase.praseTaskProject((NetObject) msg.obj,theActivity,theActivity.mTask);
                if(project1 != null) {
                    //theActivity.mProject = project1;
//                    ProjectAsks.getProjectItemDetial(theActivity,theActivity.mTaskStructurePresenter.mTaskStructureHandler,theActivity.mProject);
                    intent = new Intent(TaskAsks.ACTION_TASK_CHANGE_PROJECT);
                    intent.putExtra("taskid",theActivity.mTask.taskId);
                    theActivity.sendBroadcast(intent);
//                    TaskAsks.getTaskDetial(theActivity,theActivity.mTaskStructurePresenter.mTaskStructureHandler,theActivity.mTask);
                }
                break;
            case TaskAsks.SET_TASK_PARENT_SUCCESS:
                Task parent = TaskPrase.praseTaskParent((NetObject) msg.obj,theActivity,theActivity.mTask);
                if(parent != null) {
                    theActivity.mPraentTask = parent;
                    TaskAsks.getTaskDetial(theActivity,theActivity.mTaskStructurePresenter.mTaskStructureHandler,theActivity.mTask);
                }
                break;
            case TaskAsks.CHANGE_STAGE_SUCCESS:
                theActivity.waitDialog.hide();
                Task task2 = ProjectStagePrase.praseStageTaskSet((NetObject) msg.obj,theActivity);
                if(task2 != null) {
                    theActivity.mTaskStructurePresenter.initStages();
                }
                break;
            case UPDATA_TASK:
                theActivity.mTaskStructurePresenter.updataTaskDetial((Intent) msg.obj);
                break;
            case UPDATA_PROJECT:
                theActivity.mTaskStructurePresenter.updataTaskProject((Intent) msg.obj);
                break;
            case EXIST_TASK:
                intent = (Intent) msg.obj;
                String id = intent.getStringExtra("taskid");
                String cid = intent.getStringExtra("cid");
                if(theActivity.mTask.taskId.equals(id) && !theActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)&&theActivity.mTask.leaderId.equals(cid))
                    theActivity.finish();
                else
                    theActivity.mTaskStructurePresenter.updataTaskDetial((Intent) msg.obj);
                break;
            case TASK_DELETE:
                intent = (Intent) msg.obj;
                String id1 = intent.getStringExtra("taskid");
                if(theActivity.mTask.taskId.equals(id1))
                    theActivity.finish();
                else
                    theActivity.mTaskStructurePresenter.updataTaskDetial((Intent) msg.obj);
                break;
        }

    }
}
