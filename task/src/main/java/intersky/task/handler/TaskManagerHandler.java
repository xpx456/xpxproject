package intersky.task.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.ProjectFileAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.entity.Project;
import intersky.task.prase.ProjectFilePrase;
import intersky.task.prase.ProjectPrase;
import intersky.task.prase.TaskPrase;
import intersky.task.view.activity.TaskManagerActivity;
import intersky.task.view.fragment.ProjectFragment;
import intersky.task.view.fragment.TaskFragment;
import intersky.xpxnet.net.NetObject;
//08
public class TaskManagerHandler extends Handler {

    public static final int UPDATA_PROJECT_LIST_VIEW = 3250700;
    public static final int UPDATA_PROJECT_LIST_DATA = 3250701;
    public static final int UPDATA_TASK_LIST_DATA = 3250702;
    public static final int EVENT_PROJECT_UNEXPEND = 3250703;
    public static final int EVENT_PROJECT_EXPEND = 3250704;
    public TaskManagerActivity theActivity;

    public TaskManagerHandler(TaskManagerActivity mTaskManagerActivity) {
        theActivity  = mTaskManagerActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case UPDATA_PROJECT_LIST_VIEW:
                theActivity.mProjectAdapter.notifyDataSetChanged();
                theActivity.mProjectSearchAdapter.notifyDataSetChanged();
                break;
            case ProjectAsks.PROJECT_LIST_SUCCESS:
                 ArrayList<Project> heads = ProjectPrase.praseProjects((NetObject) msg.obj,theActivity);
                 if(TaskManager.getInstance().projectOldPage > TaskManager.getInstance().projectPage && TaskManager.getInstance().projectkAll == false)
                 {
                     ProjectFragment mProjectFragment = (ProjectFragment) theActivity.fragments.get(TaskManagerActivity.PAGE_PROJECT);
                     if (mProjectFragment.mPullToRefreshView != null) {
                         ProjectAsks.getProject(theActivity,theActivity.mTaskManagerPresenter.mTaskManagerHandler,mProjectFragment.mSearchViewLayout.getText(),TaskManager.getInstance().projectPage);
                     }
                     else
                     {
                         ProjectAsks.getProject(theActivity,theActivity.mTaskManagerPresenter.mTaskManagerHandler,"",TaskManager.getInstance().projectPage);
                     }
                 }
                 else
                 {
                     TaskManager.getInstance().projectOldPage = 1;
                     if(heads.size() > 0 )
                         theActivity.mTaskManagerPresenter.getFileNames(heads);
                     theActivity.mProjectAdapter.notifyDataSetChanged();
                     theActivity.mProjectSearchAdapter.notifyDataSetChanged();
                     theActivity.waitDialog.hide();
                 }
                break;
            case ProjectFileAsks.PROJECT_FILE_LIST_SUCCESS:
                theActivity.waitDialog.hide();
                ProjectFilePrase.parseFilsName((NetObject) msg.obj);
                theActivity.mProjectAdapter.notifyDataSetChanged();
                break;
            case TaskAsks.TASK_LIST_SUCCESS:
                TaskPrase.praseTask((NetObject) msg.obj,theActivity);
                TaskFragment mTaskFragment = (TaskFragment) theActivity.fragments.get(TaskManagerActivity.PAGE_TASK);
                if(mTaskFragment != null)
                {
                    if(TaskManager.getInstance().taskOldPage > TaskManager.getInstance().taskPage && TaskManager.getInstance().taskAll == false)
                    {
                        TaskAsks.getTask(theActivity,theActivity.mTaskManagerPresenter.mTaskManagerHandler,"",theActivity.tags,theActivity.mTaskType.mClassId
                                ,theActivity.mTaskFilter.mClassId,theActivity.mTaskOrder.mOrderType,theActivity.mTaskOrder.mClassId
                                ,"",TaskManager.getInstance().taskPage);
                    }
                    else
                    {
                        if (theActivity.mTaskAdapter != null) {
                            theActivity.mTaskAdapter.notifyDataSetChanged();
                        }
                        theActivity.waitDialog.hide();
                    }
                }
                else
                {
                    if(TaskManager.getInstance().taskOldPage > TaskManager.getInstance().taskPage && TaskManager.getInstance().taskAll == false)
                    {
                        TaskAsks.getTask(theActivity,theActivity.mTaskManagerPresenter.mTaskManagerHandler,"",theActivity.tags,theActivity.mTaskType.mClassId
                                ,theActivity.mTaskFilter.mClassId,theActivity.mTaskOrder.mOrderType,theActivity.mTaskOrder.mClassId
                                ,"",TaskManager.getInstance().taskPage);
                    }
                    else
                    {
                        if (theActivity.mTaskAdapter != null) {
                            theActivity.mTaskAdapter.notifyDataSetChanged();
                        }
                        theActivity.waitDialog.hide();
                    }
                }
                break;
            case ProjectFileAsks.PROJECT_FILE_RENAME_SUCCESS:
                theActivity.waitDialog.hide();
                ProjectFilePrase.praseRename((NetObject) msg.obj);
                theActivity.mProjectAdapter.notifyDataSetChanged();
                break;
            case ProjectFileAsks.PROJECT_FILE_DELTETE_SUCCESS:
                theActivity.waitDialog.hide();
                Project file = ProjectFilePrase.praseDelete((NetObject) msg.obj);
                if(file != null)
                {
                    theActivity.mTaskManagerPresenter.initDelete(file);
                }
                break;
            case ProjectFileAsks.PROJECT_FILE_CREAT_SUCCESS:
                theActivity.waitDialog.hide();
                ProjectFilePrase.initCreat((NetObject) msg.obj);
                theActivity.mProjectAdapter.notifyDataSetChanged();
                break;
            case ProjectFileAsks.PROJECT_MOVE_SUCCESS:
            case ProjectFileAsks.PROJECT_MOVE_OUT_SUCCESS:
                theActivity.waitDialog.hide();
                Project item = ProjectFilePrase.praseMove((NetObject) msg.obj);
                if(item != null)
                theActivity.mTaskManagerPresenter.initMove(item);
                break;
            case Project.EVENT_PROJECT_MORE:
                theActivity.mTaskManagerPresenter.doMore((Project) msg.obj);
                break;
            case UPDATA_PROJECT_LIST_DATA:
                theActivity.mTaskManagerPresenter.updataProjectAll();
                break;
            case UPDATA_TASK_LIST_DATA:
                theActivity.mTaskManagerPresenter.updataTaskAll();
                break;
            case EVENT_PROJECT_EXPEND:
                theActivity.mTaskManagerPresenter.odExpend((Project) msg.obj);
                break;
            case EVENT_PROJECT_UNEXPEND:
                theActivity.mTaskManagerPresenter.unExpend((Project) msg.obj);
                break;

        }

    }
}
