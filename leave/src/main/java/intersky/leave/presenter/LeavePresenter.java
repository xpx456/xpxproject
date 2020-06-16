package intersky.leave.presenter;

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
import android.widget.TextView;

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
import intersky.leave.LeaveManager;
import intersky.leave.R;
import intersky.leave.asks.LeaveAsks;
import intersky.leave.handler.LeaveHandler;
import intersky.leave.prase.LeavePrase;
import intersky.leave.receiver.LeaveReceiver;
import intersky.leave.view.activity.LeaveActivity;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.WebEdit;
import intersky.select.SelectManager;
import intersky.select.entity.Select;
import intersky.xpxnet.net.NetObject;
import xpx.com.toolbar.utils.ToolBarHelper;


public class LeavePresenter implements Presenter {

    public LeaveActivity mLeaveActivity;
    public LeaveHandler mLeaveHandler;

    public LeavePresenter(LeaveActivity mLeaveActivity) {
        this.mLeaveActivity = mLeaveActivity;
        mLeaveHandler = new LeaveHandler(mLeaveActivity);
        mLeaveActivity.setBaseReceiver(new LeaveReceiver(mLeaveHandler));
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mLeaveActivity.mLeave = mLeaveActivity.getIntent().getParcelableExtra("leave");
        if(mLeaveActivity.mLeave.lid.length() > 0)
        mLeaveActivity.setContentView(R.layout.activity_leave);
        else
            mLeaveActivity.setContentView(R.layout.activity_leave1);
        ToolBarHelper.setTitle(mLeaveActivity.mActionBar, mLeaveActivity.getString(R.string.keyword_leave));
        ToolBarHelper.setRightBtnText(mLeaveActivity.mActionBar, mLeaveActivity.mSubmitListener, mLeaveActivity.getString(R.string.button_word_summit));
        mLeaveActivity.mRelativeLayout = (RelativeLayout) mLeaveActivity.findViewById(R.id.shade);
        mLeaveActivity.mBeginText = (TextView) mLeaveActivity.findViewById(R.id.leavebeginname);
        mLeaveActivity.mEndText = (TextView) mLeaveActivity.findViewById(R.id.leaveendname);
        mLeaveActivity.mBeginLayer = (RelativeLayout) mLeaveActivity.findViewById(R.id.leavebegin);
        mLeaveActivity.mEndLayer = (RelativeLayout) mLeaveActivity.findViewById(R.id.leaveend);
        mLeaveActivity.mTypeLayer = (RelativeLayout) mLeaveActivity.findViewById(R.id.leavetype);
        mLeaveActivity.mTypeText = (TextView) mLeaveActivity.findViewById(R.id.leavetypename);
        mLeaveActivity.mCountText = (EditText) mLeaveActivity.findViewById(R.id.leavedayname);
        if(mLeaveActivity.mLeave.lid.length() > 0)
        {
            mLeaveActivity.mReasonText = (WebEdit) mLeaveActivity.findViewById(R.id.content1text);
            mLeaveActivity.mReasonText.setHit("#ceced3",mLeaveActivity.getString(R.string.xml_leave_reason_i));
            mLeaveActivity.mReasonText.setTxtColor(Color.rgb(154,156,163));
            mLeaveActivity.mReasonText.setAction(LeaveManager.ACTION_SET_LEAVE_CONTENT);
            mLeaveActivity.mReasonText.setOnClickListener(mLeaveActivity.startEditListener);
        }
        else
            mLeaveActivity.mReasonText1 = (EditText) mLeaveActivity.findViewById(R.id.content1text);
        mLeaveActivity.mBeginLayer.setOnClickListener(mLeaveActivity.mStartTextListener);
        mLeaveActivity.mEndLayer.setOnClickListener(mLeaveActivity.mEndTextListener);
        mLeaveActivity.mTypeLayer.setOnClickListener(mLeaveActivity.mTypeListener);
        mLeaveActivity.mImageLayer = (LinearLayout) mLeaveActivity.findViewById(R.id.image_content);
        mLeaveActivity.sender = (MyLinearLayout) mLeaveActivity.findViewById(R.id.sender);
        mLeaveActivity.copyer = (MyLinearLayout) mLeaveActivity.findViewById(R.id.copyer);
        mLeaveActivity.takePhoto = (RelativeLayout) mLeaveActivity.findViewById(R.id.buttom_takephoto);
        mLeaveActivity.getPhoto = (RelativeLayout) mLeaveActivity.findViewById(R.id.buttom_picture);
        mLeaveActivity.takePhoto.setOnClickListener(mLeaveActivity.mTakePhotoListenter);
        mLeaveActivity.getPhoto.setOnClickListener(mLeaveActivity.mAddPicListener);
        if(LeaveManager.getInstance().mLeaveTypes.size() == 0)
        {
            LeaveManager.getInstance().getLeaveType();
        }
        initDetial();

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
    }

