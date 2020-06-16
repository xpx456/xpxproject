package intersky.notice.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.appbase.ReplyUtils;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Reply;
import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.mywidget.WebEdit;
import intersky.notice.NoticeManager;
import intersky.notice.R;
import intersky.notice.asks.NoticeAsks;
import intersky.notice.entity.Notice;
import intersky.notice.handler.NoticeDetialHandler;
import intersky.notice.receicer.NoticeDetialReceiver;
import intersky.notice.view.activity.NewNoticeActivity;
import intersky.notice.view.activity.NoticeDetialActivity;
import intersky.notice.view.activity.NoticeReadlActivity;
import intersky.oa.OaAsks;
import intersky.oa.OaUtils;
import xpx.com.toolbar.utils.ToolBarHelper;


public class NoticeDetialPresenter implements Presenter {

    public NoticeDetialActivity mNoticeDetialActivity;
    public NoticeDetialHandler mNoticeDetialHandler;

    public NoticeDetialPresenter(NoticeDetialActivity mNoticeDetialActivity) {
        this.mNoticeDetialActivity = mNoticeDetialActivity;
        mNoticeDetialHandler = new NoticeDetialHandler(mNoticeDetialActivity);
        mNoticeDetialActivity.setBaseReceiver(new NoticeDetialReceiver(mNoticeDetialHandler));
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

        mNoticeDetialActivity.setContentView(R.layout.activity_notice_detial);
        ToolBarHelper.setTitle(mNoticeDetialActivity.mActionBar, mNoticeDetialActivity.getString(R.string.keyword_systemdetial));
        mNoticeDetialActivity.notice = mNoticeDetialActivity.getIntent().getParcelableExtra("notice");
        mNoticeDetialActivity.mNoticePersonLayer = (RelativeLayout) mNoticeDetialActivity.findViewById(R.id.readperson);
        mNoticeDetialActivity.mAnswerlayer = (LinearLayout) mNoticeDetialActivity.findViewById(R.id.answeritem);
        mNoticeDetialActivity.mAttachmentlayer = (LinearLayout) mNoticeDetialActivity.findViewById(R.id.attchmentlayer);
        mNoticeDetialActivity.mEditTextContent = (EditText) mNoticeDetialActivity.findViewById(R.id.et_sendmessage);
        mNoticeDetialActivity.mEditTextContent.setOnEditorActionListener(mNoticeDetialActivity.sendtext);
        mNoticeDetialActivity.mRelativeLayout = (RelativeLayout) mNoticeDetialActivity.findViewById(R.id.shade);
        mNoticeDetialActivity.mTitle = (TextView) mNoticeDetialActivity.findViewById(R.id.conversation_title);
        mNoticeDetialActivity.mContent = (WebEdit) mNoticeDetialActivity.findViewById(R.id.conversation_content);
        mNoticeDetialActivity.mContent.setEditable(false);
        mNoticeDetialActivity.mSubject = (TextView) mNoticeDetialActivity.findViewById(R.id.conversation_dete);
        mNoticeDetialActivity.mRead = (TextView) mNoticeDetialActivity.findViewById(R.id.readpersontitle);
        mNoticeDetialActivity.mAnswer = (TextView) mNoticeDetialActivity.findViewById(R.id.answertitle);
        mNoticeDetialActivity.recordid = mNoticeDetialActivity.getIntent().getStringExtra("id");
        mNoticeDetialActivity.mNoticePersonLayer.setOnClickListener(mNoticeDetialActivity.mShowReadListener);
        mNoticeDetialActivity.mEditTextContent = (EditText) mNoticeDetialActivity.findViewById(R.id.et_sendmessage);
        mNoticeDetialActivity.mEditTextContent.setOnEditorActionListener(mNoticeDetialActivity.sendtext);
        NoticeAsks.getDetial(mNoticeDetialActivity,mNoticeDetialHandler,mNoticeDetialActivity.notice);

    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
        IntentFilter filter = new IntentFilter();
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

    public void Destroy() {
        // TODO Auto-generated method stub
        mNoticeDetialActivity.mContent.destroy();
    }


    public void showRead() {
        Intent intent = new Intent(mNoticeDetialActivity, NoticeReadlActivity.class);
        intent.putExtra("json", mNoticeDetialActivity.json);
        intent.putExtra("notice", mNoticeDetialActivity.notice);
        mNoticeDetialActivity.startActivity(intent);
    }

    public void dodismiss() {
        mNoticeDetialActivity.popupWindow1.dismiss();
    }

    public void showEdit() {

        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        MenuItem mMenuItem = new MenuItem();
        mMenuItem.btnName = mNoticeDetialActivity.getString(R.string.leave_deit_pop);
        mMenuItem.mListener = mNoticeDetialActivity.mEditListenter;
        mMenuItem.item = mNoticeDetialActivity.notice;
        items.add(mMenuItem);
        mMenuItem = new MenuItem();
        mMenuItem.btnName = mNoticeDetialActivity.getString(R.string.button_delete);
        mMenuItem.mListener = mNoticeDetialActivity.mDeleteListenter;
        mMenuItem.item = mNoticeDetialActivity.notice;
        items.add(mMenuItem);
        mNoticeDetialActivity.popupWindow1 = AppUtils.creatButtomMenu(mNoticeDetialActivity,mNoticeDetialActivity.mRelativeLayout,items,mNoticeDetialActivity.findViewById(R.id.activity_about));
    }

    private void deleteData() {

        AppUtils.creatDialogTowButton(mNoticeDetialActivity,mNoticeDetialActivity.getString(R.string.xml_workreport_notice_delete),"",mNoticeDetialActivity.getString(R.string.button_word_cancle),
                mNoticeDetialActivity.getString(R.string.button_word_ok),null,new DeleteNoticeDialogListener(mNoticeDetialActivity.notice));
    }

    public class DeleteNoticeDialogListener implements DialogInterface.OnClickListener {

        public Notice notice;
        public DeleteNoticeDialogListener(Notice notice) {
            this.notice = notice;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            NoticeAsks.doDelete(mNoticeDetialActivity,mNoticeDetialHandler,notice);
        }
    }

    public void editdata() {
        Intent intent = new Intent(mNoticeDetialActivity, NewNoticeActivity.class);
        intent.putExtra("notice", mNoticeDetialActivity.notice);
        mNoticeDetialActivity.startActivity(intent);
    }

    public void onEdit() {
        editdata();
        mNoticeDetialActivity.popupWindow1.dismiss();
    }

    public void onDelete() {
        deleteData();
        mNoticeDetialActivity.popupWindow1.dismiss();
    }


    public void senderReply() {
        if (mNoticeDetialActivity.mEditTextContent.getText().length() == 0) {
            AppUtils.showMessage(mNoticeDetialActivity, mNoticeDetialActivity.getString(R.string.xml_reply_delete));
            return;
        }
        mNoticeDetialActivity.waitDialog.show();
        NoticeAsks.sendReply(mNoticeDetialActivity,mNoticeDetialHandler,mNoticeDetialActivity.notice,mNoticeDetialActivity.mEditTextContent.getText().toString());
    }

    public ReplyUtils.DoDelete doDelete = new ReplyUtils.DoDelete(){
        @Override
        public void doDeltet(Reply reply) {
            deleteReply(reply);
        }
    };

    public void deleteReply(Reply mReplyModel) {
        mNoticeDetialActivity.waitDialog.show();
        NoticeAsks.deleteReply(mNoticeDetialActivity,mNoticeDetialHandler,mReplyModel);
    }

    public void showDeleteReply(Reply mReplyModel) {
        AppUtils.creatDialogTowButton(mNoticeDetialActivity,mNoticeDetialActivity.getString(R.string.xml_reply_delete),"",
                mNoticeDetialActivity.getString(R.string.button_word_cancle),mNoticeDetialActivity.getString(R.string.button_word_ok),null,new ReplyUtils.DeleteReplyDialogListener(mReplyModel,doDelete));
    }

    public void praseDetial() {
        try {
            mNoticeDetialActivity.mPics.clear();
            mNoticeDetialActivity.mAttachmentlayer.removeAllViews();
            OaAsks.getAttachments(mNoticeDetialActivity.notice.attachment,mNoticeDetialHandler,mNoticeDetialActivity);
            mNoticeDetialActivity.mSubject.setText(mNoticeDetialActivity.notice.username + " "
                    + mNoticeDetialActivity.notice.create_time);
            mNoticeDetialActivity.mTitle.setText(mNoticeDetialActivity.notice.title);
            mNoticeDetialActivity.mContent.setText(mNoticeDetialActivity.notice.content);
            if (NoticeManager.getInstance().oaUtils.mAccount.mRecordId.equals(mNoticeDetialActivity.notice.uid))
                ToolBarHelper.setRightBtnText(mNoticeDetialActivity.mActionBar, mNoticeDetialActivity.mMoreListenter, " · · ·", true);
            mNoticeDetialActivity.mUnReaders.clear();
            mNoticeDetialActivity.mReaders.clear();
            Bus.callData(mNoticeDetialActivity,"chat/getContacts",mNoticeDetialActivity.notice.unread_id,mNoticeDetialActivity.mUnReaders);
            Bus.callData(mNoticeDetialActivity,"chat/getContacts",mNoticeDetialActivity.notice.read_id,mNoticeDetialActivity.mReaders);
            mNoticeDetialActivity.mRead.setText(String.valueOf(mNoticeDetialActivity.mReaders.size()) + mNoticeDetialActivity.getString(R.string.xml_notice_read_person) +
                    " " + String.valueOf(mNoticeDetialActivity.mUnReaders.size()) + mNoticeDetialActivity.getString(R.string.xml_notice_unread_person));
            mNoticeDetialActivity.mReplys.clear();
            mNoticeDetialActivity.mAnswerlayer.removeAllViews();
            if (mNoticeDetialActivity.notice.replyJson.length() > 0) {
                XpxJSONArray ja2 = new XpxJSONArray(mNoticeDetialActivity.notice.replyJson);
                mNoticeDetialActivity.mReplys.clear();
                mNoticeDetialActivity.mAnswerlayer.removeAllViews();
                for (int i = 0; i < ja2.length(); i++) {
                    XpxJSONObject jo2 = ja2.getJSONObject(i);
                    Reply mReplyModel = new Reply(jo2.getString("notice_reply_id"), jo2.getString("user_id"), jo2.getString("reply_content"), mNoticeDetialActivity.notice.nid, jo2.getString("create_time"));
                    mNoticeDetialActivity.mReplys.add(mReplyModel);
                }

            }
            ReplyUtils.praseReplyViews(mNoticeDetialActivity.mReplys,mNoticeDetialActivity,mNoticeDetialActivity.mAnswer
                    ,mNoticeDetialActivity.mAnswerlayer,mNoticeDetialActivity.mDeleteReplyListenter,mNoticeDetialHandler);
            if(mNoticeDetialActivity.notice.isread == 0)
            {
                mNoticeDetialActivity.notice.isread = 1;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void praseAttahcmentViews() {
        for(int i = 0 ; i < mNoticeDetialActivity.mPics.size() ; i++)
        {
            Attachment attachment = mNoticeDetialActivity.mPics.get(i);
            String url = "";
            if(attachment.mRecordid.length() > 0)
            {
                url = NoticeManager.getInstance().oaUtils.praseClodAttchmentUrl(attachment.mRecordid, (int) (40 * mNoticeDetialActivity.mBasePresenter.mScreenDefine.density));
            }
            Bus.callData(mNoticeDetialActivity,"filetools/addPicView",attachment,url,mNoticeDetialActivity.mAnswerlayer);
        }
    }



}
