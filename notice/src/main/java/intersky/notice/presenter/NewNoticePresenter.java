package intersky.notice.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import intersky.mywidget.WebEdit;
import intersky.notice.NoticeManager;
import intersky.notice.R;
import intersky.notice.asks.NoticeAsks;
import intersky.notice.handler.NewNoticeHandler;
import intersky.notice.prase.NoticePrase;
import intersky.notice.receicer.NewNoticeReceiver;
import intersky.notice.view.activity.NewNoticeActivity;
import intersky.oa.OaUtils;
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
        ToolBarHelper.setTitle(mNewNoticeActivity.mActionBar, mNewNoticeActivity.getString(R.string.keyword_notice_new_title));
        ToolBarHelper.setRightBtnText(mNewNoticeActivity.mActionBar, mNewNoticeActivity.publicListener, mNewNoticeActivity.getString(R.string.keyword_public));
        mNewNoticeActivity.mTitleText = (EditText) mNewNoticeActivity.findViewById(R.id.leavedayname);
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
        mNewNoticeActivity.mImageLayer = (LinearLayout) mNewNoticeActivity.findViewById(R.id.image_content);
        mNewNoticeActivity.takePhoto = (RelativeLayout) mNewNoticeActivity.findViewById(R.id.buttom_takephoto);
        mNewNoticeActivity.getPhoto = (RelativeLayout) mNewNoticeActivity.findViewById(R.id.buttom_picture);
        mNewNoticeActivity.takePhoto.setOnClickListener(mNewNoticeActivity.mTakePhotoListenter);
        mNewNoticeActivity.getPhoto.setOnClickListener(mNewNoticeActivity.mAddPicListener);
        mNewNoticeActivity.mSenderLayer.setOnClickListener(mNewNoticeActivity.senderListener);

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
            Bus.callData(mNewNoticeActivity,"filetools/addPicView",attachment,url,mNewNoticeActivity.mImageLayer,mNewNoticeActivity.mDeletePicListener);
        }
    }


    public void selectSender() {
        Bus.callData(mNewNoticeActivity,"chat/selectListContact",mNewNoticeActivity.mSenders,NoticeManager.ACTION_NOTICE_UPATE_SENDER,"选择联系人",false);
    }


    public void setSender(Intent intent) {
        mNewNoticeActivity.mSenders.clear();
        mNewNoticeActivity.mSenderText.setText(intent.getStringExtra("depart"));
        mNewNoticeActivity.mSenders.addAll((ArrayList<Contacts>)Bus.callData(mNewNoticeActivity,"chat/mselectitems",""));

    }

    public void setpic(Intent intent) {
        ArrayList<Attachment> attachments = intent.getParcelableArrayListExtra("attachments");
        mNewNoticeActivity.mPics.addAll(attachments);
        initPicView(attachments);
    }


    public void addPic() {
        Bus.callData(mNewNoticeActivity,"filetools/getPhotos",false,NoticeManager.MAX_PIC_COUNT,"intersky.notice.view.activity.NewNoticeActivity",NoticeManager.ACTION_NOTICE_ADDPICTORE);
    }


    public void initPicView(ArrayList<Attachment> mPics) {
        for (int i = 0; i < mPics.size(); i++) {
            Bus.callData(mNewNoticeActivity,"filetools/addPicViewListener",mPics.get(i),"",mNewNoticeActivity.mImageLayer,mNewNoticeActivity.mDeletePicListener);
        }

    }

    public void deletePic(Attachment attachment) {
        AppUtils.creatDialogTowButton(mNewNoticeActivity,mNewNoticeActivity.getString(R.string.cicle_delete_attachment),"",mNewNoticeActivity.getString(R.string.button_word_cancle),
                mNewNoticeActivity.getString(R.string.button_word_ok),null, (DialogInterface.OnClickListener) Bus.callData(mNewNoticeActivity,"filetools/DeletePicListener",attachment,mNewNoticeActivity.mPics,mNewNoticeActivity.mImageLayer));
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

    }


    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Actions.TAKE_PHOTO:
                File mFile = new File((String) Bus.callData(mNewNoticeActivity,"filetools/takePhotoUri",""));
                if (mFile.exists()) {
                    Attachment attachment = new Attachment("",mFile.getName(),"","",mFile.length(),mFile.length(),"");
                    mNewNoticeActivity.mPics.add(attachment);
                    Bus.callData(mNewNoticeActivity,"filetools/addPicViewListener",attachment,mNewNoticeActivity,"",mNewNoticeActivity.mImageLayer,mNewNoticeActivity.mDeletePicListener);
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

}