    public void initDetial() {
        mLeaveActivity.mBeginText.setText(mLeaveActivity.mLeave.start.substring(0, 16));
        mLeaveActivity.mEndText.setText(mLeaveActivity.mLeave.end.substring(0, 16));
        mLeaveActivity.mTypeText.setText(LeaveManager.getInstance().gettypeName(mLeaveActivity.mLeave.leave_type_id));
        mLeaveActivity.mCountText.setText(mLeaveActivity.mLeave.count);
        if(mLeaveActivity.mLeave.lid.length() > 0)
        mLeaveActivity.mReasonText.setText(mLeaveActivity.mLeave.content);
        else
            mLeaveActivity.mReasonText1.setText(mLeaveActivity.mLeave.content);
        Bus.callData(mLeaveActivity,"chat/getContacts",mLeaveActivity.mLeave.senders, mLeaveActivity.mSenders);
        Bus.callData(mLeaveActivity,"chat/getContacts",mLeaveActivity.mLeave.copyers, mLeaveActivity.mCopyers);
        initselectView(mLeaveActivity.mSenders, mLeaveActivity.sender, mLeaveActivity.senderListener, mLeaveActivity.mDeleteSenderListener, true);
        initselectView(mLeaveActivity.mCopyers, mLeaveActivity.copyer, mLeaveActivity.copyerListener, mLeaveActivity.mDeleteCopyerListener, false);
        mLeaveActivity.mImageLayer.removeAllViews();
        if(mLeaveActivity.mLeave.attachjson.length() > 0)
        LeavePrase.praseAddtchment(mLeaveActivity.mLeave.attachjson, mLeaveActivity.mPics, mLeaveActivity.mLeave);
        praseAttahcmentViews();
    }

    public void praseAttahcmentViews() {
        for(int i = 0 ; i < mLeaveActivity.mPics.size() ; i++)
        {
            Attachment attachment = mLeaveActivity.mPics.get(i);
            String url = "";
            if(attachment.mRecordid.length() > 0)
            {
                url = LeaveManager.getInstance().oaUtils.praseClodAttchmentUrl(attachment.mRecordid, (int) (40 * mLeaveActivity.mBasePresenter.mScreenDefine.density));
            }
            Bus.callData(mLeaveActivity,"filetools/addPicViewListener",attachment,url,mLeaveActivity.mImageLayer,mLeaveActivity.mDeletePicListener);
        }
    }


    public void deleteSender(View v) {
        Contacts send = (Contacts) v.getTag();
        int a = mLeaveActivity.sender.indexOfChild(v);
        View array = mLeaveActivity.sender.getChildAt(a + 1);
        mLeaveActivity.sender.removeView(v);
        mLeaveActivity.sender.removeView(array);
        mLeaveActivity.mSenders.remove(send);
    }

    public void deleteCopyer(View v) {
        Contacts copy = (Contacts) v.getTag();
        mLeaveActivity.copyer.removeView(v);
        mLeaveActivity.mCopyers.remove(copy);
    }

    public void selectSender() {
        Bus.callData(mLeaveActivity,"chat/selectListContact",mLeaveActivity.mSenders,LeaveManager.ACTION_LEAVE_UPATE_SENDER,"选择联系人",false);
    }

    public void selectCopyer() {
        Bus.callData(mLeaveActivity,"chat/selectListContact",mLeaveActivity.mCopyers,LeaveManager.ACTION_LEAVE_UPATE_COPYER,"选择联系人",false);
    }

