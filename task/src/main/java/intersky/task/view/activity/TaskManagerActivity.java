package intersky.task.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.MyRelativeLayout;
import intersky.mywidget.PullToRefreshView;
import intersky.task.asks.ProjectFileAsks;
import intersky.task.entity.CourseClass;
import intersky.task.entity.Project;
import intersky.task.presenter.TaskManagerPresenter;
import intersky.task.view.adapter.ClassAdapter;
import intersky.task.view.adapter.FileAdapter;
import intersky.task.view.adapter.ProjectAdapter;
import intersky.task.view.adapter.TagClassAdapter;
import intersky.task.view.adapter.TaskAdapter;
import intersky.task.view.fragment.ProjectFragment;
import intersky.task.view.fragment.TaskFragment;

/**
 * Created by xpx on 2017/8/18.
 */

public class TaskManagerActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{

    public static final int PAGE_TASK = 0;
    public static final int PAGE_PROJECT = 1;
    public static final int EVENT_GET_PROJECT_LIST_SUCCESS = 1002;
    public static final int EVENT_GET_PROJECT_LIST_FAIL = 1003;
    public static final int EVENT_GET_TASK_LIST_SUCCESS = 1004;
    public static final int EVENT_GET_TASK_LIST_FAIL = 1005;
    public static final int EVENT_GET_TASK_LIST_SEARCH_SUCCESS = 1006;
    public static final int EVENT_GET_TASK_LIST_SEARCH_FAIL = 1007;
    public static final int EVENT_UPDATA_TASK_LIST_ALL = 1008;
    public static final int EVENT_UPDATA_PROJECT_VIWE = 1009;
    public static final int EVENT_UPDATA_TASK_ITEM = 1010;
    public static final int EVENT_UPDATA_PROJECT_BASE = 1014;
    public static final int EVENT_ADD_PROJECT_BASE = 1015;
    public static final int EVENT_UPDATA_TASK_SUCCESS = 1016;
    public static final int EVENT_UPDATA_TASK_FAIL = 1017;
    public static final int EVENT_UPDATA_PROJECT_ONE = 1018;
    public static final int EVENT_UPDATA_PROJECT_SUCCESS = 1019;
    public static final int EVENT_UPDATA_PROJECT_FAIL = 1020;
    public static final int EVENT_UPDATA_PROJECT_LEADERE = 1021;
    public static final int EVENT_PROJECT_EXPEND= 1022;
    public static final int EVENT_PROJECT_UNEXPEND= 1023;
    public static final int EVENT_PROJECT_MORE= 1024;
    public static final int EVENT_PROJECT_RENAME_SUCCESS = 1025;
    public static final int EVENT_PROJECT_RENAME_FAIL = 1026;
    public static final int EVENT_PROJECT_DELTETE_SUCCESS = 1027;
    public static final int EVENT_PROJECT_DELTETE_FAIL = 1028;
    public static final int EVENT_PROJECT_NAME_SUCCESS = 1029;
    public static final int EVENT_PROJECT_NAME_FAIL = 1030;
    public static final int EVENT_PROJECT_CREAT_SUCCESS = 1031;
    public static final int EVENT_PROJECT_MOVE_SUCCESS = 1032;
    public TaskManagerPresenter mTaskManagerPresenter = new TaskManagerPresenter(this);
    public List<Fragment> fragments = new ArrayList<Fragment>();
    public TaskFragment taskFragment;
    public ProjectFragment projectFragment;
    public ArrayList<CourseClass> mTaskTypeClass = new ArrayList<CourseClass>();
    public ArrayList<CourseClass> mTaskFilterClass = new ArrayList<CourseClass>();
    public ArrayList<CourseClass> mTaskOrderClass = new ArrayList<CourseClass>();
    public ArrayList<CourseClass> mTaskTagClass = new ArrayList<CourseClass>();
    public RelativeLayout shade;
    public CourseClass mTaskType;
    public CourseClass mTaskFilter;
    public CourseClass mTaskOrder;
//    public CourseClass mTaskTag;
    public RelativeLayout tabTask;
    public TextView task;
    public RelativeLayout tabProject;
    public TextView project;
    public ProjectAdapter mProjectAdapter;
    public ProjectAdapter mProjectSearchAdapter;
    public TaskAdapter mTaskAdapter;
    public FileAdapter mFileAdapter;
    public int nowPage;
    public String tags = "";
    public PopupWindow popupWindow;
    public boolean domove = false;
    public View buttomView;
    public MyLinearLayout type;
    public MyLinearLayout filter;
    public MyLinearLayout order;
    public MyLinearLayout color;
    public TextView btnOk;
    public TextView btnReset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskManagerPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mTaskManagerPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener creatListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskManagerPresenter.doCreat();
        }
    };

    public View.OnClickListener tebTaskListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskManagerPresenter.setContent(TaskManagerActivity.PAGE_TASK);
        }
    };

    public View.OnClickListener tebProjectListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskManagerPresenter.setContent(TaskManagerActivity.PAGE_PROJECT);
        }
    };


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mTaskManagerPresenter.onFoot();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mTaskManagerPresenter.onHead();
    }

    public View.OnClickListener renameListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            if(popupWindow != null)
            {
                popupWindow.dismiss();
            }
            mTaskManagerPresenter.doReName((Project) v.getTag());
        }
    };

    public View.OnClickListener deleteListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            if(popupWindow != null)
            {
                popupWindow.dismiss();
            }
            ProjectFileAsks.delete(mTaskManagerPresenter.mTaskManagerActivity,mTaskManagerPresenter.mTaskManagerHandler, (Project) v.getTag());
        }
    };

    public View.OnClickListener creatFileListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            if(popupWindow != null)
            {
                popupWindow.dismiss();
            }
            mTaskManagerPresenter.doCreat((Project) v.getTag());
        }
    };

    public View.OnClickListener moveListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            if(popupWindow != null)
            {
                popupWindow.dismiss();
            }
            mTaskManagerPresenter.doMoveSelect((Project) v.getTag());
        }
    };

    public View.OnClickListener moveOutListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            if(popupWindow != null)
            {
                popupWindow.dismiss();
            }
            ProjectFileAsks.moveout(mTaskManagerPresenter.mTaskManagerActivity,mTaskManagerPresenter.mTaskManagerHandler,(Project) v.getTag());
        }
    };
}
