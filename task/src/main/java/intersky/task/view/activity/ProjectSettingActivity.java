package intersky.task.view.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.mywidget.switchbutton.SwitchButton;
import intersky.select.entity.Select;
import intersky.task.asks.ProjectAsks;
import intersky.task.entity.Project;
import intersky.task.presenter.ProjectSettingPresenter;

/**
 * Created by xpx on 2017/8/18.
 */

public class ProjectSettingActivity extends BaseActivity {

    public static final String ACTION_PROJECT_POWER = "ACTION_PROJECT_POWER";
    public static final String ACTION_PROJECT_CONTRAL = "ACTION_PROJECT_CONTRAL";
    public ArrayList<Select> powers = new ArrayList<Select>();
    public ArrayList<Select> contrals = new ArrayList<Select>();
    public ProjectSettingPresenter mProjectSettingePresenter = new ProjectSettingPresenter(this);
    public RelativeLayout top;
    public RelativeLayout saveTemple;
    public RelativeLayout power;
    public RelativeLayout contral;
    public TextView powerValue;
    public TextView contralValue;
    public TextView butomBtn;
//    public ImageView topImg;
    public Switch mSwitch;
    public Project project;
    public AlertDialog dialog;
    public int leavetype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProjectSettingePresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mProjectSettingePresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener setSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            waitDialog.show();
            ProjectAsks.saveTempal(mProjectSettingePresenter.mProjectSettingActivity,mProjectSettingePresenter.mProjectSettingHandler,project);
        }
    };

    public View.OnClickListener setPowerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mProjectSettingePresenter.setPower();
        }
    };

    public View.OnClickListener setContralListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mProjectSettingePresenter.setContral();
        }
    };

    public View.OnClickListener existListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mProjectSettingePresenter.doExist();
        }
    };

    public View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ProjectAsks.doApply(mProjectSettingePresenter.mProjectSettingActivity,mProjectSettingePresenter.mProjectSettingHandler,project);
        }
    };

    public View.OnClickListener deleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mProjectSettingePresenter.doDelete();
        }
    };


    public CompoundButton.OnCheckedChangeListener mChecklistener = new  CompoundButton.OnCheckedChangeListener()
    {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(isChecked == true)
            {
                if(project.isTop == 0)
                {
                    waitDialog.show();
                    ProjectAsks.setTop(mProjectSettingePresenter.mProjectSettingActivity,mProjectSettingePresenter.mProjectSettingHandler,project,1);
                }
            }
            else
            {
                if(project.isTop == 1)
                {
                    waitDialog.show();
                    ProjectAsks.setTop(mProjectSettingePresenter.mProjectSettingActivity,mProjectSettingePresenter.mProjectSettingHandler,project,0);
                }
            }

        }
    };
}
