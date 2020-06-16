package intersky.workreport.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import intersky.appbase.Actions;
import intersky.appbase.PermissionResult;
import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.TimeUtils;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Attachment;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.WebEdit;
import intersky.oa.OaUtils;
import intersky.workreport.R;
import intersky.workreport.WorkReportManager;
import intersky.workreport.asks.WorkReportAsks;
import intersky.workreport.handler.NewReportHandler;
import intersky.workreport.prase.WorkReportPrase;
import intersky.workreport.receiver.NewReportReceiver;
import intersky.workreport.view.activity.NewReportActivity;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

public class NewReportPresenter implements Presenter {

    public NewReportActivity mNewReportActivity;
    public NewReportHandler mNewReportHandler;

    public NewReportPresenter(NewReportActivity mNewReportActivity) {
        this.mNewReportActivity = mNewReportActivity;
        this.mNewReportHandler = new NewReportHandler(mNewReportActivity);
        mNewReportActivity.setBaseReceiver(new NewReportReceiver(mNewReportHandler));
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mNewReportActivity.mReport = mNewReportActivity.getIntent().getParcelableExtra("report");
        if (mNewReportActivity.mReport.mType == WorkReportManager.TYPE_WEEK) {
            if(mNewReportActivity.mReport.mRecordid.length() > 0)
            {
                mNewReportActivity.setContentView(R.layout.activity_creat_report_week);
                mNewReportActivity.summerText = (WebEdit) mNewReportActivity.findViewById(R.id.content5text);
                mNewReportActivity.summerText.setHit("#ceced3",mNewReportActivity.getString(R.string.xml_workreport_hinttext));
                mNewReportActivity.summerText.setTxtColor(Color.rgb(154,156,163));
                mNewReportActivity.summerText.setAction(WorkReportManager.ACTION_SET_WORK_CONTENT5);
                mNewReportActivity.summerText.setOnClickListener(mNewReportActivity.startEdit5Listener);
            }
            else
            {
                mNewReportActivity.setContentView(R.layout.activity_creat_report_week1);
                mNewReportActivity.summerText1 = (EditText) mNewReportActivity.findViewById(R.id.content5text);
                mNewReportActivity.summerText1.setOnTouchListener(mNewReportActivity);
            }

            mNewReportActivity.mworktime = (RelativeLayout) mNewReportActivity.findViewById(R.id.worktime);
            mNewReportActivity.worktimename1 = (TextView) mNewReportActivity.findViewById(R.id.worktimestart);
            mNewReportActivity.worktimename2 = (TextView) mNewReportActivity.findViewById(R.id.worktimeend);
            mNewReportActivity.worktimename1.setTag("start");
            mNewReportActivity.worktimename1.setOnClickListener(mNewReportActivity.detepickListener1);
            mNewReportActivity.worktimename2.setTag("end");
            mNewReportActivity.worktimename2.setOnClickListener(mNewReportActivity.detepickListener2);


        } else if (mNewReportActivity.mReport.mType == WorkReportManager.TYPE_DAY) {
            if(mNewReportActivity.mReport.mRecordid.length() > 0)
            {
                mNewReportActivity.setContentView(R.layout.activity_creat_report);

            }
            else
            {
                mNewReportActivity.setContentView(R.layout.activity_creat_report1);
            }
            mNewReportActivity.mworktime = (RelativeLayout) mNewReportActivity.findViewById(R.id.worktime);
            mNewReportActivity.worktimename = (TextView) mNewReportActivity.findViewById(R.id.worktimename);
            mNewReportActivity.mworktime.setOnClickListener(mNewReportActivity.detepickListener);
        } else if (mNewReportActivity.mReport.mType == WorkReportManager.TYPE_MONTH) {
            if(mNewReportActivity.mReport.mRecordid.length() > 0)
            {
                mNewReportActivity.setContentView(R.layout.activity_creat_report_month);
                mNewReportActivity.summerText = (WebEdit) mNewReportActivity.findViewById(R.id.content5text);
                mNewReportActivity.summerText.setHit("#ceced3",mNewReportActivity.getString(R.string.xml_workreport_hinttext));
                mNewReportActivity.summerText.setTxtColor(Color.rgb(154,156,163));
                mNewReportActivity.summerText.setAction(WorkReportManager.ACTION_SET_WORK_CONTENT5);
                mNewReportActivity.summerText.setOnClickListener(mNewReportActivity.startEdit5Listener);
            }
            else
            {
                mNewReportActivity.setContentView(R.layout.activity_creat_report_month1);
                mNewReportActivity.summerText1 = (EditText) mNewReportActivity.findViewById(R.id.content5text);
                mNewReportActivity.summerText1.setOnTouchListener(mNewReportActivity);
            }
            mNewReportActivity.mworktime = (RelativeLayout) mNewReportActivity.findViewById(R.id.worktime);
            mNewReportActivity.worktimename = (TextView) mNewReportActivity.findViewById(R.id.worktimename);
            mNewReportActivity.mworktime.setOnClickListener(mNewReportActivity.detepickListener);
        }
        mNewReportActivity.mshada = (RelativeLayout) mNewReportActivity.findViewById(R.id.shade);
        mNewReportActivity.scollayer = (ScrollView) mNewReportActivity.findViewById(R.id.scrollView1);
        if(mNewReportActivity.mReport.mRecordid.length() > 0)
        {
            mNewReportActivity.mNowWork = (WebEdit) mNewReportActivity.findViewById(R.id.content1text);
            mNewReportActivity.mNowWork.setHit("#ceced3",mNewReportActivity.getString(R.string.xml_workreport_hinttext));
            mNewReportActivity.mNowWork.setTxtColor(Color.rgb(154,156,163));
            mNewReportActivity.mNowWork.setAction(WorkReportManager.ACTION_SET_WORK_CONTENT1);
            mNewReportActivity.mNowWork.setOnClickListener(mNewReportActivity.startEdit1Listener);
            mNewReportActivity.mNextWork = (WebEdit) mNewReportActivity.findViewById(R.id.content2text);
            mNewReportActivity.mNextWork.setHit("#ceced3",mNewReportActivity.getString(R.string.xml_workreport_hinttext));
            mNewReportActivity.mNextWork.setTxtColor(Color.rgb(154,156,163));
            mNewReportActivity.mNextWork.setAction(WorkReportManager.ACTION_SET_WORK_CONTENT2);
            mNewReportActivity.mNextWork.setOnClickListener(mNewReportActivity.startEdit2Listener);
            mNewReportActivity.mWorkHelp = (WebEdit) mNewReportActivity.findViewById(R.id.content3text);
            mNewReportActivity.mWorkHelp.setHit("#ceced3",mNewReportActivity.getString(R.string.xml_workreport_hinttext));
            mNewReportActivity.mWorkHelp.setTxtColor(Color.rgb(154,156,163));
            mNewReportActivity.mWorkHelp.setAction(WorkReportManager.ACTION_SET_WORK_CONTENT3);
            mNewReportActivity.mWorkHelp.setOnClickListener(mNewReportActivity.startEdit3Listener);
            mNewReportActivity.mRemark = (WebEdit) mNewReportActivity.findViewById(R.id.content4text);
            mNewReportActivity.mRemark.setHit("#ceced3",mNewReportActivity.getString(R.string.xml_workreport_hinttext));
            mNewReportActivity.mRemark.setTxtColor(Color.rgb(154,156,163));
            mNewReportActivity.mRemark.setAction(WorkReportManager.ACTION_SET_WORK_CONTENT3);
            mNewReportActivity.mRemark.setOnClickListener(mNewReportActivity.startEdit4Listener);
        }
        else
        {
            mNewReportActivity.mNowWork1 = (EditText) mNewReportActivity.findViewById(R.id.content1text);
            mNewReportActivity.mNextWork1 = (EditText) mNewReportActivity.findViewById(R.id.content2text);
            mNewReportActivity.mWorkHelp1 = (EditText) mNewReportActivity.findViewById(R.id.content3text);
            mNewReportActivity.mRemark1 = (EditText) mNewReportActivity.findViewById(R.id.content4text);
            mNewReportActivity.mNowWork1.setOnTouchListener(mNewReportActivity);
            mNewReportActivity.mNextWork1.setOnTouchListener(mNewReportActivity);
            mNewReportActivity.mWorkHelp1.setOnTouchListener(mNewReportActivity);
            mNewReportActivity.mRemark1.setOnTouchListener(mNewReportActivity);
        }
        mNewReportActivity.mworkSender = (RelativeLayout) mNewReportActivity.findViewById(R.id.sendayer);
        mNewReportActivity.mworkCopyer = (RelativeLayout) mNewReportActivity.findViewById(R.id.ccayer);
        mNewReportActivity.sender = (MyLinearLayout) mNewReportActivity.findViewById(R.id.sender);
        mNewReportActivity.copyer = (MyLinearLayout) mNewReportActivity.findViewById(R.id.copyer);
        mNewReportActivity.takePhoto = (RelativeLayout) mNewReportActivity.findViewById(R.id.buttom_takephoto);
        mNewReportActivity.getPhoto = (RelativeLayout) mNewReportActivity.findViewById(R.id.buttom_picture);
        mNewReportActivity.takePhoto.setOnClickListener(mNewReportActivity.mTakePhotoListenter);
        mNewReportActivity.getPhoto.setOnClickListener(mNewReportActivity.mAddPicListener);

        mNewReportActivity.mImageLayer = (LinearLayout) mNewReportActivity.findViewById(R.id.image_content);
        ToolBarHelper.setRightBtnText(mNewReportActivity.mActionBar, mNewReportActivity.saveListener, mNewReportActivity.getString(R.string.button_word_save));
        prasseDetial();
    }

