package intersky.workreport.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.lang.ref.WeakReference;

import intersky.appbase.ReplyUtils;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.Reply;
import intersky.oa.OaAsks;
import intersky.workreport.WorkReportManager;
import intersky.workreport.asks.WorkReportAsks;
import intersky.workreport.entity.Report;
import intersky.workreport.prase.WorkReportPrase;
import intersky.workreport.view.activity.ReportDetialActivity;
import intersky.xpxnet.net.NetObject;
//02
public class ReportDetialHandler extends Handler {

    public static final int EVENT_DETIAL_UPDATA = 3270200;
    public static final int EVENT_DETIAL_DELETE = 3270201;
    public ReportDetialActivity theActivity;

    public ReportDetialHandler(ReportDetialActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case WorkReportAsks.EVENT_ADD_REPLY_SUCCESS:
                    theActivity.waitDialog.hide();
                    Reply reply = WorkReportPrase.prasseReply((NetObject) msg.obj,theActivity.mReplys);
                    if(reply != null)
                    {
                        theActivity.mEditTextContent.setText("");
                        ReplyUtils.praseReplyViews(theActivity.mReplys,theActivity,theActivity.answertitle
                                ,theActivity.answerLayers,theActivity.mDeleteReplyListenter,theActivity.mReportDetialPresenter.mReportDetialHandler,reply);
                    }

                    break;
                case WorkReportAsks.EVENT_DELETE_REPORT_SUCCESS:
                    if(WorkReportPrase.praseData((NetObject) msg.obj,theActivity)) {
                        Report report = (Report) ((NetObject) msg.obj).item;
                        WorkReportManager.getInstance().sendReportDelete(report.mRecordid);
                        theActivity.waitDialog.hide();
                        Bus.callData(theActivity,"function/updateOahit");
                        Intent intent = new Intent();
                        intent.putExtra("type", Conversation.CONVERSATION_TYPE_REPORT);
                        intent.putExtra("detialid",report.mRecordid);
                        Bus.callData(theActivity,"conversation/setdetialdelete",intent);
                        theActivity.finish();
                    }
                    break;
                case WorkReportAsks.EVENT_DELETE_REPLY_SUCCESS:
                    theActivity.waitDialog.hide();
                    int pos = WorkReportPrase.praseReplyDelete((NetObject) msg.obj,theActivity.mReplys);
                    ReplyUtils.removeReplyView(pos,theActivity.mReplys,theActivity.answertitle,theActivity.answerLayers,theActivity);
                    break;
                case OaAsks.EVENT_GET_ATTCHMENT_SUCCESS:
                    theActivity.mImageLayer.setVisibility(View.VISIBLE);
                    WorkReportPrase.praseAddtchment((NetObject) msg.obj,theActivity.mPics,theActivity.report);
                    theActivity.mReportDetialPresenter.praseAttahcmentViews();
                    theActivity.waitDialog.hide();
                    break;
                case WorkReportAsks.EVENT_GET_DETIAL_SUCCESS:
                    theActivity.waitDialog.hide();
                    WorkReportPrase.prasseDetial((NetObject) msg.obj,theActivity);
                    WorkReportManager.getInstance().register.conversationFunctions.Read(WorkReportManager.getInstance().register.typeName
                            ,theActivity.report.mRecordid);
                    theActivity.mReportDetialPresenter.prasseDetial();
                    break;
                case EVENT_DETIAL_UPDATA:
                    Intent intent = (Intent) msg.obj;
                    String id = intent.getStringExtra("reportid");
                    if(id.equals(theActivity.report.mRecordid))
                    WorkReportAsks.getReportDetial(theActivity,theActivity.mReportDetialPresenter.mReportDetialHandler,theActivity.report);
                    break;
                case EVENT_DETIAL_DELETE:
                    Intent intent1 = (Intent) msg.obj;
                    String id1 = intent1.getStringExtra("reportid");
                    WorkReportManager.getInstance().upDataHit(theActivity);
                    Bus.callData(theActivity,"function/updateOahit");
                    if(id1.equals(theActivity.report.mRecordid))
                        theActivity.finish();
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
