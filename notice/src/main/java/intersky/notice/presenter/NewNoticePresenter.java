package intersky.notice.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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
import intersky.apputils.MenuItem;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.WebEdit;
import intersky.notice.NoticeManager;
import intersky.notice.R;
import intersky.notice.asks.NoticeAsks;
import intersky.notice.handler.NewNoticeHandler;
import intersky.notice.prase.NoticePrase;
import intersky.notice.receicer.NewNoticeReceiver;
import intersky.notice.view.activity.NewNoticeActivity;
import intersky.xpxnet.net.NetObject;
import xpx.com.toolbar.utils.ToolBarHelper;

public class NewNoticePresenter implements Presenter {

    public NewNoticeActivity mNewNoticeActivity;
    public NewNoticeHandler mNewNoticeHandler;

    public NewNoticePresenter(NewNoticeActivity mNewNoticeActivity) {
        this.mNewNoticeActivity = mNewNoticeActivity;
        mNewNoticeHandler = new NewNoticeHandler(mNewNoticeActivity);
        mNewNoticeActivity.setBaseReceiver(new NewNoticeReceiver(mNewNoticeHandler));
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mNewNoticeActivity.notice = mNewNoticeActivity.getIntent().getParcelableExtra("notice");
        if(mNewNoticeActivity.notice.nid.length() > 0)
        {
            mNewNoticeActivity.setContentView(R.layout.activity_notice_new);
        }
        else
        {
            mNewNoticeActivity.setContentView(R.layout.activity_notice_new1);
        }
        ImageView back = mNewNoticeActivity.findViewById(R.id.back);
        back.setOnClickListener(mNewNoticeActivity.mBackListener);
        mNewNoticeActivity.mTitleText = (EditText) mNewNoticeActivity.findViewById(R.id.leavedayname);
        mNewNoticeActivity.publish = mNewNoticeActivity.findViewById(R.id.publish);
        mNewNoticeActivity.publish.setOnClickListener(mNewNoticeActivity.publicListener);
        mNewNoticeActivity.mRelativeLayout = (RelativeLayout) mNewNoticeActivity.findViewById(R.id.shade);
        if(mNewNoticeActivity.notice.nid.length() > 0)
        {
            mNewNoticeActivity.mCountText = (WebEdit) mNewNoticeActivity.findViewById(R.id.content1text);
            mNewNoticeActivity.mCountText.setOnClickListener(mNewNoticeActivity.startEditListener);
            mNewNoticeActivity.mCountText.setHit("#c0c0c0",mNewNoticeActivity.getString(R.string.xml_syscontent_hint));
            mNewNoticeActivity.mCountText.setTxtColor(Color.rgb(164,166,171));
            mNewNoticeActivity.mCountText.setAction(NoticeManager.ACTION_SET_NOTICE_CONTENT);
        }
        else
        {
            mNewNoticeActivity.mCountText1 = (EditText) mNewNoticeActivity.findViewById(R.id.content1text);
        }
        mNewNoticeActivity.mSenderText = (TextView) mNewNoticeActivity.findViewById(R.id.leavetypename);
        mNewNoticeActivity.mSenderLayer = (RelativeLayout) mNewNoticeActivity.findViewById(R.id.noticesender);
        mNewNoticeActivity.mImageLayer = (MyLinearLayout) mNewNoticeActivity.findViewById(R.id.image_content);
        mNewNoticeActivity.mSenderLayer.setOnClickListener(mNewNoticeActivity.senderListener);
        initAddTextView();
        praseDetial();

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

    private void initAddTextView() {
        LayoutInflater mInflater = (LayoutInflater) mNewNoticeActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = mInflater.inflate(R.layout.fujian_item_add_up, null);
        ImageView mTextView = (ImageView) mView.findViewById(R.id.add_title);
        mTextView.setOnClickListener(mNewNoticeActivity.mShowAddListener);
        mNewNoticeActivity.mImageLayer.addView(mView);

    }


    public void dodismiss() {
        if(mNewNoticeActivity.popupWindow1 != null)
        mNewNoticeActivity.popupWindow1.dismiss();
    }

    public void showAdd() {

        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        MenuItem menuItem = new MenuItem();
        menuItem.btnName = mNewNoticeActivity.getString(R.string.button_word_takephoto);
        menuItem.mListener = mNewNoticeActivity.mTakePhotoListenter;
        items.add(menuItem);
        menuItem = new MenuItem();
        menuItem.btnName = mNewNoticeActivity.getString(R.string.button_word_album);
        menuItem.mListener = mNewNoticeActivity.mAddPicListener;
        items.add(menuItem);
        mNewNoticeActivity.popupWindow1 = AppUtils.creatButtomMenu(mNewNoticeActivity,mNewNoticeActivity.mRelativeLayout,items,mNewNoticeActivity.findViewById(R.id.activity_about));
    }


    public void praseDetial() {

        mNewNoticeActivity.mTitleText.setText(mNewNoticeActivity.notice.title);
        if(mNewNoticeActivity.notice.nid.length() > 0)
        {
            mNewNoticeActivity.mCountText.setText(mNewNoticeActivity.notice.content);
        }
        else
        {
            mNewNoticeActivity.mCountText1.setText(mNewNoticeActivity.notice.content);
        }
        mNewNoticeActivity.mSenderText.setText(mNewNoticeActivity.notice.dept_name);
        NoticePrase.praseAddtchment(mNewNoticeActivity.notice.attachJson, mNewNoticeActivity.mPics, mNewNoticeActivity.notice);
        Bus.callData(mNewNoticeActivity,"chat/getContacts",mNewNoticeActivity.notice.sender_id, mNewNoticeActivity.mSenders);
        praseAttahcmentViews();

    }

    public void praseAttahcmentViews() {
        for(int i = 0 ; i < mNewNoticeActivity.mPics.size() ; i++)
        {
            Attachment attachment = mNewNoticeActivity.mPics.get(i);
            String url = "";
            if(attachment.mRecordid.length() > 0)
            {
                url = NoticeManager.getInstance().oaUtils.praseClodAttchmentUrl(attachment.mRecordid, (int) (40 * mNewNoticeActivity.mBasePresenter.mScreenDefine.density));
            }
            Bus.callData(mNewNoticeActivity,"filetools/addPicView3",attachment,url,mNewNoticeActivity.mImageLayer,mNewNoticeActivity.mDeletePicListener);
        }
    }


    public void selectSender() {
        Bus.callData(mNewNoticeActivity,"chat/selectListContact",mNewNoticeActivity.mSenders,NoticeManager.ACTION_NOTICE_UPATE_SENDER,mNewNoticeActivity.getString(R.string.select_contact),false);
    }


    public void setSender(Intent intent) {
        mNewNoticeActivity.mSenders.clear();
        mNewNoticeActivity.mSenderText.setText(intent.getStringExtra("depart"));
        mNewNoticeActivity.mSenders.addAll((ArrayList<Contacts>)Bus.callData(mNewNoticeActivity,"chat/mselectitems",""));

    }

    public void setpic(Intent intent) {
        ArrayList<Attachment> attachments = intent.getParcelableArrayListExtra("attachments");
        if(attachments.size()+mNewNoticeActivity.mPics.size() > NoticeManager.MAX_PIC_COUNT)
        {
            AppUtils.showMessage(mNewNoticeActivity,mNewNoticeActivity.getString(R.string.keyword_photoaddmax1)
                    + String.valueOf(NoticeManager.MAX_PIC_COUNT)+mNewNoticeActivity.getString(R.string.keyword_photoaddmax2));
        }
        else
        {
            mNewNoticeActivity.mPics.addAll(attachments);
        }
        initPicView(attachments);
    }


    public void addPic() {
        Bus.callData(mNewNoticeActivity,"filetools/getPhotos",false,NoticeManager.MAX_PIC_COUNT,"intersky.notice.view.activity.NewNoticeActivity",NoticeManager.ACTION_NOTICE_ADDPICTORE);
        dodismiss();
    }


    public void initPicView(ArrayList<Attachment> mPics) {
        for (int i = 0; i < mPics.size(); i++) {
            Bus.callData(mNewNoticeActivity,"filetools/addPicView3",mPics.get(i),"",mNewNoticeActivity.mImageLayer,mNewNoticeActivity.mDeletePicListener);
        }
        if (mNewNoticeActivity.mPics.size() == NoticeManager.MAX_PIC_COUNT) {
            View mview = mNewNoticeActivity.mImageLayer.getChildAt(mNewNoticeActivity.mImageLayer.getChildCount() - 1);
            mNewNoticeActivity.mImageLayer.removeView(mview);
        }
    }

    public void removePic(View view) {
        View v = (View) view.getTag();
        Attachment attachment = (Attachment) v.getTag();
        mNewNoticeActivity.mImageLayer.removeView(v);
        mNewNoticeActivity.mPics.remove(attachment);
        LayoutInflater mInflater = (LayoutInflater) mNewNoticeActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(mNewNoticeActivity.mPics.size() < NoticeManager.MAX_PIC_COUNT)
        {
            View mView = mInflater.inflate(R.layout.fujian_item_add_up, null);
            ImageView mTextView = (ImageView) mView.findViewById(R.id.add_title);
            mTextView.setOnClickListener(mNewNoticeActivity.mShowAddListener);
            mNewNoticeActivity.mImageLayer.addView(mView);
        }
    }

    public void takePhoto() {
        if(mNewNoticeActivity.mPics.size() < NoticeManager.MAX_PIC_COUNT)
        {
            mNewNoticeActivity.permissionRepuest = (PermissionResult) Bus.callData(mNewNoticeActivity,"filetools/checkPermissionTakePhoto"
                    ,mNewNoticeActivity,Bus.callData(mNewNoticeActivity,"filetools/getfilePath","notice/photo"));
        }
        else
        {
            AppUtils.showMessage(mNewNoticeActivity,mNewNoticeActivity.getString(R.string.keyword_photoaddmax1)
                    + String.valueOf(NoticeManager.MAX_PIC_COUNT)+mNewNoticeActivity.getString(R.string.keyword_photoaddmax2));
        }
        dodismiss();
    }


    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Actions.TAKE_PHOTO:
                File mFile = new File((String) Bus.callData(mNewNoticeActivity,"filetools/takePhotoUri",""));
                if (mFile.exists()) {
                    Attachment attachment = new Attachment("",mFile.getName(),mFile.getPath(),"",mFile.length(),mFile.length(),"");
                    mNewNoticeActivity.mPics.add(attachment);
                    Bus.callData(mNewNoticeActivity,"filetools/addPicView3",attachment,"",mNewNoticeActivity.mImageLayer,mNewNoticeActivity.mDeletePicListener);
                    if (mNewNoticeActivity.mPics.size() == NoticeManager.MAX_PIC_COUNT) {
                        View mview = mNewNoticeActivity.mImageLayer.getChildAt(mNewNoticeActivity.mImageLayer.getChildCount() - 1);
                        mNewNoticeActivity.mImageLayer.removeView(mview);
                    }
                }
                break;
        }
    }

    public void submit() {
        if(mNewNoticeActivity.notice.nid.length() > 0)
        {
            if (mNewNoticeActivity.mCountText.getText().toString().length() == 0) {
                AppUtils.showMessage(mNewNoticeActivity, mNewNoticeActivity.getString(R.string.message_notice_title));
                return;
            }
        }
        else
        {
            if (mNewNoticeActivity.mCountText1.getText().toString().length() == 0) {
                AppUtils.showMessage(mNewNoticeActivity, mNewNoticeActivity.getString(R.string.message_notice_title));
                return;
            }
        }
        if (mNewNoticeActivity.mTitleText.getText().toString().length() == 0) {
            AppUtils.showMessage(mNewNoticeActivity, mNewNoticeActivity.getString(R.string.message_notice_content));
            return;
        }
        if (mNewNoticeActivity.mSenders.size() == 0) {
            AppUtils.showMessage(mNewNoticeActivity, mNewNoticeActivity.getString(R.string.message_notice_sender));
            return;
        }
        if (mNewNoticeActivity.issub == false) {
            mNewNoticeActivity.issub = true;
            mNewNoticeActivity.waitDialog.show();
            NetObject netObject = (NetObject) Bus.callData(mNewNoticeActivity,"filetools/getUploadFiles",mNewNoticeActivity.mPics);
            if(((ArrayList<File>) netObject.item).size() > 0)
            {
                NoticeManager.getInstance().oaUtils.uploadAttchments((ArrayList<File>) netObject.item,mNewNoticeHandler,netObject.result);
            }
            else
            {
                doSave(netObject.result);
            }

        }
        else
        {
            mNewNoticeActivity.waitDialog.show();
        }
    }

    public void doSave(String json) {

        mNewNoticeActivity.notice.attachment = json;
        mNewNoticeActivity.notice.title = mNewNoticeActivity.mTitleText.getText().toString();
        if(mNewNoticeActivity.notice.nid.length() > 0)
        mNewNoticeActivity.notice.content = mNewNoticeActivity.mCountText.getText();
        else
            mNewNoticeActivity.notice.content = mNewNoticeActivity.mCountText1.getText().toString();
        mNewNoticeActivity.notice.dept_name = mNewNoticeActivity.mSenderText.getText().toString();
        mNewNoticeActivity.notice.sender_id = (String) Bus.callData(mNewNoticeActivity,"chat/getMemberIds",mNewNoticeActivity.mSenders);
        NoticeAsks.saveNotice(mNewNoticeActivity,mNewNoticeHandler,mNewNoticeActivity.notice);
    }

    public void startEdit()
    {
        Bus.callData(mNewNoticeActivity,"riche/startEdit",NoticeManager.ACTION_SET_NOTICE_CONTENT,mNewNoticeActivity.mCountText.html);
    }

    public void askFinish()
    {
        AppUtils.creatDialogTowButton(mNewNoticeActivity, mNewNoticeActivity.getString(R.string.save_ask),
                mNewNoticeActivity.getString(R.string.save_ask_title),mNewNoticeActivity.getString(R.string.button_word_cancle)
                ,mNewNoticeActivity.getString(R.string.button_word_ok),null,new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mNewNoticeActivity.finish();
                    }
                });
    }

    public void chekcBack()
    {
        if(mNewNoticeActivity.notice.nid.length() > 0)
        {
            if (mNewNoticeActivity.mCountText.getText().toString().length() > 0) {
                askFinish();
                return;
            }
        }
        else
        {
            if (mNewNoticeActivity.mCountText1.getText().toString().length() > 0) {
                askFinish();
                return;
            }
        }
        if (mNewNoticeActivity.mTitleText.getText().toString().length() > 0) {
            askFinish();
            return;
        }
        if (mNewNoticeActivity.mSenders.size() > 0) {
            askFinish();
            return;
        }
        if(mNewNoticeActivity.mPics.size() > 0)
        {
            askFinish();
            return;
        }
        mNewNoticeActivity.finish();
    }



    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        if (e1.getX() - e2.getX() > mNewNoticeActivity.mBasePresenter.mScreenDefine.verticalMinDistance*mNewNoticeActivity.mBasePresenter.mScreenDefine.density && Math.abs(velocityX) > 0)
        {
            return false;
        }
        else if (e2.getX() - e1.getX() > mNewNoticeActivity.mBasePresenter.mScreenDefine.verticalMinDistance*mNewNoticeActivity.mBasePresenter.mScreenDefine.density && Math.abs(velocityX) > 0)
        {

            if (e2.getX() - e1.getX() > e2.getY() - e1.getY() && e2.getX() - e1.getX() > e1.getY() - e2.getY() && mNewNoticeActivity.flagFillBack == true)
            {
                mNewNoticeActivity.isdestory = true;
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
