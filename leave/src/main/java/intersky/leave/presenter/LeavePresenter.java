package intersky.leave.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import intersky.apputils.MenuItem;
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

        ImageView back = mLeaveActivity.findViewById(R.id.back);
        back.setOnClickListener(mBackListener);
        TextView submit = mLeaveActivity.findViewById(R.id.publish);
        submit.setOnClickListener(mLeaveActivity.mSubmitListener);
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
        mLeaveActivity.mImageLayer = (MyLinearLayout) mLeaveActivity.findViewById(R.id.image_content);
        mLeaveActivity.sender = (MyLinearLayout) mLeaveActivity.findViewById(R.id.sender);
        mLeaveActivity.copyer = (MyLinearLayout) mLeaveActivity.findViewById(R.id.copyer);
        if(LeaveManager.getInstance().mLeaveTypes.size() == 0)
        {
            LeaveManager.getInstance().getLeaveType();
        }
        initDetial();
        initAddTextView();
    }

    private void initAddTextView() {
        LayoutInflater mInflater = (LayoutInflater) mLeaveActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = mInflater.inflate(R.layout.fujian_item_add_up, null);
        ImageView mTextView = (ImageView) mView.findViewById(R.id.add_title);
        mTextView.setOnClickListener(mLeaveActivity.mShowAddListener);
        mLeaveActivity.mImageLayer.addView(mView);

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
            Bus.callData(mLeaveActivity,"filetools/addPicView3",attachment,url,mLeaveActivity.mImageLayer,mLeaveActivity.mDeletePicListener);
        }
    }


    public void deleteSender(View v) {
        Contacts send = (Contacts) v.getTag();
        int a = mLeaveActivity.sender.indexOfChild(v);
        if(a > 0)
        {
            View array = mLeaveActivity.sender.getChildAt(a - 1);
            mLeaveActivity.sender.removeView(array);
        }
        mLeaveActivity.sender.removeView(v);
        mLeaveActivity.mSenders.remove(send);
    }

    public void deleteCopyer(View v) {
        Contacts copy = (Contacts) v.getTag();
        mLeaveActivity.copyer.removeView(v);
        mLeaveActivity.mCopyers.remove(copy);
    }

    public void selectSender() {
        Bus.callData(mLeaveActivity,"chat/selectListContact",mLeaveActivity.mSenders,LeaveManager.ACTION_LEAVE_UPATE_SENDER,mLeaveActivity.getString(R.string.select_contact),false);
    }

    public void selectCopyer() {
        Bus.callData(mLeaveActivity,"chat/selectListContact",mLeaveActivity.mCopyers,LeaveManager.ACTION_LEAVE_UPATE_COPYER,mLeaveActivity.getString(R.string.select_contact),false);
    }

    public void initselectView(ArrayList<Contacts> mselectitems, MyLinearLayout mlayer, View.OnClickListener senderListener, View.OnClickListener itemListener, boolean issender) {
        LayoutInflater mInflater = (LayoutInflater) mLeaveActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mlayer.removeAllViews();
        if (mselectitems.size() > 0) {
            for (int i = 0; i < mselectitems.size(); i++) {
                Contacts mContact = mselectitems.get(i);
                View mview = mInflater.inflate(R.layout.sample_contact_item_ex, null);
                TextView mhead = (TextView) mview.findViewById(R.id.contact_img);
                ImageView delete = mview.findViewById(R.id.delete);
                AppUtils.setContactCycleHead(mhead,mContact.getName());
                TextView name = (TextView) mview.findViewById(R.id.title);
                name.setText(mContact.getName());
                mview.setTag(mContact);
                mview.setOnClickListener(itemListener);
                mlayer.addView(mview);
                if(i != mselectitems.size()-1 && issender == true)
                {
                    View mview2 = mInflater.inflate(R.layout.arrayview2, null);
                    mlayer.addView(mview2);
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
        AppUtils.creatDataAndTimePicker(mLeaveActivity,mLeaveActivity.mLeave.end, mLeaveActivity.getString(R.string.keyword_end),mOnEndSetListener);
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

    public void doDismiss()
    {
        if(mLeaveActivity.popupWindow1 != null)
        {
            mLeaveActivity.popupWindow1.dismiss();
        }
    }

    public void addPic() {
        Bus.callData(mLeaveActivity,"filetools/getPhotos",false,LeaveManager.MAX_PIC_COUNT,"intersky.leave.view.activity.LeaveActivity",LeaveManager.ACTION_LEAVE_ADDPICTORE);
        doDismiss();
    }
    

    public void initPicView(ArrayList<Attachment> mPics) {
        for (int i = 0; i < mPics.size(); i++) {
            Bus.callData(mLeaveActivity,"filetools/addPicView3",mPics.get(i),"",mLeaveActivity.mImageLayer,mLeaveActivity.mDeletePicListener);
        }
        if (mLeaveActivity.mPics.size() == LeaveManager.MAX_PIC_COUNT) {
            View mview = mLeaveActivity.mImageLayer.getChildAt(mLeaveActivity.mImageLayer.getChildCount() - 1);
            mLeaveActivity.mImageLayer.removeView(mview);
        }
    }

    public void setpic(Intent intent) {
        ArrayList<Attachment> attachments = intent.getParcelableArrayListExtra("attachments");
        if(attachments.size()+mLeaveActivity.mPics.size() > LeaveManager.MAX_PIC_COUNT)
        {
            AppUtils.showMessage(mLeaveActivity,mLeaveActivity.getString(R.string.keyword_photoaddmax1)
                    + String.valueOf(LeaveManager.MAX_PIC_COUNT)+mLeaveActivity.getString(R.string.keyword_photoaddmax2));
        }
        else
        {
            mLeaveActivity.mPics.addAll(attachments);
            initPicView(attachments);
        }
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
        doDismiss();
    }


    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Actions.TAKE_PHOTO:
                File mFile = new File((String) Bus.callData(mLeaveActivity,"filetools/takePhotoUri",""));
                if (mFile.exists()) {
                    Attachment attachment = new Attachment("",mFile.getName(),mFile.getPath(),"",mFile.length(),mFile.length(),"");
                    mLeaveActivity.mPics.add(attachment);
                    Bus.callData(mLeaveActivity,"filetools/addPicView2",attachment,"",mLeaveActivity.mImageLayer,mLeaveActivity.mDeletePicListener);
                    if (mLeaveActivity.mPics.size() == LeaveManager.MAX_PIC_COUNT) {
                        View mview = mLeaveActivity.mImageLayer.getChildAt(mLeaveActivity.mImageLayer.getChildCount() - 1);
                        mLeaveActivity.mImageLayer.removeView(mview);
                    }
                }
                break;
        }
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

    public void removePic(View view) {
        View v = (View) view.getTag();
        Attachment attachment = (Attachment) v.getTag();
        mLeaveActivity.mImageLayer.removeView(v);
        mLeaveActivity.mPics.remove(attachment);
        LayoutInflater mInflater = (LayoutInflater) mLeaveActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(mLeaveActivity.mPics.size() < LeaveManager.MAX_PIC_COUNT)
        {
            View mView = mInflater.inflate(R.layout.fujian_item_add_up, null);
            ImageView mTextView = (ImageView) mView.findViewById(R.id.add_title);
            mTextView.setOnClickListener(mLeaveActivity.mShowAddListener);
            mLeaveActivity.mImageLayer.addView(mView);
        }
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

    public void showAdd() {

        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        MenuItem menuItem = new MenuItem();
        menuItem.btnName = mLeaveActivity.getString(R.string.button_word_takephoto);
        menuItem.mListener = mLeaveActivity.mTakePhotoListenter;
        items.add(menuItem);
        menuItem = new MenuItem();
        menuItem.btnName = mLeaveActivity.getString(R.string.button_word_album);
        menuItem.mListener = mLeaveActivity.mAddPicListener;
        items.add(menuItem);
        mLeaveActivity.popupWindow1 = AppUtils.creatButtomMenu(mLeaveActivity,mLeaveActivity.mRelativeLayout,items,mLeaveActivity.findViewById(R.id.activity_about));
    }

    public void askFinish()
    {
        AppUtils.creatDialogTowButton(mLeaveActivity, mLeaveActivity.getString(R.string.save_ask),
                mLeaveActivity.getString(R.string.save_ask_title),mLeaveActivity.getString(R.string.button_word_cancle)
                ,mLeaveActivity.getString(R.string.button_word_ok),null,new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mLeaveActivity.finish();
                    }
                });
    }

    public void chekcBack() {
        if (mLeaveActivity.mCountText.getText().toString().length() == 0) {
            askFinish();
            return;
        }
        if(mLeaveActivity.mLeave.lid.length() > 0)
        {
            if (mLeaveActivity.mReasonText.getText().length() == 0) {
                askFinish();
                return;
            }
        }
        else
        {
            if (mLeaveActivity.mReasonText1.getText().toString().length() == 0) {
                askFinish();
                return;
            }
        }
        if (mLeaveActivity.mSenders.size() == 0) {
            askFinish();
            return;
        }
        mLeaveActivity.finish();
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        if (e1.getX() - e2.getX() > mLeaveActivity.mBasePresenter.mScreenDefine.verticalMinDistance*mLeaveActivity.mBasePresenter.mScreenDefine.density && Math.abs(velocityX) > 0)
        {
            return false;
        }
        else if (e2.getX() - e1.getX() > mLeaveActivity.mBasePresenter.mScreenDefine.verticalMinDistance*mLeaveActivity.mBasePresenter.mScreenDefine.density && Math.abs(velocityX) > 0)
        {

            if (e2.getX() - e1.getX() > e2.getY() - e1.getY() && e2.getX() - e1.getX() > e1.getY() - e2.getY() && mLeaveActivity.flagFillBack == true)
            {
                mLeaveActivity.isdestory = true;
                chekcBack();
                return true;
            }
            else
            {
                return false;
            }
        }
        return false;
    }


    public View.OnClickListener mBackListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            chekcBack();
        }
    };
}
