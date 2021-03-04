package intersky.task.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.mywidget.MyLinearLayout;
import intersky.select.SelectManager;
import intersky.select.entity.Select;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.entity.Project;
import intersky.task.entity.Task;
import intersky.task.presenter.TaskCreatPresenter;

/**
 * Created by xpx on 2017/8/18.
 */

public class TaskCreatActivity extends BaseActivity {

    public static final String ACTION_CREAT_SET_LEADER = "ACTION_CREAT_SET_LEADER";
    public static final String ACTION_CREAT_SET_PROJECT = "ACTION_CREAT_SET_PROJECT";
    public static final String ACTION_CREAT_SET_STAGE = "ACTION_CREAT_SET_STAGE";
    public static final String ACTION_CREAT_SET_PARENT= "ACTION_CREAT_SET_PARENT";
    public static final String ACTION_ADD_PICTORE = "ACTION_ADD_PICTORE";
    public static final String ACTION_ADD_COPYER = "ACTION_ADD_COPYER";

    public TaskCreatPresenter mTaskCreatPresenter = new TaskCreatPresenter(this);
    public Contacts mLeader;
    public ArrayList<Contacts> mCopyers = new ArrayList<Contacts>();
    public ArrayList<Attachment> mPics = new ArrayList<Attachment>();
    public ArrayList<Select> mStages = new ArrayList<Select>();
    public Select mselectmStage;
    public Project mProjectItemModel;
    public Task parentTask;
    public Task mTask;
    public RelativeLayout mTaskDuty;
    public MyLinearLayout copyer;
    public MyLinearLayout mImageLayer;
    public RelativeLayout mDateStart;
    public RelativeLayout mDateEnd;
    public RelativeLayout mDuty;
    public TextView publish;
    public RelativeLayout projectArea;
    public RelativeLayout stageArea;
    public TextView mLeaderText;
    public TextView dateTextStart;
    public TextView dateTextEnd;
    public EditText taskName;
    public TextView proJect;
    public TextView stage;
    public TextView durtyPerson;
    public EditText desCtibe;
    public String mfilepath;
    public String lastNetHash = "";
    public String attjson = "";
    public boolean issub = false;
    public PopupWindow popupWindow1;
    public RelativeLayout mRelativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskCreatPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mTaskCreatPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener copyerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskCreatPresenter.selectCopyer();
        }
    };

    public View.OnClickListener mDeleteCopyerListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mTaskCreatPresenter.deleteCopyer( v);
        }
    };

    public View.OnClickListener mDeletePicListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mTaskCreatPresenter.delete( v);
        }
    };

    public View.OnClickListener detepickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskCreatPresenter.onDataPick1();
        }
    };

    public View.OnClickListener detepickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskCreatPresenter.onDataPick2();
        }
    };

    public View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskCreatPresenter.submit();
        }
    };

    public View.OnClickListener setDutyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskCreatPresenter.startDuaySelect();
        }
    };

    public View.OnClickListener mAddPicListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mTaskCreatPresenter.addPic();
        }
    };

    public View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mTaskCreatPresenter.takePhoto();
        }

    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTaskCreatPresenter.takePhotoResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public View.OnClickListener setProject = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskCreatPresenter.selectProject();
        }
    };
    public View.OnClickListener setStage = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskCreatPresenter.selectStage();
        }
    };

    public SelectManager.SelectDetial selectProjectDetial = new SelectManager.SelectDetial() {
        @Override
        public void onfoot() {
            if(TaskManager.getInstance().projectkAll = false)
                ProjectAsks.getProject(mTaskCreatPresenter.mTaskCreatActivity,mTaskCreatPresenter.mTaskCreatHandler,"", TaskManager.getInstance().projectPage);
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
            ProjectAsks.getProject(mTaskCreatPresenter.mTaskCreatActivity,mTaskCreatPresenter.mTaskCreatHandler,"", TaskManager.getInstance().projectPage);
        }
    };


    public View.OnClickListener mShowAddListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mTaskCreatPresenter.showAdd( );
        }
    };

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return mTaskCreatPresenter.onFling(motionEvent, motionEvent1, v, v1);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果触及到返回键所要执行的操作是什么
            mTaskCreatPresenter.chekcBack();
            return true;
        }
        return super.onKeyDown  (keyCode ,event);

    }
}