    public void initselectView(ArrayList<Contacts> mselectitems, MyLinearLayout mlayer, View.OnClickListener senderListener, View.OnClickListener itemListener, boolean issender) {
        LayoutInflater mInflater = (LayoutInflater) mLeaveActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mlayer.removeAllViews();
        if (mselectitems.size() > 0) {
            for (int i = 0; i < mselectitems.size(); i++) {
                Contacts mContact = mselectitems.get(i);
                View mview = mInflater.inflate(R.layout.sample_contact_item_ex, null);
                TextView mhead = (TextView) mview.findViewById(R.id.contact_img);
                Bus.callData(mLeaveActivity,"chat/setContactCycleHead",mhead,mContact);
                TextView name = (TextView) mview.findViewById(R.id.title);
                name.setText(mContact.mName);
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

    public void setSender() {
        mLeaveActivity.mSenders.clear();
        mLeaveActivity.mSenders.addAll((ArrayList<Contacts>)Bus.callData(mLeaveActivity,"chat/mselectitems",""));
        initselectView(mLeaveActivity.mSenders, mLeaveActivity.sender, mLeaveActivity.senderListener, mLeaveActivity.mDeleteSenderListener, true);

    }

    public void setCopyer() {
        mLeaveActivity.mCopyers.clear();
        mLeaveActivity.mCopyers.addAll((ArrayList<Contacts>)Bus.callData(mLeaveActivity,"chat/mselectitems",""));
        initselectView(mLeaveActivity.mCopyers, mLeaveActivity.copyer, mLeaveActivity.copyerListener, mLeaveActivity.mDeleteCopyerListener, false);

    }



    public void showBeginTimeDialog() {
        AppUtils.creatDataAndTimePicker(mLeaveActivity,mLeaveActivity.mLeave.start, mLeaveActivity.getString(R.string.keyword_begin),mOnBeginSetListener);
    }

    public void showEndTimeDialog() {
        AppUtils.creatDataAndTimePicker(mLeaveActivity,mLeaveActivity.mLeave.start, mLeaveActivity.getString(R.string.keyword_end),mOnEndSetListener);
    }

    public void setType() {
        if(LeaveManager.getInstance().mLeaveTypes.size() > 0)
        {
            Select select = LeaveManager.getInstance().gettype(mLeaveActivity.mLeave.leave_type_id);
            if(select != null)
            {
                if(select.iselect == false)
                {
                    LeaveManager.getInstance().cleanSelect();
                    select.iselect = true;
                }

            }
            SelectManager.getInstance().startSelectView(mLeaveActivity,LeaveManager.getInstance().mLeaveTypes,mLeaveActivity.getString(R.string.xml_leave_type_title),
                    LeaveManager.ACTION_SET_LEAVE_TYPE,true,false);
        }
        else
        {
            AppUtils.showMessage(mLeaveActivity,mLeaveActivity.getString(R.string.message_leave_type_fail));
        }
    }


    public void addPic() {
        Bus.callData(mLeaveActivity,"filetools/getPhotos",false,LeaveManager.MAX_PIC_COUNT,"intersky.leave.view.activity.LeaveActivity",LeaveManager.ACTION_LEAVE_ADDPICTORE);
    }

    public void initPicView(ArrayList<Attachment> mPics) {
        for (int i = 0; i < mPics.size(); i++) {
            Bus.callData(mLeaveActivity,"filetools/addPicViewListener",mPics.get(i),"",mLeaveActivity.mImageLayer,mLeaveActivity.mDeletePicListener);
        }
    }

    public void setpic(Intent intent) {
        ArrayList<Attachment> attachments = intent.getParcelableArrayListExtra("attachments");
        mLeaveActivity.mPics.addAll(attachments);
        initPicView(attachments);
    }


    public void takePhoto() {
        if(mLeaveActivity.mPics.size() < LeaveManager.MAX_PIC_COUNT)
        {
            mLeaveActivity.permissionRepuest = (PermissionResult) Bus.callData(mLeaveActivity,"filetools/checkPermissionTakePhoto"
                    ,mLeaveActivity,Bus.callData(mLeaveActivity,"filetools/getfilePath","leave/photo"));
        }
        else
        {
            AppUtils.showMessage(mLeaveActivity,mLeaveActivity.getString(R.string.keyword_photoaddmax1)
                    + String.valueOf(LeaveManager.MAX_PIC_COUNT)+mLeaveActivity.getString(R.string.keyword_photoaddmax2));
        }

    }


    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Actions.TAKE_PHOTO:
                File mFile = new File((String) Bus.callData(mLeaveActivity,"filetools/takePhotoUri",""));
                if (mFile.exists()) {
                    Attachment attachment = new Attachment("",mFile.getName(),mFile.getPath(),"",mFile.length(),mFile.length(),"");
                    mLeaveActivity.mPics.add(attachment);
                    Bus.callData(mLeaveActivity,"filetools/addPicViewListener",attachment,"",mLeaveActivity.mImageLayer,mLeaveActivity.mDeletePicListener);
                }
                break;
        }
    }


    public void uploadImages() {
        mLeaveActivity.waitDialog.show();
        if (mLeaveHandler != null)
            mLeaveHandler.sendEmptyMessageDelayed(LeaveActivity.EVENT_DO_UPPIC, 100);
    }


    public void submit() {

        int count = TimeUtils.measureDayCount(mLeaveActivity.mBeginText.getText() + ":00", mLeaveActivity.mEndText.getText() + ":00");
        if (mLeaveActivity.mLeave.leave_type_id.equals("-1")) {
            AppUtils.showMessage(mLeaveActivity, mLeaveActivity.getString(R.string.message_leave_type));
            return;
        } else if (count < 0) {

            AppUtils.showMessage(mLeaveActivity, mLeaveActivity.getString(R.string.message_begin_end));
            return;
        } else if (mLeaveActivity.mCountText.getText().toString().length() == 0) {
            AppUtils.showMessage(mLeaveActivity, mLeaveActivity.getString(R.string.keyword_leave_day));
            return;
        }
        if(mLeaveActivity.mLeave.lid.length() > 0)
        {
            if (mLeaveActivity.mReasonText.getText().length() == 0) {
                AppUtils.showMessage(mLeaveActivity, mLeaveActivity.getString(R.string.message_leave_reason));
                return;
            }
        }
        else
        {
            if (mLeaveActivity.mReasonText1.getText().toString().length() == 0) {
                AppUtils.showMessage(mLeaveActivity, mLeaveActivity.getString(R.string.message_leave_reason));
                return;
            }
        }
        if (mLeaveActivity.mSenders.size() == 0) {
            AppUtils.showMessage(mLeaveActivity, mLeaveActivity.getString(R.string.message_leave_approve));
            return;
        }

        if (mLeaveActivity.issub == false) {
            mLeaveActivity.issub = true;
            saveSenderAndCopyer();
            NetObject netObject = (NetObject) Bus.callData(mLeaveActivity,"filetools/getUploadFiles",mLeaveActivity.mPics);
            if(((ArrayList<File>) netObject.item).size() > 0)
            {
                mLeaveActivity.waitDialog.show();
                LeaveManager.getInstance().oaUtils.uploadAttchments((ArrayList<File>) netObject.item,mLeaveHandler,netObject.result);
            }
            else
            {
                doSave(netObject.result);
            }

        } else {
            mLeaveActivity.waitDialog.show();
        }


    }

    public void doSave(String attach) {
        mLeaveActivity.mLeave.count = mLeaveActivity.mCountText.getText().toString();
        if(mLeaveActivity.mLeave.lid .length() > 0)
        {
            mLeaveActivity.mLeave.content = mLeaveActivity.mReasonText.getText();
        }
        else
        {
            mLeaveActivity.mLeave.content = mLeaveActivity.mReasonText1.getText().toString();
        }

        mLeaveActivity.mLeave.senders = (String) Bus.callData(mLeaveActivity,"chat/getMemberIds",mLeaveActivity.mSenders);
        mLeaveActivity.mLeave.copyers = (String) Bus.callData(mLeaveActivity,"chat/getMemberIds",mLeaveActivity.mCopyers);
        mLeaveActivity.mLeave.start = mLeaveActivity.mBeginText.getText().toString()+":00";
        mLeaveActivity.mLeave.end = mLeaveActivity.mEndText.getText().toString()+":00";
        mLeaveActivity.mLeave.attachs = attach;
        mLeaveActivity.waitDialog.show();
        LeaveAsks.saveLeave(mLeaveHandler,mLeaveActivity,mLeaveActivity.mLeave);
    }


    public void settype() {
        mLeaveActivity.mLeave.leave_type_id = SelectManager.getInstance().mSignal.mId;
        mLeaveActivity.mTypeText.setText(SelectManager.getInstance().mSignal.mName);
    }

    public void deletePic(Attachment attachment) {
        AppUtils.creatDialogTowButton(mLeaveActivity,mLeaveActivity.getString(R.string.cicle_delete_attachment),"",mLeaveActivity.getString(R.string.button_word_cancle),
                mLeaveActivity.getString(R.string.button_word_ok),null, (DialogInterface.OnClickListener) Bus.callData(mLeaveActivity,"filetools/DeletePicListener",attachment,mLeaveActivity.mPics,mLeaveActivity.mImageLayer));
    }


    public DoubleDatePickerDialog.OnDateSetListener mOnBeginSetListener = new DoubleDatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%04d-%02d-%02d %02d:%02d", startYear, startMonthOfYear + 1, startDayOfMonth, hour, miniute);
            int count = TimeUtils.measureDayCount3(textString + ":00", mLeaveActivity.mEndText.getText() + ":00");
            if (count <= 0) {
                AppUtils.showMessage(mLeaveActivity, mLeaveActivity.getString(R.string.message_begin_end));
            } else {
                mLeaveActivity.mBeginText.setText(textString);
                //mNewNoticeActivity.mCountText.setText(String.valueOf(count));
            }
        }
    };

    public DoubleDatePickerDialog.OnDateSetListener mOnEndSetListener = new DoubleDatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%04d-%02d-%02d %02d:%02d", startYear, startMonthOfYear + 1, startDayOfMonth, hour, miniute);
            int count = TimeUtils.measureDayCount3(mLeaveActivity.mBeginText.getText() + ":00", textString + ":00");
            if (count <= 0) {
                AppUtils.showMessage(mLeaveActivity, mLeaveActivity.getString(R.string.message_begin_end));
            } else {
                mLeaveActivity.mEndText.setText(textString);
                //mNewNoticeActivity.mCountText.setText(String.valueOf(count));
            }
        }
    };

    public void startEdit()
    {
        Bus.callData(mLeaveActivity,"riche/startEdit",LeaveManager.ACTION_SET_LEAVE_CONTENT,mLeaveActivity.mReasonText.html);
    }

    public void saveSenderAndCopyer() {
        String senderids = "";
        for (int i = 0; i < mLeaveActivity.mSenders.size(); i++) {
            Contacts mContacts = mLeaveActivity.mSenders.get(i);
            if (i != mLeaveActivity.mSenders.size() - 1) {
                senderids += mContacts.mRecordid + ",";
            } else {
                senderids += mContacts.mRecordid;
            }

        }
        String copyerids = "";
        for (int i = 0; i < mLeaveActivity.mCopyers.size(); i++) {
            Contacts mContacts = mLeaveActivity.mCopyers.get(i);
            if (i != mLeaveActivity.mCopyers.size() - 1) {
                copyerids += mContacts.mRecordid + ",";
            } else {
                copyerids += mContacts.mRecordid;
            }

        }
        SharedPreferences sharedPre = mLeaveActivity.getSharedPreferences(LeaveManager.LEAVEINFO, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putString(LeaveManager.LEAVETSENDER + LeaveManager.getInstance().oaUtils.mAccount.mRecordId
                + LeaveManager.getInstance().oaUtils.service.sAddress + LeaveManager.getInstance().oaUtils.service.sPort, senderids);
        e.putString(LeaveManager.LEAVECOPYER + LeaveManager.getInstance().oaUtils.mAccount.mRecordId
                + LeaveManager.getInstance().oaUtils.service.sAddress + LeaveManager.getInstance().oaUtils.service.sPort, copyerids);
        e.commit();
    }

}
