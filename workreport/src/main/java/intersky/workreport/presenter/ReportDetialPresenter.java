package intersky.workreport.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.appbase.ReplyUtils;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Reply;
import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.WebEdit;
import intersky.oa.OaAsks;
import intersky.oa.OaUtils;
import intersky.workreport.R;
import intersky.workreport.WorkReportManager;
import intersky.workreport.asks.WorkReportAsks;
import intersky.workreport.entity.Report;
import intersky.workreport.handler.ReportDetialHandler;
import intersky.workreport.view.activity.NewReportActivity;
import intersky.workreport.view.activity.ReportDetialActivity;
import intersky.xpxnet.net.NetUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

public class ReportDetialPresenter implements Presenter {

    public ReportDetialActivity mReportDetialActivity;
    public ReportDetialHandler mReportDetialHandler;

    public ReportDetialPresenter(ReportDetialActivity mReportDetialActivity) {
        this.mReportDetialActivity = mReportDetialActivity;
        this.mReportDetialHandler = new ReportDetialHandler(mReportDetialActivity);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mReportDetialActivity.report = mReportDetialActivity.getIntent().getParcelableExtra("report");
        mReportDetialActivity.report.userName = (String) Bus.callData(mReportDetialActivity,"chat/getContactName",mReportDetialActivity.report.mUserid);
        if (mReportDetialActivity.report.mType == WorkReportManager.TYPE_WEEK) {
            mReportDetialActivity.setContentView(R.layout.activity_detial_report_week);
            mReportDetialActivity.mworktime = (RelativeLayout) mReportDetialActivity.findViewById(R.id.worktime);
            mReportDetialActivity.worktimename1 = (TextView) mReportDetialActivity.findViewById(R.id.worktimestart);
            mReportDetialActivity.worktimename2 = (TextView) mReportDetialActivity.findViewById(R.id.worktimeend);
            mReportDetialActivity.summerText = (WebEdit) mReportDetialActivity.findViewById(R.id.content5text);
            mReportDetialActivity.summerText.setBaseUrl(WorkReportManager.getInstance().oaUtils.service.sAddress+WorkReportManager.getInstance().oaUtils.service.sPort+":");
            mReportDetialActivity.summerText.setEditable(false);
            ToolBarHelper.setTitle(mReportDetialActivity.mActionBar, mReportDetialActivity.report.userName + mReportDetialActivity.getString(R.string.xml_workreport_dweek1));
            mReportDetialActivity.summerText.setEnabled(false);
        } else if (mReportDetialActivity.report.mType == WorkReportManager.TYPE_DAY) {
            mReportDetialActivity.setContentView(R.layout.activity_detial_report);
            mReportDetialActivity.mworktime = (RelativeLayout) mReportDetialActivity.findViewById(R.id.worktime);
            mReportDetialActivity.worktimename = (TextView) mReportDetialActivity.findViewById(R.id.worktimename);
            ToolBarHelper.setTitle(mReportDetialActivity.mActionBar, mReportDetialActivity.report.userName + mReportDetialActivity.getString(R.string.xml_workreport_dday1));
        } else if (mReportDetialActivity.report.mType == WorkReportManager.TYPE_MONTH) {
            mReportDetialActivity.setContentView(R.layout.activity_detial_report_month);
            mReportDetialActivity.mworktime = (RelativeLayout) mReportDetialActivity.findViewById(R.id.worktime);
            mReportDetialActivity.worktimename = (TextView) mReportDetialActivity.findViewById(R.id.worktimename);
            mReportDetialActivity.summerText = (WebEdit) mReportDetialActivity.findViewById(R.id.content5text);
            mReportDetialActivity.summerText.setBaseUrl(WorkReportManager.getInstance().oaUtils.service.sAddress+WorkReportManager.getInstance().oaUtils.service.sPort+":");
            mReportDetialActivity.summerText.setEditable(false);
            ToolBarHelper.setTitle(mReportDetialActivity.mActionBar, mReportDetialActivity.report.userName + mReportDetialActivity.getString(R.string.xml_workreport_dmonthy1));
            mReportDetialActivity.summerText.setEnabled(false);
        }
        mReportDetialActivity.mshada = (RelativeLayout) mReportDetialActivity.findViewById(R.id.shade);
        mReportDetialActivity.answer = (RelativeLayout) mReportDetialActivity.findViewById(R.id.answer);
        mReportDetialActivity.scollayer = (ScrollView) mReportDetialActivity.findViewById(R.id.scrollView1);
        mReportDetialActivity.answerLayers = (LinearLayout) mReportDetialActivity.findViewById(R.id.answeritem);
        mReportDetialActivity.mEditTextContent = (EditText) mReportDetialActivity.findViewById(R.id.et_sendmessage);
        mReportDetialActivity.mEditTextContent.setOnEditorActionListener(mReportDetialActivity.sendtext);
        mReportDetialActivity.answertitle = (TextView) mReportDetialActivity.findViewById(R.id.answertitle);
        mReportDetialActivity.mNowWork = (WebEdit) mReportDetialActivity.findViewById(R.id.content1text);
        mReportDetialActivity.mNowWork.setEditable(false);
        mReportDetialActivity.mNextWork = (WebEdit) mReportDetialActivity.findViewById(R.id.content2text);
        mReportDetialActivity.mNextWork.setEditable(false);
//        mReportDetialActivity.mNextWork.setOnTouchListener(mReportDetialActivity);
        mReportDetialActivity.mWorkHelp = (WebEdit) mReportDetialActivity.findViewById(R.id.content3text);
        mReportDetialActivity.mWorkHelp.setEditable(false);
        mReportDetialActivity.mRemark = (WebEdit) mReportDetialActivity.findViewById(R.id.content4text);
        mReportDetialActivity.mRemark.setEditable(false);
        mReportDetialActivity.mworkSender = (RelativeLayout) mReportDetialActivity.findViewById(R.id.sendayer);
        mReportDetialActivity.mworkCopyer = (RelativeLayout) mReportDetialActivity.findViewById(R.id.ccayer);
        mReportDetialActivity.sender = (MyLinearLayout) mReportDetialActivity.findViewById(R.id.sender);
        mReportDetialActivity.copyer = (MyLinearLayout) mReportDetialActivity.findViewById(R.id.copyer);
        mReportDetialActivity.mImageLayer = (LinearLayout) mReportDetialActivity.findViewById(R.id.image_content);
        WorkReportAsks.getReportDetial(mReportDetialActivity,mReportDetialHandler,mReportDetialActivity.report);
//        ConversationManager.getInstance().sendDetialConversationRead(Conversation.CONVERSATION_TYPE_REPORT,mReportDetialActivity.report.mRecordid);
        if (mReportDetialActivity.report.mUserid.equals(WorkReportManager.getInstance().oaUtils.mAccount.mRecordId))
            ToolBarHelper.setRightBtnText(mReportDetialActivity.mActionBar, mReportDetialActivity.mMoreListenter, " · · ·", true);


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
        mReportDetialHandler= null;
        mReportDetialActivity.mRemark.destroy();
        mReportDetialActivity.mNowWork.destroy();
        mReportDetialActivity.mNextWork.destroy();
        mReportDetialActivity.mWorkHelp.destroy();
        if(mReportDetialActivity.summerText != null)
        {
            mReportDetialActivity.summerText.destroy();
        }
    }

