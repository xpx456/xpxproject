package intersky.task.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.mywidget.PullToRefreshView;
import intersky.task.entity.CourseClass;
import intersky.task.entity.Project;
import intersky.task.entity.Task;
import intersky.task.presenter.ProjectStageDetialPresenter;
import intersky.task.view.adapter.ClassAdapter;
import intersky.task.view.adapter.TagClassAdapter;

/**
 * Created by xpx on 2017/8/18.
 */

public class ProjectStageDetialActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener,
        PullToRefreshView.OnFooterRefreshListener {

    public ProjectStageDetialPresenter mProjectStageDetialPresenter = new ProjectStageDetialPresenter(this);
    public RelativeLayout templateView;
    public RelativeLayout sysView;
    public Project mProject;
    public LinearLayout stageArea;
    public RelativeLayout btnStatuhensive;
    public TextView txtStatuhens;
    public ImageView imgStatuhens;
    public RelativeLayout btnPersonhensive;
    public TextView txtPersonhens;
    public ImageView imgPersonhens;
    public RelativeLayout btnOrderhensive;
    public TextView txtOrderhens;
    public ImageView imgOrderhens;
    public LinearLayout topLayer;
    public ListView mClassList;
    public RelativeLayout mShade;
    public ArrayList<CourseClass> mTaskTypeClass = new ArrayList<CourseClass>();
    public ArrayList<CourseClass> mTaskFilterClass = new ArrayList<CourseClass>();
    public ArrayList<CourseClass> mTaskOrderClass = new ArrayList<CourseClass>();
    public CourseClass mTaskType;
    public CourseClass mTaskFilter;
    public CourseClass mTaskOrder;
    public TagClassAdapter mStatuAdapter;
    public ClassAdapter mPersonAdapter;
    public ClassAdapter mOrderAdapter;
    public boolean showStatu = false;
    public boolean showPerson = false;
    public boolean showOrder = false;
    public String tags = "";
    public PullToRefreshView mPullToRefreshView;
    public String expends = "";
    public HashMap<String, Task> expentTask = new HashMap<String, Task>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProjectStageDetialPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mProjectStageDetialPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener startTemplateView = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mProjectStageDetialPresenter.startTemplateView();
        }
    };

    public View.OnClickListener startSysView = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mProjectStageDetialPresenter.startSysView();
        }
    };

    public View.OnClickListener startTaskDetial = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mProjectStageDetialPresenter.startTask((Task) v.getTag());
        }
    };

    public View.OnClickListener doExpendListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mProjectStageDetialPresenter.doexPend((Task) v.getTag());
        }
    };

    public View.OnClickListener mShowClassLinstener1 = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mProjectStageDetialPresenter.doClass1();
        }
    };

    public View.OnClickListener mShowClassLinstener2 = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mProjectStageDetialPresenter.doClass2();
        }
    };

    public View.OnClickListener mShowClassLinstener3 = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mProjectStageDetialPresenter.doClass3();
        }
    };

    public AdapterView.OnItemClickListener statuListenter = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mProjectStageDetialPresenter.doSearchStatu((CourseClass) parent.getAdapter().getItem(position));
        }
    };

    public AdapterView.OnItemClickListener filterSelectListenter = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mProjectStageDetialPresenter.doSearchFilter((CourseClass) parent.getAdapter().getItem(position));
        }
    };

    public AdapterView.OnItemClickListener classOrderListenter = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mProjectStageDetialPresenter.doOrder((CourseClass) parent.getAdapter().getItem(position));
        }
    };

    public View.OnClickListener shadeClick = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mProjectStageDetialPresenter.doHidall();
        }
    };

    public View.OnClickListener newTask = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mProjectStageDetialPresenter.newTask();
        }
    };

    public View.OnClickListener showDetial = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mProjectStageDetialPresenter.showDetial();
        }
    };

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mPullToRefreshView.onFooterRefreshComplete();
        mProjectStageDetialPresenter.getTask();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPullToRefreshView.onHeaderRefreshComplete();
        mProjectStageDetialPresenter.getTask();
    }
}
