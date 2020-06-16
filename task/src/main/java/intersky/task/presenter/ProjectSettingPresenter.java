package intersky.task.presenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.mywidget.switchbutton.SwitchButton;
import intersky.select.SelectManager;
import intersky.select.entity.Select;
import intersky.task.R;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.handler.ProjectSettingHandler;
import intersky.task.receiver.ProjectSettinglReceiver;
import intersky.task.view.activity.ProjectSettingActivity;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class ProjectSettingPresenter implements Presenter {

    public ProjectSettingHandler mProjectSettingHandler;
    public ProjectSettingActivity mProjectSettingActivity;

    public ProjectSettingPresenter(ProjectSettingActivity mProjectSettingActivity)
    {
        this.mProjectSettingActivity = mProjectSettingActivity;
        this.mProjectSettingHandler = new ProjectSettingHandler(mProjectSettingActivity);
        mProjectSettingActivity.setBaseReceiver(new ProjectSettinglReceiver(mProjectSettingHandler));
    }

    @Override
    public void Create() {

        initView();
    }

    @Override
    public void initView() {
        mProjectSettingActivity.setContentView(R.layout.activity_project_setting);
        mProjectSettingActivity.top = (RelativeLayout) mProjectSettingActivity.findViewById(R.id.top);
        mProjectSettingActivity.saveTemple = (RelativeLayout) mProjectSettingActivity.findViewById(R.id.save_tempal);
        mProjectSettingActivity.power = (RelativeLayout) mProjectSettingActivity.findViewById(R.id.power);
        mProjectSettingActivity.contral = (RelativeLayout) mProjectSettingActivity.findViewById(R.id.contral);
        mProjectSettingActivity.powerValue = (TextView) mProjectSettingActivity.findViewById(R.id.power_value);
        mProjectSettingActivity.contralValue = (TextView) mProjectSettingActivity.findViewById(R.id.contral_value);
        mProjectSettingActivity.butomBtn = (TextView) mProjectSettingActivity.findViewById(R.id.buttom_btn);
        mProjectSettingActivity.mSwitch = (Switch) mProjectSettingActivity.findViewById(R.id.swich);
        mProjectSettingActivity.mSwitch.setOnCheckedChangeListener(mProjectSettingActivity.mChecklistener);
        mProjectSettingActivity.project = mProjectSettingActivity.getIntent().getParcelableExtra("project");
        mProjectSettingActivity.leavetype = mProjectSettingActivity.getIntent().getIntExtra("leavetype",3);
        if(mProjectSettingActivity.leavetype == 1)
        {
            mProjectSettingActivity.butomBtn.setText(R.string.project_setting_delete);
            mProjectSettingActivity.butomBtn.setOnClickListener(mProjectSettingActivity.deleteListener);
        }
        else if(mProjectSettingActivity.leavetype == 2 || mProjectSettingActivity.leavetype ==3)
        {
            mProjectSettingActivity.butomBtn.setText(R.string.project_setting_exit);
            mProjectSettingActivity.butomBtn.setOnClickListener(mProjectSettingActivity.existListener);
        }
        else
        {
            mProjectSettingActivity.butomBtn.setText(R.string.project_setting_add);
            mProjectSettingActivity.butomBtn.setOnClickListener(mProjectSettingActivity.addListener);
        }

        if(mProjectSettingActivity.project.isTop == 1)
        {
            mProjectSettingActivity.mSwitch.setChecked(true);
        }
        else
        {
            mProjectSettingActivity.mSwitch.setChecked(false);
        }
        initSelects();
        mProjectSettingActivity.saveTemple.setOnClickListener(mProjectSettingActivity.setSaveListener);
        mProjectSettingActivity.power.setOnClickListener(mProjectSettingActivity.setPowerListener);
        mProjectSettingActivity.contral.setOnClickListener(mProjectSettingActivity.setContralListener);
        if(mProjectSettingActivity.leavetype == 3)
        {
            mProjectSettingActivity.power.setVisibility(View.GONE);
            mProjectSettingActivity.contral.setVisibility(View.GONE);
        }
        ToolBarHelper.setTitle(mProjectSettingActivity.mActionBar,mProjectSettingActivity.getString(R.string.project_more_setting));
        initOper();
    }

    public void initSelects()
    {
        Select moreModel = new Select("0",mProjectSettingActivity.getString(R.string.project_setting_power_0));
        if(mProjectSettingActivity.project.isPower == 0)
        moreModel.iselect = true;
        mProjectSettingActivity.powers.add(moreModel);
        moreModel = new Select("1",mProjectSettingActivity.getString(R.string.project_setting_power_1));
        if(mProjectSettingActivity.project.isPower == 1)
            moreModel.iselect = true;
        mProjectSettingActivity.powers.add(moreModel);
        moreModel = new Select("0",mProjectSettingActivity.getString(R.string.project_setting_contral_0));
        if(mProjectSettingActivity.project.isLayer == 0)
        moreModel.iselect = true;
        mProjectSettingActivity.contrals.add(moreModel);
        moreModel = new Select("1",mProjectSettingActivity.getString(R.string.project_setting_contral_1));
        if(mProjectSettingActivity.project.isLayer == 1)
            moreModel.iselect = true;
        mProjectSettingActivity.contrals.add(moreModel);

        if(mProjectSettingActivity.project.isPower == 1)
        {
            mProjectSettingActivity.powerValue.setText(mProjectSettingActivity.getString(R.string.project_setting_power_1));
        }
        else
        {
            mProjectSettingActivity.powerValue.setText(mProjectSettingActivity.getString(R.string.project_setting_power_0));
        }
        if(mProjectSettingActivity.project.isLayer == 1)
        {
            mProjectSettingActivity.contralValue.setText(mProjectSettingActivity.getString(R.string.project_setting_contral_1));
        }
        else
        {
            mProjectSettingActivity.contralValue.setText(mProjectSettingActivity.getString(R.string.project_setting_contral_0));
        }
    }

    public void initOper() {
        if(!TaskManager.getInstance().oaUtils.mAccount.mRecordId.equals(mProjectSettingActivity.project.leaderId))
        {
            mProjectSettingActivity.power.setVisibility(View.GONE);
            mProjectSettingActivity.contral.setVisibility(View.GONE);
        }
        else
        {
            mProjectSettingActivity.power.setVisibility(View.VISIBLE);
            mProjectSettingActivity.contral.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {
    }

    //net
    public void setPower() {
        SelectManager.getInstance().startSelectView(mProjectSettingActivity,mProjectSettingActivity.powers,mProjectSettingActivity.getString(R.string.project_setting_power_title),
                ProjectSettingActivity.ACTION_PROJECT_POWER,true,false);
    }

    public void setContral() {

        SelectManager.getInstance().startSelectView(mProjectSettingActivity,mProjectSettingActivity.contrals,mProjectSettingActivity.getString(R.string.project_setting_contral_title),
                ProjectSettingActivity.ACTION_PROJECT_CONTRAL,true,false);
    }

    public void doDelete()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mProjectSettingActivity);
        View view = View.inflate(mProjectSettingActivity, R.layout.alter_dialog1, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(mProjectSettingActivity.getString(R.string.project_setting_delete_title));
        TextView content = (TextView) view.findViewById(R.id.content);
        content.setText(mProjectSettingActivity.getString(R.string.project_setting_delete_content));
        TextView btn1 = (TextView) view.findViewById(R.id.btn1);
        btn1.setText(mProjectSettingActivity.getString(R.string.project_setting_delete_only));
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectAsks.deleteProject(mProjectSettingActivity,mProjectSettingHandler,mProjectSettingActivity.project);
                mProjectSettingActivity.dialog.dismiss();
            }
        });
        TextView btn2 = (TextView) view.findViewById(R.id.btn2);
        btn2.setText(mProjectSettingActivity.getString(R.string.project_setting_delete_all));
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectAsks.deleteProject(mProjectSettingActivity,mProjectSettingHandler,mProjectSettingActivity.project);
                String[] ids = mProjectSettingActivity.project.mTaskList.split(",");
                if(mProjectSettingActivity.project.mTaskList.length() > 0)
                {
                    for(int i = 0 ; i < ids.length ; i++)
                    {
                        TaskAsks.doDeleteById(mProjectSettingActivity,mProjectSettingHandler,ids[i],1);
                    }
                }
                mProjectSettingActivity.dialog.dismiss();
            }
        });
        TextView btn3 = (TextView) view.findViewById(R.id.cancle);
        btn3.setText(mProjectSettingActivity.getString(R.string.button_word_cancle));
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProjectSettingActivity.dialog.dismiss();
            }
        });
        mProjectSettingActivity.dialog = builder.create();
        mProjectSettingActivity.dialog.show();
    }

    public void doExist()
    {

        AppUtils.creatDialogTowButton(mProjectSettingActivity,"",mProjectSettingActivity.getString(R.string.project_setting_exits_title)
                ,mProjectSettingActivity.getString(R.string.button_word_cancle),mProjectSettingActivity.getString(R.string.button_word_ok),null,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        ProjectAsks.removeMember(mProjectSettingActivity,mProjectSettingHandler,mProjectSettingActivity.project,null);
                    }
                });

    }



    public void updtataTop()
    {
        if(mProjectSettingActivity.project.isTop == 1)
        {
            mProjectSettingActivity.mSwitch.setChecked(true);
        }
        else
        {
            mProjectSettingActivity.mSwitch.setChecked(false);
        }
    }

    public void updataPower()
    {
        mProjectSettingActivity.powerValue.setText(SelectManager.getInstance().mSignal.mName);
        mProjectSettingActivity.project.isPower = Integer.valueOf(SelectManager.getInstance().mSignal.mId);
        if(SelectManager.getInstance().mSignal.mId.equals("0"))
        {
            mProjectSettingActivity.powers.get(0).iselect = true;
            mProjectSettingActivity.powers.get(1).iselect = false;
        }
        else
        {
            mProjectSettingActivity.powers.get(0).iselect = false;
            mProjectSettingActivity.powers.get(1).iselect = true;
        }

    }

    public void updataContral()
    {
        mProjectSettingActivity.contralValue.setText(SelectManager.getInstance().mSignal.mName);
        mProjectSettingActivity.project.isLayer = Integer.valueOf(SelectManager.getInstance().mSignal.mId);
        if(SelectManager.getInstance().mSignal.mId.equals("0"))
        {
            mProjectSettingActivity.contrals.get(0).iselect = true;
            mProjectSettingActivity.contrals.get(1).iselect = false;
        }
        else
        {
            mProjectSettingActivity.contrals.get(0).iselect = false;
            mProjectSettingActivity.contrals.get(1).iselect = true;
        }
    }


}
