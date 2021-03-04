package intersky.workreport.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import intersky.mywidget.WebEdit;
import intersky.oa.OaAsks;
import intersky.workreport.R;
import intersky.workreport.WorkReportManager;
import intersky.workreport.asks.WorkReportAsks;
import intersky.workreport.entity.Report;
import intersky.workreport.handler.ReportDetialHandler;
import intersky.workreport.view.activity.NewReportActivity;
import intersky.workreport.view.activity.ReportDetialActivity;
import intersky.workreport.view.activity.ReportListActivity;

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
            mReportDetialActivity.worktimename1 = (TextView) mReportDetialActivity.findViewById(R.id.worktimestart);
            mReportDetialActivity.worktimename2 = (TextView) mReportDetialActivity.findViewById(R.id.worktimeend);
            mReportDetialActivity.summerText = (WebEdit) mReportDetialActivity.findViewById(R.id.content5text);
            mReportDetialActivity.summerText.setBaseUrl(WorkReportManager.getInstance().oaUtils.service.sAddress+WorkReportManager.getInstance().oaUtils.service.sPort+":");
            mReportDetialActivity.summerText.setEditable(false);
            TextView title = mReportDetialActivity.findViewById(R.id.title);
            title.setText(mReportDetialActivity.report.userName + mReportDetialActivity.getString(R.string.xml_workreport_dweek1));
            mReportDetialActivity.summerText.setEnabled(false);
        } else if (mReportDetialActivity.report.mType == WorkReportManager.TYPE_DAY) {
            mReportDetialActivity.setContentView(R.layout.activity_detial_report);
            mReportDetialActivity.worktimename = (TextView) mReportDetialActivity.findViewById(R.id.worktimename);
            TextView title = mReportDetialActivity.findViewById(R.id.title);
            title.setText(mReportDetialActivity.report.userName + mReportDetialActivity.getString(R.string.xml_workreport_dday1));
        } else if (mReportDetialActivity.report.mType == WorkReportManager.TYPE_MONTH) {
            mReportDetialActivity.setContentView(R.layout.activity_detial_report_month);
            mReportDetialActivity.worktimename = (TextView) mReportDetialActivity.findViewById(R.id.worktimename);
            mReportDetialActivity.summerText = (WebEdit) mReportDetialActivity.findViewById(R.id.content5text);
            mReportDetialActivity.summerText.setBaseUrl(WorkReportManager.getInstance().oaUtils.service.sAddress+WorkReportManager.getInstance().oaUtils.service.sPort+":");
            mReportDetialActivity.summerText.setEditable(false);
            TextView title = mReportDetialActivity.findViewById(R.id.title);
            title.setText(mReportDetialActivity.report.userName + mReportDetialActivity.getString(R.string.xml_workreport_dmonthy1));
            mReportDetialActivity.summerText.setEnabled(false);
        }
        ImageView back = mReportDetialActivity.findViewById(R.id.back);
        back.setOnClickListener(mReportDetialActivity.mBackListener);
        mReportDetialActivity.head = mReportDetialActivity.findViewById(R.id.headimg);
        mReportDetialActivity.headname = mReportDetialActivity.findViewById(R.id.headname);
        mReportDetialActivity.more = mReportDetialActivity.findViewById(R.id.more);
        mReportDetialActivity.more.setOnClickListener(mReportDetialActivity.mMoreListenter);
        mReportDetialActivity.mshada = (RelativeLayout) mReportDetialActivity.findViewById(R.id.shade);
        mReportDetialActivity.answer = (RelativeLayout) mReportDetialActivity.findViewById(R.id.answer);
        mReportDetialActivity.scollayer = (ScrollView) mReportDetialActivity.findViewById(R.id.scrollView1);
        mReportDetialActivity.answerLayers = (LinearLayout) mReportDetialActivity.findViewById(R.id.answeritem);
        mReportDetialActivity.mEditTextContent = (EditText) mReportDetialActivity.findViewById(R.id.et_sendmessage);
        mReportDetialActivity.btnSend = mReportDetialActivity.findViewById(R.id.btn_send);
        mReportDetialActivity.btnSend.setOnClickListener(mReportDetialActivity.sendListener);
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
        mReportDetialActivity.sender = (LinearLayout) mReportDetialActivity.findViewById(R.id.sender);
        mReportDetialActivity.copyer = (LinearLayout) mReportDetialActivity.findViewById(R.id.copyer);
        mReportDetialActivity.sendtxt = mReportDetialActivity.findViewById(R.id.sendtitle);
        mReportDetialActivity.copytxt = mReportDetialActivity.findViewById(R.id.cctitle);
        mReportDetialActivity.mworkSender = mReportDetialActivity.findViewById(R.id.sendayer);
        mReportDetialActivity.mworkCopyer = mReportDetialActivity.findViewById(R.id.copylayer);
        mReportDetialActivity.sendtxt.setVisibility(View.GONE);
        mReportDetialActivity.copytxt.setVisibility(View.GONE);
        mReportDetialActivity.mworkSender.setVisibility(View.GONE);
        mReportDetialActivity.mworkCopyer.setVisibility(View.GONE);
        mReportDetialActivity.mImageLayer = (LinearLayout) mReportDetialActivity.findViewById(R.id.image_content);
        WorkReportAsks.getReportDetial(mReportDetialActivity,mReportDetialHandler,mReportDetialActivity.report);
        if (mReportDetialActivity.report.mUserid.equals(WorkReportManager.getInstance().oaUtils.mAccount.mRecordId))
        {
            mReportDetialActivity.more.setVisibility(View.VISIBLE);
        }
        else
        {
            mReportDetialActivity.more.setVisibility(View.INVISIBLE);
        }


    }

    public void updataContactViews(ArrayList<Contacts> contacts) {

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
            AppUtils.setContactCycleHead(mReportDetialActivity.head,mReportDetialActivity.report.userName);
            mReportDetialActivity.headname.setText(mReportDetialActivity.report.userName);
            if (mReportDetialActivity.report.mType == WorkReportManager.TYPE_WEEK) {
                mReportDetialActivity.worktimename1.setText(mReportDetialActivity.report.mBegainTime);
                mReportDetialActivity.worktimename2.setText(mReportDetialActivity.report.mEndTime);
                mReportDetialActivity.summerText.setText(mReportDetialActivity.report.mSummery);
            } else if (mReportDetialActivity.report.mType == WorkReportManager.TYPE_DAY) {
                mReportDetialActivity.worktimename.setText(mReportDetialActivity.report.mBegainTime);

            } else if (mReportDetialActivity.report.mType == WorkReportManager.TYPE_MONTH) {
                mReportDetialActivity.worktimename.setText(mReportDetialActivity.report.mBegainTime);
                mReportDetialActivity.summerText.setText(mReportDetialActivity.report.mSummery);
            }
            Bus.callData(mReportDetialActivity,"chat/getContacts",mReportDetialActivity.report.mSenders, mReportDetialActivity.mSenders);
            Bus.callData(mReportDetialActivity,"chat/getContacts",mReportDetialActivity.report.mCopyers, mReportDetialActivity.mCopyers);
            if(mReportDetialActivity.report.mNtype != ReportListActivity.TYPE_SEND)
            {
                mReportDetialActivity.sendtxt.setVisibility(View.GONE);
                mReportDetialActivity.copytxt.setVisibility(View.GONE);
                mReportDetialActivity.mworkSender.setVisibility(View.GONE);
                mReportDetialActivity.mworkCopyer.setVisibility(View.GONE);
            }
            else
            {
                mReportDetialActivity.sendtxt.setVisibility(View.VISIBLE);
                mReportDetialActivity.copytxt.setVisibility(View.VISIBLE);
                mReportDetialActivity.mworkSender.setVisibility(View.VISIBLE);
                mReportDetialActivity.mworkCopyer.setVisibility(View.VISIBLE);
                initselectView(mReportDetialActivity.mSenders, mReportDetialActivity.sender);
                initselectView(mReportDetialActivity.mCopyers, mReportDetialActivity.copyer);
            }

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

    public void initselectView(ArrayList<Contacts> mselectitems, LinearLayout mlayer) {

        mlayer.removeAllViews();
        for (int i = 0; i < mselectitems.size(); i++) {
            Contacts mContact = mselectitems.get(i);
            View mView = mReportDetialActivity.getLayoutInflater().inflate(R.layout.task_contact_item, null);
            mView.setTag(mContact);
            mView.setOnClickListener(showHeadListener);
            TextView mhead = (TextView) mView.findViewById(R.id.contact_img);
            TextView title = (TextView) mView.findViewById(R.id.title);
            title.setText(mContact.getName());
            AppUtils.setContactCycleHead(mhead, mContact.getName());
            mlayer.addView(mView);
        }
    }

    public View.OnClickListener showHeadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bus.callData(mReportDetialActivity,"chat/startContactDetial",v.getTag());
        }
    };

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
