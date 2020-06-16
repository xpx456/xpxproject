package intersky.task.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.Actions;
import intersky.appbase.PermissionResult;
import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.TimeUtils;
import intersky.mywidget.MyLinearLayout;
import intersky.oa.OaUtils;
import intersky.select.SelectManager;
import intersky.select.entity.Select;
import intersky.task.R;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.handler.TaskCreatHandler;
import intersky.task.receiver.TaskCreatReceiver;
import intersky.task.view.activity.TaskCreatActivity;
import intersky.xpxnet.net.NetObject;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class TaskCreatPresenter implements Presenter {

    public TaskCreatHandler mTaskCreatHandler;
    public TaskCreatActivity mTaskCreatActivity;

    public TaskCreatPresenter(TaskCreatActivity mTaskCreatActivity) {
        this.mTaskCreatActivity = mTaskCreatActivity;
        this.mTaskCreatHandler = new TaskCreatHandler(mTaskCreatActivity);
        mTaskCreatActivity.setBaseReceiver(new TaskCreatReceiver(mTaskCreatHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mTaskCreatActivity.setContentView(R.layout.activity_creat_task);
        mTaskCreatActivity.mTask = mTaskCreatActivity.getIntent().getParcelableExtra("task");
        mTaskCreatActivity.parentTask = mTaskCreatActivity.getIntent().getParcelableExtra("parentask");
        mTaskCreatActivity.mProjectItemModel = mTaskCreatActivity.getIntent().getParcelableExtra("project");
        mTaskCreatActivity.mTaskDuty = (RelativeLayout) mTaskCreatActivity.findViewById(R.id.durty);
        mTaskCreatActivity.copyer = (MyLinearLayout) mTaskCreatActivity.findViewById(R.id.copyer);
        mTaskCreatActivity.mImageLayer = (LinearLayout) mTaskCreatActivity.findViewById(R.id.image_content);
        mTaskCreatActivity.mDateStart = (RelativeLayout) mTaskCreatActivity.findViewById(R.id.datestart);
        mTaskCreatActivity.mDateEnd = (RelativeLayout) mTaskCreatActivity.findViewById(R.id.dateend);
        mTaskCreatActivity.mDuty = (RelativeLayout) mTaskCreatActivity.findViewById(R.id.durty);
        mTaskCreatActivity.mLeaderText = (TextView) mTaskCreatActivity.findViewById(R.id.durty_value);
        mTaskCreatActivity.dateTextEnd = (TextView) mTaskCreatActivity.findViewById(R.id.dateend_value);
        mTaskCreatActivity.dateTextStart = (TextView) mTaskCreatActivity.findViewById(R.id.datestart_value);
        mTaskCreatActivity.taskName = (EditText) mTaskCreatActivity.findViewById(R.id.name_value);
        mTaskCreatActivity.proJect = (TextView) mTaskCreatActivity.findViewById(R.id.project_value);
        mTaskCreatActivity.durtyPerson = (TextView) mTaskCreatActivity.findViewById(R.id.durty_value);
        mTaskCreatActivity.desCtibe = (EditText) mTaskCreatActivity.findViewById(R.id.desc_value);
        mTaskCreatActivity.stage = (TextView) mTaskCreatActivity.findViewById(R.id.stage_value);
        mTaskCreatActivity.stageArea = (RelativeLayout) mTaskCreatActivity.findViewById(R.id.stage);
        mTaskCreatActivity.projectArea = (RelativeLayout) mTaskCreatActivity.findViewById(R.id.project);
        mTaskCreatActivity.mDuty.setOnClickListener(mTaskCreatActivity.setDutyListener);
        mTaskCreatActivity.mDateStart.setOnClickListener(mTaskCreatActivity.detepickListener1);
        mTaskCreatActivity.mDateEnd.setOnClickListener(mTaskCreatActivity.detepickListener2);
        mTaskCreatActivity.takePhoto = (RelativeLayout) mTaskCreatActivity.findViewById(R.id.buttom_takephoto);
        mTaskCreatActivity.getPhoto = (RelativeLayout) mTaskCreatActivity.findViewById(R.id.buttom_picture);
        mTaskCreatActivity.takePhoto.setOnClickListener(mTaskCreatActivity.mTakePhotoListenter);
        mTaskCreatActivity.getPhoto.setOnClickListener(mTaskCreatActivity.mAddPicListener);
        mTaskCreatActivity.projectArea.setOnClickListener(mTaskCreatActivity.setProject);
        mTaskCreatActivity.stageArea.setOnClickListener(mTaskCreatActivity.setStage);
        if(mTaskCreatActivity.mTask.leaderId.length() > 0)
        mTaskCreatActivity.mLeader = (Contacts) Bus.callData(mTaskCreatActivity,"chat/getContactItem",mTaskCreatActivity.mTask.leaderId);

        if(mTaskCreatActivity.mLeader != null)
        mTaskCreatActivity.mLeaderText.setText(mTaskCreatActivity.mLeader.getName());
        if (mTaskCreatActivity.mTask.projectId.length() > 0 && !mTaskCreatActivity.mTask.projectId.equals("0")) {
            mTaskCreatActivity.stageArea.setVisibility(View.VISIBLE);
            mTaskCreatActivity.proJect.setText(mTaskCreatActivity.mProjectItemModel.mName);
            initStages();
        }
        else {
            mTaskCreatActivity.stageArea.setVisibility(View.GONE);
        }
        ToolBarHelper.setTitle(mTaskCreatActivity.mActionBar, mTaskCreatActivity.getString(R.string.task_creat_title));
        ToolBarHelper.setRightBtnText(mTaskCreatActivity.mActionBar, mTaskCreatActivity.saveListener, mTaskCreatActivity.getString(R.string.button_word_finish));
        Bus.callData(mTaskCreatActivity,"chat/getContacts",mTaskCreatActivity.mTask.senderIdList, mTaskCreatActivity.mCopyers);
        initselectView(mTaskCreatActivity.mCopyers, mTaskCreatActivity.copyer, mTaskCreatActivity.copyerListener, mTaskCreatActivity.mDeleteCopyerListener, false);
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

    public void initStages() {
        mTaskCreatActivity.mStages.clear();
        try {
            JSONArray ja = new JSONArray(mTaskCreatActivity.mProjectItemModel.stageString);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Select mSelectMoreModel = new Select(jo.getString("project_stages_id"), jo.getString("name"));
                mTaskCreatActivity.mStages.add(mSelectMoreModel);
                if(mTaskCreatActivity.mTask.stageId.length() == 0)
                {
                    if(i == 0)
                    {
                        mTaskCreatActivity.mselectmStage = mSelectMoreModel;
                        mTaskCreatActivity.mTask.stageName = mSelectMoreModel.mName;
                        mTaskCreatActivity.mTask.stageId = mSelectMoreModel.mId;
                        mSelectMoreModel.iselect = true;
                        mTaskCreatActivity.stage.setText(mTaskCreatActivity.mTask.stageName);
                        if (mTaskCreatActivity.stage.getVisibility() == View.GONE) {
                            mTaskCreatActivity.stage.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else
                {
                    if (mSelectMoreModel.mId.equals(mTaskCreatActivity.mTask.stageId)) {
                        mTaskCreatActivity.mselectmStage = mSelectMoreModel;
                        mTaskCreatActivity.mTask.stageName = mSelectMoreModel.mName;
                        mTaskCreatActivity.mTask.stageId = mSelectMoreModel.mId;
                        mSelectMoreModel.iselect = true;
                        mTaskCreatActivity.stage.setText(mTaskCreatActivity.mTask.stageName);
                        if (mTaskCreatActivity.stage.getVisibility() == View.GONE) {
                            mTaskCreatActivity.stage.setVisibility(View.VISIBLE);
                        }
                    }
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getStageName(String id) {
        for(int i =0 ; i < mTaskCreatActivity.mStages.size() ; i++) {
            if(id.equals(mTaskCreatActivity.mStages.get(i).mId))
            {
                mTaskCreatActivity.mselectmStage = mTaskCreatActivity.mStages.get(i);
                return mTaskCreatActivity.mStages.get(i).mName;
            }
        }
        return "";
    }

    public void setDuty(Intent intent) {
        mTaskCreatActivity.mLeader  = intent.getParcelableExtra("contacts");
        for(int i = 0 ; i < mTaskCreatActivity.mCopyers.size() ; i++)
        {
            if(mTaskCreatActivity.mCopyers.get(i).mRecordid.equals(mTaskCreatActivity.mLeader.mRecordid))
            {
                mTaskCreatActivity.mCopyers.remove(mTaskCreatActivity.mLeader);
            }
        }
        mTaskCreatActivity.mLeaderText.setText(mTaskCreatActivity.mLeader.getName());
        initselectView(mTaskCreatActivity.mCopyers, mTaskCreatActivity.copyer, mTaskCreatActivity.copyerListener, mTaskCreatActivity.mDeleteCopyerListener, false);
    }

    public void startDuaySelect() {
        Bus.callData(mTaskCreatActivity,"chat/setContacts","负责人", TaskCreatActivity.ACTION_CREAT_SET_LEADER);

    }


    public void submit() {

        if (mTaskCreatActivity.taskName.getText().toString().replaceAll(" ", "").length() == 0) {

            AppUtils.showMessage(mTaskCreatActivity, mTaskCreatActivity.getString(R.string.task_creat_name_empty));
            return;
        }
        if (mTaskCreatActivity.mLeader == null) {

            AppUtils.showMessage(mTaskCreatActivity, mTaskCreatActivity.getString(R.string.task_creat_durty_empty));
            return;
        }


        if (mTaskCreatActivity.issub == false) {
            mTaskCreatActivity.issub = true;

            NetObject netObject = (NetObject) Bus.callData(mTaskCreatActivity,"filetools/getUploadFiles",mTaskCreatActivity.mPics);
            if(((ArrayList<File>) netObject.item).size() > 0)
            {
                TaskManager.getInstance().oaUtils.uploadAttchments((ArrayList<File>) netObject.item,mTaskCreatHandler,netObject.result);
            }
            else
            {
                doAddTask(netObject.result);
            }

        }
        else
        {
            mTaskCreatActivity.waitDialog.show();
        }
    }

    public void doAddTask(String json) {
        mTaskCreatActivity.mTask.attachmentHash = json;
        mTaskCreatActivity.mTask.attachmentName = "";
        for(int i = 0 ; i < mTaskCreatActivity.mPics.size() ; i++)
        {
            if(mTaskCreatActivity.mTask.attachmentName.length() == 0)
            {
                mTaskCreatActivity.mTask.attachmentName += mTaskCreatActivity.mPics.get(i).mName;
            }
            else
            {
                mTaskCreatActivity.mTask.attachmentName += ","+mTaskCreatActivity.mPics.get(i).mName;
            }
        }
        mTaskCreatActivity.mTask.taskName = mTaskCreatActivity.taskName.getText().toString();
        mTaskCreatActivity.mTask.leaderId = mTaskCreatActivity.mLeader.mRecordid;
        if(mTaskCreatActivity.parentTask != null)
        mTaskCreatActivity.mTask.parentId = mTaskCreatActivity.parentTask.taskId;
        if(mTaskCreatActivity.mProjectItemModel != null)
        mTaskCreatActivity.mTask.projectId = mTaskCreatActivity.mProjectItemModel.projectId;
        if(mTaskCreatActivity.mselectmStage != null)
        mTaskCreatActivity.mTask.stageId = mTaskCreatActivity.mselectmStage.mId;
        mTaskCreatActivity.mTask.des = mTaskCreatActivity.desCtibe.getText().toString();
        if(mTaskCreatActivity.mTask.beginTime.length() > 0 && mTaskCreatActivity.mTask.beginTime.length() <=16)
        mTaskCreatActivity.mTask.beginTime = mTaskCreatActivity.dateTextStart.getText().toString()+":00";
        if(mTaskCreatActivity.mTask.endTime.length() > 0 && mTaskCreatActivity.mTask.endTime.length() <=16)
        mTaskCreatActivity.mTask.endTime = mTaskCreatActivity.dateTextEnd.getText().toString()+":00";
        mTaskCreatActivity.mTask.senderIdList = (String) Bus.callData(mTaskCreatActivity,"chat/getMemberIds",mTaskCreatActivity.mCopyers);
        TaskAsks.addTask(mTaskCreatActivity,mTaskCreatHandler,mTaskCreatActivity.mTask);

    }


    public void selectProject() {
        TaskManager.getInstance().projectSelectAdapter.setSelect(mTaskCreatActivity.mTask.projectId);
        SelectManager.getInstance().startCustomSelectView(mTaskCreatActivity, TaskManager.getInstance().projectSelectAdapter,mTaskCreatActivity.selectProjectDetial,mTaskCreatActivity.getString(R.string.task_structure_aline_project2),
                TaskCreatActivity.ACTION_CREAT_SET_PROJECT,true,false);
    }

    public void selectStage() {
        SelectManager.getInstance().startSelectView(mTaskCreatActivity,mTaskCreatActivity.mStages,mTaskCreatActivity.getString(R.string.task_structure_change_stage),
                TaskCreatActivity.ACTION_CREAT_SET_STAGE,true,false);
    }

    public void onDataPick1() {

        if(mTaskCreatActivity.dateTextStart.getText().toString().length() != 0)
        {
            AppUtils.creatDataAndTimePicker(mTaskCreatActivity,mTaskCreatActivity.dateTextStart.getText().toString()+ ":00"
                    , mTaskCreatActivity.getString(R.string.task_detial_set_begin_1),mOnBeginSetListener);
        }
        else
        {
            AppUtils.creatDataAndTimePicker(mTaskCreatActivity, TimeUtils.getDateAndTime(), mTaskCreatActivity.getString(R.string.task_detial_set_begin_1),mOnBeginSetListener);
        }
    }

    public DoubleDatePickerDialog.OnDateSetListener mOnBeginSetListener = new DoubleDatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%04d-%02d-%02d %02d:%02d", startYear, startMonthOfYear + 1, startDayOfMonth, hour, miniute);
            if (mTaskCreatActivity.dateTextEnd.getText().length() > 0) {
                if (TimeUtils.measureBefore(textString + ":00",
                        mTaskCreatActivity.dateTextEnd.getText().toString() + ":00")) {
                    mTaskCreatActivity.dateTextStart.setText(textString);
                } else {
                    AppUtils.showMessage(mTaskCreatActivity, mTaskCreatActivity.getString(R.string.task_detial_time_hit));
                }
            } else {
                mTaskCreatActivity.dateTextStart.setText(textString);
            }

        }
    };

    public void onDataPick2() {
        if(mTaskCreatActivity.dateTextEnd.getText().toString().length() != 0)
        {
            AppUtils.creatDataAndTimePicker(mTaskCreatActivity,mTaskCreatActivity.dateTextEnd.getText().toString() + ":00"
                    , mTaskCreatActivity.getString(R.string.task_detial_set_end_1),mOnEndSetListener);
        }
        else
        {
            AppUtils.creatDataAndTimePicker(mTaskCreatActivity,TimeUtils.getDateAndTime(), mTaskCreatActivity.getString(R.string.task_detial_set_end_1),mOnEndSetListener);
        }
    }

    public DoubleDatePickerDialog.OnDateSetListener mOnEndSetListener = new DoubleDatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%04d-%02d-%02d %02d:%02d", startYear, startMonthOfYear + 1, startDayOfMonth, hour, miniute);
            if (mTaskCreatActivity.dateTextStart.getText().length() > 0) {
                if (TimeUtils.measureBefore(mTaskCreatActivity.dateTextStart.getText().toString() + ":00", textString + ":00")) {
                    mTaskCreatActivity.dateTextEnd.setText(textString);
                } else {
                    AppUtils.showMessage(mTaskCreatActivity, mTaskCreatActivity.getString(R.string.task_detial_time_hit));
                }
            } else {
                mTaskCreatActivity.dateTextEnd.setText(textString);
            }
        }
    };

    public void initselectView(ArrayList<Contacts> mselectitems, MyLinearLayout mlayer, View.OnClickListener senderListener, View.OnClickListener itemListener, boolean issender) {
        LayoutInflater mInflater = (LayoutInflater) mTaskCreatActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mlayer.removeAllViews();
        if (mselectitems.size() > 0) {
            for (int i = 0; i < mselectitems.size(); i++) {
                Contacts mContact = mselectitems.get(i);
                View mview = mInflater.inflate(R.layout.sample_contact_item_ex, null);
                TextView mhead = (TextView) mview.findViewById(R.id.contact_img);
                AppUtils.setContactCycleHead(mhead,mContact.getName(),mContact.colorhead);
                TextView name = (TextView) mview.findViewById(R.id.title);
                name.setText(mContact.getName());
                mview.setTag(mContact);
                mview.setOnClickListener(itemListener);
                mlayer.addView(mview);

                if (issender) {
                    View aview = mInflater.inflate(R.layout.arrayview, null);
                    mlayer.addView(aview);
                }
            }

        }
        View mview = mInflater.inflate(R.layout.sample_contact_item_add, null);
        mview.setOnClickListener(senderListener);
        mlayer.addView(mview);

    }

    public void selectCopyer() {

        ArrayList<Contacts> contacts = new ArrayList<Contacts>();
        contacts.add(mTaskCreatActivity.mLeader);
        Bus.callData(mTaskCreatActivity,"chat/selectListAddContact",mTaskCreatActivity.mCopyers,TaskCreatActivity.ACTION_ADD_COPYER,"选择联系人",false,contacts);
    }

    public void deleteCopyer(View v) {
        Contacts copy = (Contacts) v.getTag();
        mTaskCreatActivity.copyer.removeView(v);
        mTaskCreatActivity.mCopyers.remove(copy);
    }

    public void setCopyer() {
        mTaskCreatActivity.mCopyers.clear();
        mTaskCreatActivity.mCopyers.addAll((ArrayList<Contacts>)Bus.callData(mTaskCreatActivity,"chat/mselectitems",""));
        initselectView(mTaskCreatActivity.mCopyers, mTaskCreatActivity.copyer, mTaskCreatActivity.copyerListener, mTaskCreatActivity.mDeleteCopyerListener, false);
    }


    public void addPic() {
        Bus.callData(mTaskCreatActivity,"filetools/getPhotos",false,TaskManager.PIC_MAXSIZE,"intersky.notice.view.activity.NewNoticeActivity",TaskCreatActivity.ACTION_ADD_PICTORE);
    }

    public void initPicView(ArrayList<Attachment> mPics) {
        //mNoticeReadActivity.mImageLayer.removeAllViews();
        for (int i = 0; i < mPics.size(); i++) {
            Bus.callData(mTaskCreatActivity,"filetools/addPicViewListener",mPics.get(i),mTaskCreatActivity,"",mTaskCreatActivity.mImageLayer,mTaskCreatActivity.mDeletePicListener);
        }

    }

    public void setpic(Intent intent) {
        ArrayList<Attachment> attachments = intent.getParcelableArrayListExtra("attachments");
        mTaskCreatActivity.mPics.addAll(attachments);
        initPicView(attachments);
    }

    public void deletePic(int position) {
        View mview = mTaskCreatActivity.mImageLayer.getChildAt(position);
        mTaskCreatActivity.mImageLayer.removeView(mview);
        mTaskCreatActivity.mPics.remove(position);
        for (int i = 0; i < mTaskCreatActivity.mImageLayer.getChildCount(); i++) {
            View temp = mTaskCreatActivity.mImageLayer.getChildAt(i);
            ImageView close = (ImageView) temp.findViewById(R.id.close_image);
            close.setTag(i);
            Button button = (Button) temp.findViewById(R.id.close_image_b);
            button.setTag(i);
        }
    }

    public void takePhoto() {
        if(mTaskCreatActivity.mPics.size() < TaskManager.PIC_MAXSIZE)
        {
            mTaskCreatActivity.permissionRepuest = (PermissionResult) Bus.callData(mTaskCreatActivity,"filetools/checkPermissionTakePhoto"
                    ,mTaskCreatActivity,Bus.callData(mTaskCreatActivity,"filetools/getfilePath","notice/photo"));
        }
        else
        {
            AppUtils.showMessage(mTaskCreatActivity,mTaskCreatActivity.getString(R.string.keyword_photoaddmax1)
                    + String.valueOf(TaskManager.PIC_MAXSIZE)+mTaskCreatActivity.getString(R.string.keyword_photoaddmax2));
        }

    }


    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Actions.TAKE_PHOTO:
                File mFile = new File((String) Bus.callData(mTaskCreatActivity,"filetools/takePhotoUri",""));
                if (mFile.exists()) {
                    Attachment attachment = new Attachment("",mFile.getName(),"","",mFile.length(),mFile.length(),"");
                    mTaskCreatActivity.mPics.add(attachment);
                    Bus.callData(mTaskCreatActivity,"filetools/addPicViewListener",attachment,mTaskCreatActivity,"",mTaskCreatActivity.mImageLayer,mTaskCreatActivity.mDeletePicListener);
                }
                break;
        }
    }

    public void delete(View v) {
        final int position = (int) v.getTag();
        final AlertDialog.Builder builder = new AlertDialog.Builder(mTaskCreatActivity);
        builder.setMessage(mTaskCreatActivity.getString(R.string.cicle_delete_attachment));
        builder.setNegativeButton(mTaskCreatActivity.getString(R.string.button_word_cancle), null);
        builder.setPositiveButton(mTaskCreatActivity.getString(R.string.button_word_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                deletePic(position);
            }
        });
        builder.create().show();
    }

    public void updataTask(Intent intent) {
        String id = intent.getStringExtra("taskid");
        if(mTaskCreatActivity.parentTask != null)
        {
            if(mTaskCreatActivity.parentTask.taskId.equals(id))
            {
                mTaskCreatActivity.mTask.projectId = "";
            }
        }
    }

    public void updataProject(Intent intent) {
        String id = intent.getStringExtra("projectid");
        if(mTaskCreatActivity.mProjectItemModel != null)
        {
            if(mTaskCreatActivity.mProjectItemModel.projectId.equals(id))
            {
                mTaskCreatActivity.waitDialog.show();
                ProjectAsks.getProjectItemDetial(mTaskCreatActivity,mTaskCreatHandler,mTaskCreatActivity.mProjectItemModel);
            }
        }
    }

    public void updataName(Intent intent) {
        String projectid = intent.getStringExtra("projectid");
        String name = intent.getStringExtra("name");
        if(mTaskCreatActivity.mProjectItemModel.projectId.equals(projectid)) {
            mTaskCreatActivity.mProjectItemModel.mName = name;
            mTaskCreatActivity.proJect.setText(mTaskCreatActivity.mProjectItemModel.mName);
        }
    }
}