    public void prasseDetial() {
        try {
            mReportDetialActivity.mNowWork.setText(mReportDetialActivity.report.mComplete);
            mReportDetialActivity.mNextWork.setText(mReportDetialActivity.report.nextProject);
            mReportDetialActivity.mWorkHelp.setText(mReportDetialActivity.report.mHelp);
            mReportDetialActivity.mRemark.setText(mReportDetialActivity.report.mRemark);
            if (mReportDetialActivity.report.mType == WorkReportManager.TYPE_WEEK) {
                mReportDetialActivity.worktimename1.setText(mReportDetialActivity.report.mBegainTime);
                mReportDetialActivity.worktimename2.setText(mReportDetialActivity.report.mEndTime);
                mReportDetialActivity.summerText.setText(mReportDetialActivity.report.mSummery);
            } else if (mReportDetialActivity.report.mType == WorkReportManager.TYPE_DAY) {
                mReportDetialActivity.worktimename.setText(mReportDetialActivity.report.mBegainTime);

            } else if (mReportDetialActivity.report.mType == WorkReportManager.TYPE_MONTH) {
                mReportDetialActivity.worktimename.setText(mReportDetialActivity.report.mBegainTime);
                mReportDetialActivity.summerText.setText(mReportDetialActivity.report.mBegainTime);
            }
            Bus.callData(mReportDetialActivity,"chat/getContacts",mReportDetialActivity.report.mSenders, mReportDetialActivity.mSenders);
            Bus.callData(mReportDetialActivity,"chat/getContacts",mReportDetialActivity.report.mCopyers, mReportDetialActivity.mCopyers);
            initselectView(mReportDetialActivity.mSenders, mReportDetialActivity.sender);
            initselectView(mReportDetialActivity.mCopyers, mReportDetialActivity.copyer);
            mReportDetialActivity.mImageLayer.removeAllViews();
            OaAsks.getAttachments(mReportDetialActivity.report.mAttence,mReportDetialHandler,mReportDetialActivity);
            if(mReportDetialActivity.report.replyJson.length() > 0)
            {
                XpxJSONArray ja2 = new XpxJSONArray(mReportDetialActivity.report.replyJson);
                mReportDetialActivity.mReplys.clear();
                mReportDetialActivity.answerLayers.removeAllViews();
                for (int i = 0; i < ja2.length(); i++) {
                    XpxJSONObject jo2 = ja2.getJSONObject(i);
                    Reply mReplyModel = new Reply(jo2.getString("report_reply_id"), jo2.getString("user_id"), jo2.getString("reply_content"), mReportDetialActivity.report.mRecordid, jo2.getString("create_time"));
                    mReportDetialActivity.mReplys.add(mReplyModel);
                }
            }
            ReplyUtils.praseReplyViews(mReportDetialActivity.mReplys,mReportDetialActivity,mReportDetialActivity.answertitle
                    ,mReportDetialActivity.answerLayers,mReportDetialActivity.mDeleteReplyListenter,mReportDetialHandler);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void praseAttahcmentViews() {
        for(int i = 0 ; i < mReportDetialActivity.mPics.size() ; i++)
        {
            Attachment attachment = mReportDetialActivity.mPics.get(i);
            String url = "";
            if(attachment.mRecordid.length() > 0)
            {
                url = WorkReportManager.getInstance().oaUtils.praseClodAttchmentUrl(attachment.mRecordid, (int) (40 * mReportDetialActivity.mBasePresenter.mScreenDefine.density));
            }
            Bus.callData(mReportDetialActivity,"filetools/addPicView",attachment,url,mReportDetialActivity.mImageLayer);
        }
    }

    public void initselectView(ArrayList<Contacts> mselectitems, MyLinearLayout mlayer) {
        LayoutInflater mInflater = (LayoutInflater) mReportDetialActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mlayer.removeAllViews();
        if (mselectitems.size() > 0) {
            for (int i = 0; i < mselectitems.size(); i++) {
                Contacts mContact = mselectitems.get(i);
                View mview = mInflater.inflate(R.layout.sample_contact_item_ex, null);
                TextView mhead = (TextView) mview.findViewById(R.id.contact_img);
                Bus.callData(mReportDetialActivity,"chat/setContactCycleHead",mhead,mContact);
                TextView name = (TextView) mview.findViewById(R.id.title);
                name.setText(mContact.mName);
                mview.setTag(mContact);
                mlayer.addView(mview);
            }

        }
    }

    public void showEdit() {

        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        MenuItem mMenuItem = new MenuItem();
        if (mReportDetialActivity.report.mType == WorkReportManager.TYPE_DAY)
            mMenuItem.btnName = mReportDetialActivity.getString(R.string.report_deit_day);
        else if (mReportDetialActivity.report.mType == WorkReportManager.TYPE_WEEK)
            mMenuItem.btnName = mReportDetialActivity.getString(R.string.report_deit_week);
        else if (mReportDetialActivity.report.mType == WorkReportManager.TYPE_MONTH)
            mMenuItem.btnName = mReportDetialActivity.getString(R.string.report_deit_month);
        mMenuItem.mListener = mReportDetialActivity.mEditListenter;
        mMenuItem.item = mReportDetialActivity.report;
        items.add(mMenuItem);
        mMenuItem = new MenuItem();
        mMenuItem.btnName = mReportDetialActivity.getString(R.string.button_delete);
        mMenuItem.mListener = mReportDetialActivity.mDeleteListenter;
        mMenuItem.item = mReportDetialActivity.report;
        items.add(mMenuItem);
        mReportDetialActivity.popupWindow1 = AppUtils.creatButtomMenu(mReportDetialActivity,mReportDetialActivity.mshada,items,mReportDetialActivity.findViewById(R.id.activity_about));
    }

    private void deleteData(Report report) {
        String message = "";
        if (report.mType == WorkReportManager.TYPE_DAY) {
            message = mReportDetialActivity.getString(R.string.xml_workreport_delete_day);
        } else if (report.mType == WorkReportManager.TYPE_WEEK) {
            message = mReportDetialActivity.getString(R.string.xml_workreport_delete_week);

        } else if (report.mType == WorkReportManager.TYPE_MONTH) {
            message = mReportDetialActivity.getString(R.string.xml_workreport_delete_month);

        }
        AppUtils.creatDialogTowButton(mReportDetialActivity,message,"",mReportDetialActivity.getString(R.string.button_word_cancle),
                mReportDetialActivity.getString(R.string.button_word_ok),null,new DeleteReportDialogListener(report));
    }

    public class DeleteReportDialogListener implements DialogInterface.OnClickListener {

        public Report report;
        public DeleteReportDialogListener(Report report) {
            this.report = report;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            WorkReportAsks.deleteReport(mReportDetialActivity,mReportDetialHandler,report);
        }
    }

    public void editdata() {
        Intent intent = new Intent(mReportDetialActivity, NewReportActivity.class);
        intent.putExtra("report", mReportDetialActivity.report);
        mReportDetialActivity.startActivity(intent);
    }

    public void onEdit() {
        editdata();
        mReportDetialActivity.popupWindow1.dismiss();
    }

    public void onDelete(Report report) {
        deleteData(report);
        mReportDetialActivity.popupWindow1.dismiss();
    }

    public void senderReply() {
        if (mReportDetialActivity.mEditTextContent.getText().length() == 0) {
            AppUtils.showMessage(mReportDetialActivity, mReportDetialActivity.getString(R.string.reply_mesage_send_content));
            return;
        }
        mReportDetialActivity.waitDialog.show();
        WorkReportAsks.sendReply(mReportDetialActivity,mReportDetialHandler,mReportDetialActivity.report,mReportDetialActivity.mEditTextContent.getText().toString());
    }

    public void showDeleteReply(Reply mReplyModel) {
        AppUtils.creatDialogTowButton(mReportDetialActivity,mReportDetialActivity.getString(R.string.xml_reply_delete),"",
                mReportDetialActivity.getString(R.string.button_word_cancle),mReportDetialActivity.getString(R.string.button_word_ok),null,new ReplyUtils.DeleteReplyDialogListener(mReplyModel,doDelete));
    }

    public ReplyUtils.DoDelete doDelete = new ReplyUtils.DoDelete(){
        @Override
        public void doDeltet(Reply reply) {
            deleteReply(reply);
        }
    };

    public void deleteReply(Reply mReplyModel) {
        mReportDetialActivity.waitDialog.show();
        WorkReportAsks.deleteReply(mReportDetialActivity,mReportDetialHandler,mReplyModel);
    }
}
