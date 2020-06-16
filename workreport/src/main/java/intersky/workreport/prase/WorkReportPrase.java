package intersky.workreport.prase;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.Reply;
import intersky.apputils.AppUtils;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.oa.OaUtils;
import intersky.workreport.R;
import intersky.workreport.WorkReportManager;
import intersky.workreport.entity.Report;
import intersky.workreport.view.activity.ReportListActivity;
import intersky.workreport.view.adapter.ReportAdapter;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class WorkReportPrase {

    public static void praseHit(NetObject net)
    {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                return;
            }
            XpxJSONObject XpxJSONObject = new XpxJSONObject(json);
            XpxJSONObject jo= XpxJSONObject.getJSONObject("data");
            int sender_total = jo.getInt("sender_total",0);
            int copyer_total = jo.getInt("copyer_total",0);
            WorkReportManager.getInstance().hit1 = sender_total;
            WorkReportManager.getInstance().hit2 = copyer_total;
            XpxJSONObject jo2 = jo.getJSONObject("sender_unread");
            WorkReportManager.getInstance().day1 = jo2.getInt("day",0);
            WorkReportManager.getInstance().week1 = jo2.getInt("week",0);
            WorkReportManager.getInstance().month1 = jo2.getInt("month",0);
            jo2 = jo.getJSONObject("copyer_unread");
            WorkReportManager.getInstance().day2 = jo2.getInt("day",0);
            WorkReportManager.getInstance().week2 = jo2.getInt("week",0);
            WorkReportManager.getInstance().month2 = jo2.getInt("month",0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseList(NetObject net, int type, int rtype, ReportAdapter reportAdapter, Context context) {
        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return;
            }
            XpxJSONObject object = new XpxJSONObject(json);
            XpxJSONArray ja = object.getJSONArray("data");
            for (int i = 0; i < ja.length() - 1; i++) {
                XpxJSONObject jo = ja.getJSONObject(i);
                XpxJSONObject jo2 = jo.getJSONObject("has_one_notice");
                boolean isread = false;
                if (jo2.getInt("is_read",0) == 1) {
                    isread = true;
                }
                Report mReport = new Report(jo.getString("report_id"), jo.getString("begin_time"), jo.getString("end_time"), jo.getString("create_time")
                        , jo.getString("user_id"), jo2.getString("sender_id"), isread);
                if (type == ReportListActivity.TYPE_SEND) {
                    mReport.isread = true;
                }
                mReport.mNtype = type;
                mReport.mType  = rtype;
                reportAdapter.getmReports().add(mReport);
            }
            if (reportAdapter.totalCount == -1) {
                XpxJSONObject jo = ja.getJSONObject(ja.length() - 1);
                reportAdapter.totalCount = jo.getInt("total_results",0);
            }
            if (ja.length() > 0) {
                reportAdapter.nowpage++;
            }

            if (reportAdapter.totalCount == 0 && reportAdapter.nowpage == 1) {
                AppUtils.showMessage(context, context.getString(R.string.noInfoFind));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean praseList2(NetObject net, int type, int rtype, ReportAdapter reportAdapter, Context context) {
        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return false;
            }
            XpxJSONObject object = new XpxJSONObject(json);
            XpxJSONArray ja = object.getJSONArray("data");
            for (int i = 0; i < ja.length() - 1; i++) {
                XpxJSONObject jo = ja.getJSONObject(i);
                XpxJSONObject jo2 = jo.getJSONObject("has_one_notice");
                boolean isread = false;
                if (jo2.getInt("is_read",0) == 1) {
                    isread = true;
                }
                Report mReport = new Report(jo.getString("report_id"), jo.getString("begin_time"), jo.getString("end_time"), jo.getString("create_time")
                        , jo.getString("user_id"), jo2.getString("sender_id"), isread);
                if (type == ReportListActivity.TYPE_SEND) {
                    mReport.isread = true;
                }
                mReport.mNtype = type;
                mReport.mType  = rtype;
                reportAdapter.getmReports().add(mReport);
            }
            if (reportAdapter.totalCount == -1) {
                XpxJSONObject jo = ja.getJSONObject(ja.length() - 1);
                reportAdapter.totalCount = jo.getInt("total_results",0);
            }
            if (ja.length() > 0) {
                reportAdapter.nowpage++;
            }

            if (reportAdapter.totalCount == 0 && reportAdapter.nowpage == 1) {
                AppUtils.showMessage(context, context.getString(R.string.noInfoFind));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return  false;
        }
        return true;
    }

    public static boolean prasseDetial(NetObject net,Context context) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return false;
            }
            Report report = (Report) net.item;
            if(report.isread == false)
            {
                report.isread = true;
                WorkReportManager.getInstance().sendReportUpdate(report.mRecordid);
                WorkReportManager.getInstance().upDataHit(context);
                Bus.callData(context,"function/updateOahit");
            }
            Bus.callData(context,"conversation/setdetialread", Conversation.CONVERSATION_TYPE_REPORT,report.mRecordid);
            XpxJSONObject object = new XpxJSONObject(json);
            XpxJSONObject jo = object.getJSONObject("data");
            report.mType = jo.getInt("report_type",1);
            report.mUserid = jo.getString("user_id");
            report.mComplete = jo.getString("complete");
            report.nextProject = jo.getString("next_project");
            report.mHelp = jo.getString("coordination");
            report.mRemark = jo.getString("remark");
            if (report.mType == WorkReportManager.TYPE_WEEK ) {
                report.mBegainTime = jo.getString("begin_time").substring(0, 10);
                report.mEndTime = jo.getString("end_time").substring(0, 10);
                report.mSummery = jo.getString("summary");

            }else if (report.mType == WorkReportManager.TYPE_DAY ) {
                report.mBegainTime = jo.getString("begin_time").substring(0, 10);
                report.mSummery = jo.getString("summary");

            }else if(report.mType == WorkReportManager.TYPE_MONTH) {
                report.mBegainTime = jo.getString("begin_time").substring(0, 7);
                report.mSummery = jo.getString("summary");
            }
            report.mCopyers = jo.getString("copyer_id");
            report.mSenders = jo.getString("sender_id");
            report.mAttence = jo.getString("attence");
            if(jo.has("reply"))
            report.replyJson = jo.getJSONArray("reply").toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void praseAddtchment(NetObject net, ArrayList<Attachment> attachments,Report report) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                return;
            }
            report.attachJson = net.result;
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray ja = jsonObject.getJSONArray("data");
            for (int i = 0; i < ja.length(); i++) {
                XpxJSONObject jo = ja.getJSONObject(i);
                Attachment mAttachmentModel = new Attachment(jo.getString("hash"),jo.getString("name"),
                        Bus.callData(WorkReportManager.getInstance().context,"filetools/getfilePath","/" + "intersky" + "/" + "report")+"/"+jo.getString("hash")+jo.getString("ext"),
                        WorkReportManager.getInstance().oaUtils.praseClodAttchmentUrl(jo.getString("hash")),jo.getLong("size",0),0,"");
                mAttachmentModel.mPath2 = Bus.callData(WorkReportManager.getInstance().context,"filetools/getfilePath","/" + "intersky" + "/" + "report")+"/"+jo.getString("hash")+"2"+jo.getString("ext");
                attachments.add(mAttachmentModel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseAddtchment(String json, ArrayList<Attachment> attachments,Report report) {
        try {

            report.attachJson = json;
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject2 = new JSONObject(json);
            JSONArray ja = jsonObject.getJSONArray("data");
            JSONObject jsonObject3 = jsonObject2.getJSONObject("data");
            String hash = jsonObject3.getString("attence");
            String[] hashs = hash.split(",");
            for (int j = 0; j < hashs.length; j++) {
                String temp = hashs[j];
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    if (jo.getString("hash").equals(temp)) {
                        Attachment mAttachmentModel = new Attachment(jo.getString("hash"),jo.getString("name"),
                                Bus.callData(WorkReportManager.getInstance().context,"filetools/getfilePath","/" + "Intersky" + "/" + "Report")+"/"+jo.getString("hash")+jo.getString("ext"),
                                WorkReportManager.getInstance().oaUtils.praseClodAttchmentUrl(jo.getString("hash")),jo.getLong("size"),0,"");
                        mAttachmentModel.mPath2 = Bus.callData(WorkReportManager.getInstance().context,"filetools/getfilePath","/" + "Intersky" + "/" + "Report")+"/"+jo.getString("hash")+"2"+jo.getString("ext");
                        attachments.add(mAttachmentModel);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Reply prasseReply(NetObject net,ArrayList<Reply> replies) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                return null;
            }
            int count = replies.size();
            JSONObject object = new JSONObject(json);
            JSONObject jo2 = object.getJSONObject("data");
            Reply mReplyModel = new Reply(jo2.getString("report_reply_id"), jo2.getString("user_id"), jo2.getString("reply_content"), jo2.getString("report_id"), jo2.getString("create_time"));
            replies.add(0, mReplyModel);
            return mReplyModel;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int praseReplyDelete(NetObject net,ArrayList<Reply> replies) {

        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            return -1;
        }

        Reply mreply = (Reply) net.item;
        int pos = replies.indexOf(mreply);
        replies.remove(mreply);
        return  pos;
    }

    public static boolean praseData(NetObject net, Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        return  true;
    }
}
