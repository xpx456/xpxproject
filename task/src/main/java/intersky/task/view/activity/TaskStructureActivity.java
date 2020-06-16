package intersky.task.view.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.BaseActivity;
import intersky.appbase.PadBaseActivity;
import intersky.select.SelectManager;
import intersky.select.entity.CustomSelect;
import intersky.select.entity.Select;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.entity.Project;
import intersky.task.entity.Task;
import intersky.task.presenter.TaskStructurePresenter;
import intersky.task.view.adapter.TaskSelectAdapter;

/**
 * Created by xpx on 2017/8/18.
 */

public class TaskStructureActivity extends BaseActivity {

    public static final String ACTION_SET_PROJECT = "ACTION_SET_PROJECT";
    public static final String ACTION_SET_STAGE = "ACTION_SET_STAGE";
    public static final String ACTION_SET_PARENT= "ACTION_SET_PARENT";

    public ArrayList<Task> mSonTasks = new  ArrayList<Task>();
    public ArrayList<Task> mSonTasksFinish = new  ArrayList<Task>();
    public TaskStructurePresenter mTaskStructurePresenter = new TaskStructurePresenter(this);
    public ArrayList<Select> mStages = new ArrayList<Select>();
    public ArrayList<Select> mProjects = new ArrayList<Select>();
    public Select mselectmStage;
    public RelativeLayout newTaskArea;
    public RelativeLayout stage;
    public RelativeLayout parentLine;
    public RelativeLayout taskline;
    public LinearLayout parentlayer;
    public RelativeLayout lineparent;
    public TextView projectName;
    public TextView projectNameChange;
    public TextView stageName;
    public TextView stageNameChange;
    public TextView parentName;
    public TextView parentNameChange;
    public TextView head;
    public TextView nowTask;
    public ImageView image1;
    public ImageView image2;
    public ImageView image3;
    public EditText addEdit;
    public LinearLayout mSonArea;
    public TextView mSonTitle;
    public Task mTask;
    public Project mProject;
    public Task mPraentTask;
    public AlertDialog dialog;
    public boolean isEdit = false;
    public boolean isfirst = true;
    public int taskpage = 1;
    public boolean taskall = false;
    public ArrayList<CustomSelect> praentSetTasks = new ArrayList<CustomSelect>();
    public TaskSelectAdapter taskSelectAdapter;
    public HashMap<String,Task> sonList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskStructurePresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mTaskStructurePresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener openProject = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskStructurePresenter.openProject();
        }
    };

    public View.OnClickListener openParentTask = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskStructurePresenter.openParent();
        }
    };

    public View.OnClickListener setProject = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskStructurePresenter.selectProject();
        }
    };

    public View.OnClickListener setStage = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskStructurePresenter.selectStage();
        }
    };

    public View.OnClickListener setParent = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskStructurePresenter.selectParent();
        }
    };

    public View.OnClickListener doEditListtener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskStructurePresenter.doEdit();
        }
    };

    public TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED && event.getAction() == KeyEvent.ACTION_UP)
            {
                mTaskStructurePresenter.doCreat(v.getText().toString());

            }
            return true;
        }
    };

    public View.OnClickListener doDeleteListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskStructurePresenter.doDelete((Task) v.getTag());
        }
    };

    public View.OnClickListener doDetialListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskStructurePresenter.startTaskDetial((Task) v.getTag());
        }
    };

    public SelectManager.SelectDetial selectProjectDetial = new SelectManager.SelectDetial() {
        @Override
        public void onfoot() {
            if(TaskManager.getInstance().projectkAll = false)
            ProjectAsks.getProject(mTaskStructurePresenter.mTaskStructureActivity,mTaskStructurePresenter.mTaskStructureHandler,"", TaskManager.getInstance().projectPage);
        }

        @Override
        public void onHead() {
            TaskManager.getInstance().projectPage = 1;
            TaskManager.getInstance().mProjects.clear();
            TaskManager.getInstance().mProjects2.clear();
            TaskManager.getInstance().projectSelects.clear();
            TaskManager.getInstance().mHeads.clear();
            TaskManager.getInstance().projectkAll = false;
            TaskManager.getInstance().mSearchProjects.clear();
            ProjectAsks.getProject(mTaskStructurePresenter.mTaskStructureActivity,mTaskStructurePresenter.mTaskStructureHandler,"", TaskManager.getInstance().projectPage);
        }
    };


    public SelectManager.SelectDetial selectPranetDetial = new SelectManager.SelectDetial() {
        @Override
        public void onfoot() {
            if(taskall == false)
            TaskAsks.getSetParentTask(mTaskStructurePresenter.mTaskStructureActivity,mTaskStructurePresenter.mTaskStructureHandler,mTask,taskpage);
        }

        @Override
        public void onHead() {
            taskpage = 1;
            taskall = false;
            praentSetTasks.clear();
            TaskAsks.getSetParentTask(mTaskStructurePresenter.mTaskStructureActivity,mTaskStructurePresenter.mTaskStructureHandler,mTask,taskpage);
        }
    };
}