    public void prasseDetial() {

        if(mNewReportActivity.mReport.mRecordid.length() > 0)
        {
            mNewReportActivity.mNowWork.setText(mNewReportActivity.mReport.mComplete);
            mNewReportActivity.mNextWork.setText(mNewReportActivity.mReport.nextProject);
            mNewReportActivity.mWorkHelp.setText(mNewReportActivity.mReport.mHelp);
            mNewReportActivity.mRemark.setText(mNewReportActivity.mReport.mRemark);
        }
        else
        {
            mNewReportActivity.mNowWork1.setText(mNewReportActivity.mReport.mComplete);
            mNewReportActivity.mNextWork1.setText(mNewReportActivity.mReport.nextProject);
            mNewReportActivity.mWorkHelp1.setText(mNewReportActivity.mReport.mHelp);
            mNewReportActivity.mRemark1.setText(mNewReportActivity.mReport.mRemark);
        }
        if (mNewReportActivity.mReport.mType == WorkReportManager.TYPE_WEEK) {
            mNewReportActivity.worktimename1.setText(mNewReportActivity.mReport.mBegainTime);
            mNewReportActivity.worktimename2.setText(mNewReportActivity.mReport.mEndTime);
            if(mNewReportActivity.mReport.mRecordid.length() > 0)
            mNewReportActivity.summerText.setText(mNewReportActivity.mReport.mSummery);
            else
                mNewReportActivity.summerText1.setText(mNewReportActivity.mReport.mSummery);
        } else if (mNewReportActivity.mReport.mType == WorkReportManager.TYPE_DAY) {
            mNewReportActivity.worktimename.setText(mNewReportActivity.mReport.mBegainTime);

        } else if (mNewReportActivity.mReport.mType == WorkReportManager.TYPE_MONTH) {
            mNewReportActivity.worktimename.setText(mNewReportActivity.mReport.mBegainTime);
            if(mNewReportActivity.mReport.mRecordid.length() > 0)
                mNewReportActivity.summerText.setText(mNewReportActivity.mReport.mSummery);
            else
                mNewReportActivity.summerText1.setText(mNewReportActivity.mReport.mSummery);
        }
        Bus.callData(WorkReportManager.getInstance().context, "chat/getContacts", mNewReportActivity.mReport.mSenders, mNewReportActivity.mSenders);
        Bus.callData(WorkReportManager.getInstance().context, "chat/getContacts", mNewReportActivity.mReport.mCopyers, mNewReportActivity.mCopyers);
        initselectView(mNewReportActivity.mSenders, mNewReportActivity.sender, mNewReportActivity.senderListener, mNewReportActivity.mDeleteSenderListener);
        initselectView(mNewReportActivity.mCopyers, mNewReportActivity.copyer, mNewReportActivity.copyerListener, mNewReportActivity.mDeleteCopyerListener);
        mNewReportActivity.mImageLayer.removeAllViews();
        WorkReportPrase.praseAddtchment(mNewReportActivity.mReport.attachJson, mNewReportActivity.mPics, mNewReportActivity.mReport);
        praseAttahcmentViews();


    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();

    }

    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        mNewReportHandler = null;
    }

    public void onDataPick1() {
        AppUtils.creatDataPicker(mNewReportActivity, mNewReportActivity.mReport.mBegainTime, mNewReportActivity.getString(R.string.xml_select), mOnBeginSetListener);
    }

    public void onDataPick2() {
        AppUtils.creatDataPicker(mNewReportActivity, mNewReportActivity.mReport.mEndTime, mNewReportActivity.getString(R.string.xml_select), mOnEndSetListener);
    }

    public void onDataPick() {
        if (mNewReportActivity.mReport.mType == WorkReportManager.TYPE_DAY) {
            AppUtils.creatDataPicker(mNewReportActivity, mNewReportActivity.mReport.mBegainTime, mNewReportActivity.getString(R.string.xml_select), mOnDaySetListener);
        } else if (mNewReportActivity.mReport.mType == WorkReportManager.TYPE_MONTH) {
            AppUtils.creatMonthPicker(mNewReportActivity, mNewReportActivity.mReport.mEndTime, mNewReportActivity.getString(R.string.xml_select), mOnMonthSetListener);
        }
    }

    public void praseAttahcmentViews() {
        for (int i = 0; i < mNewReportActivity.mPics.size(); i++) {
            Attachment attachment = mNewReportActivity.mPics.get(i);
            String url = "";
            if (attachment.mRecordid.length() > 0) {
                url = WorkReportManager.getInstance().oaUtils.praseClodAttchmentUrl(attachment.mRecordid, (int) (40 * mNewReportActivity.mBasePresenter.mScreenDefine.density));
            }
            Bus.callData(mNewReportActivity,"filetools/addPicViewListener",attachment, url, mNewReportActivity.mImageLayer, mNewReportActivity.mDeletePicListener);
        }
    }


    public void setSender() {
        mNewReportActivity.mSenders.clear();

        mNewReportActivity.mSenders.addAll((ArrayList<Contacts>)Bus.callData(mNewReportActivity,"chat/mselectitems",""));
        initselectView(mNewReportActivity.mSenders, mNewReportActivity.sender, mNewReportActivity.senderListener, mNewReportActivity.mDeleteSenderListener);

    }

    public void setCopyer() {
        mNewReportActivity.mCopyers.clear();
        mNewReportActivity.mCopyers.addAll((ArrayList<Contacts>)Bus.callData(mNewReportActivity,"chat/mselectitems",""));
        initselectView(mNewReportActivity.mCopyers, mNewReportActivity.copyer, mNewReportActivity.copyerListener, mNewReportActivity.mDeleteCopyerListener);

    }

    public void setpic(Intent intent) {
        ArrayList<Attachment> attachments = intent.getParcelableArrayListExtra("attachments");
        mNewReportActivity.mPics.addAll(attachments);
        initPicView(attachments);
    }


    public void initPicView(ArrayList<Attachment> mPics) {
        for (int i = 0; i < mPics.size(); i++) {
            Bus.callData(mNewReportActivity,"filetools/addPicViewListener",mPics.get(i),"",mNewReportActivity.mImageLayer,mNewReportActivity.mDeletePicListener);
        }
    }

    public void selectSender() {
        Bus.callData(mNewReportActivity,"chat/selectListContact",mNewReportActivity.mSenders, WorkReportManager.ACTION_REPORT_UPATE_SENDER, "选择联系人", false);
    }

    public void selectCopyer() {
        Bus.callData(mNewReportActivity,"chat/selectListContact",mNewReportActivity.mCopyers, WorkReportManager.ACTION_REPORT_UPATE_COPYER, "选择联系人", false);
    }

    public void saveData() {
        if (mNewReportActivity.mReport.mType == WorkReportManager.TYPE_WEEK) {
            if (mNewReportActivity.worktimename1.getText().toString().length() == 0 || mNewReportActivity.worktimename2.getText().toString().length() == 0) {
                AppUtils.showMessage(mNewReportActivity, mNewReportActivity.getString(R.string.report_time_mesage));
                return;
            }
        }
        if (mNewReportActivity.mReport.mType == WorkReportManager.TYPE_DAY || mNewReportActivity.mReport.mType == WorkReportManager.TYPE_MONTH) {
            if (mNewReportActivity.worktimename.getText().toString().length() == 0) {
                AppUtils.showMessage(mNewReportActivity, mNewReportActivity.getString(R.string.report_time_mesage));
                return;
            }

        }
        if(mNewReportActivity.mReport.mRecordid.length() > 0)
        {
            if (mNewReportActivity.mNowWork.getText().toString().replaceAll(" ", "").length() == 0) {

                AppUtils.showMessage(mNewReportActivity, mNewReportActivity.getString(R.string.xml_workreport_finish));
                return;
            }
            if (mNewReportActivity.mNextWork.getText().toString().replaceAll(" ", "").length() == 0) {

                AppUtils.showMessage(mNewReportActivity, mNewReportActivity.getString(R.string.xml_workreport_plane));
                return;
            }
        }
        else
        {
            if (mNewReportActivity.mNowWork1.getText().toString().replaceAll(" ", "").length() == 0) {

                AppUtils.showMessage(mNewReportActivity, mNewReportActivity.getString(R.string.xml_workreport_finish));
                return;
            }
            if (mNewReportActivity.mNextWork1.getText().toString().replaceAll(" ", "").length() == 0) {

                AppUtils.showMessage(mNewReportActivity, mNewReportActivity.getString(R.string.xml_workreport_plane));
                return;
            }
        }

        if (mNewReportActivity.mSenders.size() == 0) {
            AppUtils.showMessage(mNewReportActivity, mNewReportActivity.getString(R.string.report_mesage_ssendss));
            return;
        }
        if (mNewReportActivity.issub == false) {
            mNewReportActivity.waitDialog.show();
            mNewReportActivity.issub = true;
            saveSenderAndCopyer();
            NetObject netObject = (NetObject) Bus.callData(mNewReportActivity,"filetools/getUploadFiles",mNewReportActivity.mPics);
            if (((ArrayList<File>) netObject.item).size() > 0) {
                WorkReportManager.getInstance().oaUtils.uploadAttchments((ArrayList<File>) netObject.item, mNewReportHandler, netObject.result);
            } else {
                doSave(netObject.result);
            }

        } else {
            mNewReportActivity.waitDialog.show();
        }

    }

    public void saveSenderAndCopyer() {
        String senderids = "";
        for (int i = 0; i < mNewReportActivity.mSenders.size(); i++) {
            Contacts mContacts = mNewReportActivity.mSenders.get(i);
            if (i != mNewReportActivity.mSenders.size() - 1) {
                senderids += mContacts.mRecordid + ",";
            } else {
                senderids += mContacts.mRecordid;
            }

        }
        String copyerids = "";
        for (int i = 0; i < mNewReportActivity.mCopyers.size(); i++) {
            Contacts mContacts = mNewReportActivity.mCopyers.get(i);
            if (i != mNewReportActivity.mCopyers.size() - 1) {
                copyerids += mContacts.mRecordid + ",";
            } else {
                copyerids += mContacts.mRecordid;
            }

        }
        SharedPreferences sharedPre = mNewReportActivity.getSharedPreferences(WorkReportManager.REPORT_INFO, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putString(WorkReportManager.REPORTSENDER + WorkReportManager.getInstance().oaUtils.mAccount.mRecordId
                + WorkReportManager.getInstance().oaUtils.service.sAddress + WorkReportManager.getInstance().oaUtils.service.sPort, senderids);
        e.putString(WorkReportManager.REPORTCOPYER + WorkReportManager.getInstance().oaUtils.mAccount.mRecordId
                + WorkReportManager.getInstance().oaUtils.service.sAddress + WorkReportManager.getInstance().oaUtils.service.sPort, copyerids);
        e.commit();
    }


    public void doSave(String attence) {

        if(mNewReportActivity.mReport.mRecordid.length() > 0)
        {
            mNewReportActivity.mReport.mComplete = mNewReportActivity.mNowWork.getText().toString();
            if(mNewReportActivity.mReport.mType != WorkReportManager.TYPE_DAY)
                mNewReportActivity.mReport.mSummery = mNewReportActivity.summerText.getText().toString();
            mNewReportActivity.mReport.nextProject = mNewReportActivity.mNextWork.getText().toString();
            mNewReportActivity.mReport.mHelp = mNewReportActivity.mWorkHelp.getText().toString();
            mNewReportActivity.mReport.mRemark = mNewReportActivity.mRemark.getText().toString();
        }
        else
        {
            mNewReportActivity.mReport.mComplete = mNewReportActivity.mNowWork1.getText().toString();
            if(mNewReportActivity.mReport.mType != WorkReportManager.TYPE_DAY)
                mNewReportActivity.mReport.mSummery = mNewReportActivity.summerText1.getText().toString();
            mNewReportActivity.mReport.nextProject = mNewReportActivity.mNextWork1.getText().toString();
            mNewReportActivity.mReport.mHelp = mNewReportActivity.mWorkHelp1.getText().toString();
            mNewReportActivity.mReport.mRemark = mNewReportActivity.mRemark1.getText().toString();
        }

        mNewReportActivity.mReport.mSenders = (String) Bus.callData(mNewReportActivity,"chat/getMemberIds",mNewReportActivity.mSenders);
        mNewReportActivity.mReport.mCopyers = (String) Bus.callData(mNewReportActivity,"chat/getMemberIds",mNewReportActivity.mCopyers);
        mNewReportActivity.mReport.mAttence = attence;
        mNewReportActivity.waitDialog.show();
        WorkReportAsks.saveReport(mNewReportActivity, mNewReportHandler, mNewReportActivity.mReport);
    }

    public void initselectView(ArrayList<Contacts> mselectitems, MyLinearLayout mlayer, View.OnClickListener senderListener, View.OnClickListener itemListener) {
        LayoutInflater mInflater = (LayoutInflater) mNewReportActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mlayer.removeAllViews();
        if (mselectitems.size() > 0) {
            for (int i = 0; i < mselectitems.size(); i++) {
                Contacts mContact = mselectitems.get(i);
                View mview = mInflater.inflate(R.layout.sample_contact_item_ex, null);
                TextView mhead = (TextView) mview.findViewById(R.id.contact_img);
                Bus.callData(mNewReportActivity,"chat/setContactCycleHead",mhead, mContact);
                TextView name = (TextView) mview.findViewById(R.id.title);
                name.setText(mContact.getName());
                mview.setTag(mContact);
                mview.setOnClickListener(itemListener);
                mlayer.addView(mview);
            }

        }
        View mview = mInflater.inflate(R.layout.sample_contact_item_add, null);
        mview.setOnClickListener(senderListener);
        mlayer.addView(mview);

    }


    public void addPic() {
        Bus.callData(mNewReportActivity,"filetools/getPhotos",false, WorkReportManager.MAX_PIC_COUNT, "intersky.workreport.view.activity.NewReportActivity", WorkReportManager.ACTION_REPORT_ADDPICTORE);
    }


    public void showPic(View v) {
        Attachment mAttachmentModel = (Attachment) v.getTag();
        Bus.callData(mNewReportActivity,"filetools/startAttachment",mAttachmentModel);
    }

    public void deleteSender(View v) {
        Contacts send = (Contacts) v.getTag();
        mNewReportActivity.sender.removeView(v);
        mNewReportActivity.mSenders.remove(send);
    }

    public void deleteCopyer(View v) {
        Contacts copy = (Contacts) v.getTag();
        mNewReportActivity.copyer.removeView(v);
        mNewReportActivity.mCopyers.remove(copy);
    }


    public boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    public void takePhoto() {
        if (mNewReportActivity.mPics.size() < WorkReportManager.MAX_PIC_COUNT) {
            mNewReportActivity.permissionRepuest = (PermissionResult) Bus.callData(mNewReportActivity,"filetools/checkPermissionTakePhoto"
                    ,mNewReportActivity,Bus.callData(mNewReportActivity,"filetools/getfilePath","report/photo"));
        } else {
            AppUtils.showMessage(mNewReportActivity, mNewReportActivity.getString(R.string.keyword_photoaddmax1)
                    + String.valueOf(WorkReportManager.MAX_PIC_COUNT) + mNewReportActivity.getString(R.string.keyword_photoaddmax2));
        }
    }


    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Actions.TAKE_PHOTO:
                File mFile = new File((String) Bus.callData(mNewReportActivity,"filetools/takePhotoUri",""));
                if (mFile.exists()) {
                    Attachment attachment = new Attachment("",mFile.getName(),mFile.getPath(),"",mFile.length(),mFile.length(),"");
                    mNewReportActivity.mPics.add(attachment);
                    Bus.callData(mNewReportActivity,"filetools/addPicViewListener",attachment,"",mNewReportActivity.mImageLayer,mNewReportActivity.mDeletePicListener);
                }
                break;
        }
    }


    public DoubleDatePickerDialog.OnDateSetListener mOnBeginSetListener = new DoubleDatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%04d-%02d-%02d", startYear, startMonthOfYear + 1, startDayOfMonth);

            int count = TimeUtils.measureDayCount(textString + " 00:00:00", mNewReportActivity.mReport.mEndTime + " 00:00:00");
            if (count <= 0) {
                AppUtils.showMessage(mNewReportActivity, mNewReportActivity.getString(R.string.message_begin_end));
            } else {
                mNewReportActivity.mReport.mBegainTime = textString + " 00:00:00";
                mNewReportActivity.worktimename1.setText(textString);
            }
        }
    };

    public DoubleDatePickerDialog.OnDateSetListener mOnEndSetListener = new DoubleDatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%04d-%02d-%02d", startYear, startMonthOfYear + 1, startDayOfMonth);


            int count = TimeUtils.measureDayCount(mNewReportActivity.mReport.mBegainTime + " 00:00:00", textString + " 00:00:00");
            if (count <= 0) {
                AppUtils.showMessage(mNewReportActivity, mNewReportActivity.getString(R.string.message_begin_end));
            } else {
                mNewReportActivity.mReport.mEndTime = textString + " 23:59:59";
                mNewReportActivity.worktimename2.setText(textString);
            }
        }
    };

    public DoubleDatePickerDialog.OnDateSetListener mOnDaySetListener = new DoubleDatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%04d-%02d-%02d", startYear, startMonthOfYear + 1, startDayOfMonth);
            mNewReportActivity.mReport.mBegainTime = textString + " 00:00:00";
            mNewReportActivity.mReport.mEndTime = textString + " 23:59:59";
            mNewReportActivity.worktimename.setText(textString);
        }
    };

    public DoubleDatePickerDialog.OnDateSetListener mOnMonthSetListener = new DoubleDatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%04d-%02d", startYear, startMonthOfYear + 1);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            Calendar a = Calendar.getInstance();
            try {
                a.setTime(format.parse(textString));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int maxDate = a.get(Calendar.DATE);
            mNewReportActivity.mReport.mBegainTime = String.format("%04d-%02d-01 00:00:00", startYear, startMonthOfYear + 1);
            mNewReportActivity.mReport.mEndTime = String.format("%04d-%02d-%02d 23:59:59", startYear, startMonthOfYear + 1, maxDate);
            mNewReportActivity.worktimename.setText(textString);
        }
    };

    public void deletePic(Attachment attachment) {
        AppUtils.creatDialogTowButton(mNewReportActivity, mNewReportActivity.getString(R.string.cicle_delete_attachment), "", mNewReportActivity.getString(R.string.button_word_cancle),
                mNewReportActivity.getString(R.string.button_word_ok), null, (DialogInterface.OnClickListener) Bus.callData(mNewReportActivity,"filetools/DeletePicListener",attachment, mNewReportActivity.mPics, mNewReportActivity.mImageLayer));
    }

    public void startEdit5()
    {
        Bus.callData(mNewReportActivity,"riche/startEdit",WorkReportManager.ACTION_SET_WORK_CONTENT5,mNewReportActivity.summerText.html);
    }

    public void startEdit1()
    {
        Bus.callData(mNewReportActivity,"riche/startEdit",WorkReportManager.ACTION_SET_WORK_CONTENT1,mNewReportActivity.mNowWork.html);
    }

    public void startEdit2()
    {
        Bus.callData(mNewReportActivity,"riche/startEdit",WorkReportManager.ACTION_SET_WORK_CONTENT2,mNewReportActivity.mNextWork.html);
    }

    public void startEdit3()
    {
        Bus.callData(mNewReportActivity,"riche/startEdit",WorkReportManager.ACTION_SET_WORK_CONTENT3,mNewReportActivity.mWorkHelp.html);
    }

    public void startEdit4()
    {
        Bus.callData(mNewReportActivity,"riche/startEdit",WorkReportManager.ACTION_SET_WORK_CONTENT4,mNewReportActivity.mRemark.html);
    }
}
