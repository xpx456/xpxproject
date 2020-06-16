package intersky.workreport.asks;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import intersky.appbase.entity.Reply;
import intersky.apputils.StringUtils;
import intersky.oa.OaUtils;
import intersky.workreport.WorkReportManager;
import intersky.workreport.entity.Report;
import intersky.xpxnet.net.BasicNameValuePair;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetListTask;
import intersky.xpxnet.net.nettask.PostNetTask;

public class WorkReportAsks {

    public static final String WORK_REPORT_PATH = "api/v1/Report.html";
    public static final String WORK_REPORT_LIST = "get.report.list";
    public static final String WORK_REPORT_ADD = "add.report.item";
    public static final String WORK_REPORT_DETIAL = "get.report.item";
    public static final String WORK_REPORT_SET = "set.report.item";
    public static final String WORK_REPORT_DELETE = "del.report.item";
    public static final String WORK_REPORT_REPLY_ADD = "add.report.reply.item";
    public static final String WORK_REPORT_REPLY_DELETE = "del.report.reply.item";
    public static final String WORK_REPORT_GET_UNREAD_COUNT = "get.report.unread.count";
    public static final int EVENT_GET_HIT_SUCCESS = 1270000;
    public static final int EVENT_GET_LIST_SUCCESS = 1270001;
    public static final int EVENT_GET_DETIAL_SUCCESS = 1270002;
    public static final int EVENT_SAVE_SUCCESS = 1270003;
    public static final int EVENT_ADD_REPLY_SUCCESS = 1270004;
    public static final int EVENT_DELETE_REPLY_SUCCESS = 1270005;
    public static final int EVENT_DELETE_REPORT_SUCCESS = 1270006;
    public static final int EVENT_GET_LIST_LIST_SUCCESS = 1270007;
    public static final int EVENT_LIST_READ = 1270008;
    public static final int EVENT_LIST_DELETE = 1270009;
    public static void gethit(Context mContext,Handler mHandler) {
        String urlString = OaUtils.getOaUtils().createURLStringoa(WorkReportManager.getInstance().oaUtils.service,WORK_REPORT_PATH);

        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", WORK_REPORT_GET_UNREAD_COUNT);
        items.add(item);
        item = new BasicNameValuePair("company_id", WorkReportManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", WorkReportManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_HIT_SUCCESS, mContext, items);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);


    }

    public static void getReport(Context mContext,Handler mHandler,int reporttype,int noticetype,int page)
    {
        String urlString = OaUtils.getOaUtils().createURLStringoa(WorkReportManager.getInstance().oaUtils.service,WORK_REPORT_PATH);

        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", WORK_REPORT_LIST);
        items.add(item);
        item = new BasicNameValuePair("company_id", WorkReportManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", WorkReportManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("report_type", String.valueOf(reporttype));
        items.add(item);
        item = new BasicNameValuePair("notice_type", String.valueOf(noticetype));
        items.add(item);
        item = new BasicNameValuePair("page_no", String.valueOf(page));
        items.add(item);
        item = new BasicNameValuePair("page_size", "40");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_LIST_SUCCESS, mContext, items,reporttype);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getReportList(Context mContext,Handler mHandler,int reporttype,int noticetype,int page)
    {
        String urlString = OaUtils.getOaUtils().createURLStringoa(WorkReportManager.getInstance().oaUtils.service,WORK_REPORT_PATH);

        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", WORK_REPORT_LIST);
        items.add(item);
        item = new BasicNameValuePair("company_id", WorkReportManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", WorkReportManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("report_type", String.valueOf(reporttype));
        items.add(item);
        item = new BasicNameValuePair("notice_type", String.valueOf(noticetype));
        items.add(item);
        item = new BasicNameValuePair("page_no", String.valueOf(page));
        items.add(item);
        item = new BasicNameValuePair("page_size", "40");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_LIST_LIST_SUCCESS, mContext, items,reporttype);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void saveReport(Context mContext,Handler mHandler,Report report) {
        String urlString = OaUtils.getOaUtils().createURLStringoa(WorkReportManager.getInstance().oaUtils.service,WORK_REPORT_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("company_id", WorkReportManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", WorkReportManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        if(report.mRecordid.length() != 0)
        {
            item = new BasicNameValuePair("method", String.valueOf(WORK_REPORT_SET));
            items.add(item);
            item = new BasicNameValuePair("report_id", report.mRecordid);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("method", String.valueOf(WORK_REPORT_ADD));
            items.add(item);
        }
        item = new BasicNameValuePair("report_type", String.valueOf(report.mNtype));
        items.add(item);
        item = new BasicNameValuePair("complete", StringUtils.toHtmlSamle(report.mComplete));
        items.add(item);
        if(report.mType == 1)
        {
            item = new BasicNameValuePair("summary", StringUtils.toHtmlSamle(report.mSummery));
            items.add(item);
        }
        item = new BasicNameValuePair("next_project", StringUtils.toHtmlSamle(report.nextProject));
        items.add(item);
        item = new BasicNameValuePair("coordination", StringUtils.toHtmlSamle(report.mHelp));
        items.add(item);
        item = new BasicNameValuePair("remark", StringUtils.toHtmlSamle(report.mRemark));
        items.add(item);
        item = new BasicNameValuePair("begin_time", report.mBegainTime);
        items.add(item);
        item = new BasicNameValuePair("end_time", report.mEndTime);
        items.add(item);
        item = new BasicNameValuePair("attence", report.mAttence);
        items.add(item);
        item = new BasicNameValuePair("sender_id", report.mSenders);
        items.add(item);
        item = new BasicNameValuePair("copyer_id", report.mCopyers);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_SAVE_SUCCESS, mContext, items,report);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getReportDetial(Context mContext,Handler mHandler,Report report) {
        String urlString = OaUtils.getOaUtils().createURLStringoa(WorkReportManager.getInstance().oaUtils.service,WORK_REPORT_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", WORK_REPORT_DETIAL);
        items.add(item);
        item = new BasicNameValuePair("company_id", WorkReportManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", WorkReportManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("report_id", report.mRecordid);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_DETIAL_SUCCESS, mContext, items,report);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void deleteReport(Context mContext,Handler mHandler,Report report) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(WorkReportManager.getInstance().oaUtils.service,WORK_REPORT_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", WORK_REPORT_DELETE);
        items.add(item);
        item = new BasicNameValuePair("company_id", WorkReportManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", WorkReportManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("report_id", report.mRecordid);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_DELETE_REPORT_SUCCESS, mContext, items,report);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void sendReply(Context mContext,Handler mHandler,Report report,String text) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(WorkReportManager.getInstance().oaUtils.service,WORK_REPORT_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", WORK_REPORT_REPLY_ADD);
        items.add(item);
        item = new BasicNameValuePair("company_id", WorkReportManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", WorkReportManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("report_id", report.mRecordid);
        items.add(item);
        item = new BasicNameValuePair("reply_content", text);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_ADD_REPLY_SUCCESS, mContext, items,report);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void deleteReply(Context mContext, Handler mHandler, Reply reply) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(WorkReportManager.getInstance().oaUtils.service,WORK_REPORT_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", WORK_REPORT_REPLY_DELETE);
        items.add(item);
        item = new BasicNameValuePair("company_id", WorkReportManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", WorkReportManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("report_reply_id", reply.mReplyId);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_DELETE_REPLY_SUCCESS, mContext, items,reply);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static boolean setReadAll(Context mContext,Handler mHandler,ArrayList<Report> reports) {
        String urlString = OaUtils.getOaUtils().createURLStringoa(WorkReportManager.getInstance().oaUtils.service,WORK_REPORT_PATH);
        ArrayList<String> urls = new ArrayList<String>();
        ArrayList<Object> objects = new ArrayList<Object>();
        ArrayList<ArrayList<NameValuePair>> datas = new ArrayList<ArrayList<NameValuePair>>();
        for(int i = 0 ; i < reports.size() ; i++)
        {
            if(reports.get(i).isread == false)
            {
                ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
                BasicNameValuePair item = new BasicNameValuePair("method", WORK_REPORT_DETIAL);
                items.add(item);
                item = new BasicNameValuePair("company_id", WorkReportManager.getInstance().oaUtils.mAccount.mCompanyId);
                items.add(item);
                item = new BasicNameValuePair("user_id", WorkReportManager.getInstance().oaUtils.mAccount.mRecordId);
                items.add(item);
                item = new BasicNameValuePair("report_id", reports.get(i).mRecordid);
                items.add(item);
                datas.add(items);
                urls.add(urlString);
                objects.add(reports.get(i));
            }
        }
        if(urls.size() > 0)
        {
            PostNetListTask mPostNetTask = new PostNetListTask(urls, mHandler, EVENT_LIST_READ, mContext, datas,objects);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
            return true;
        }
        else
        {
            return false;
        }
    }

    public static void setDeleteAll(Context mContext,Handler mHandler,ArrayList<Report> reports) {
        String urlString = OaUtils.getOaUtils().createURLStringoa(WorkReportManager.getInstance().oaUtils.service,WORK_REPORT_PATH);
        ArrayList<String> urls = new ArrayList<String>();
        ArrayList<Object> objects = new ArrayList<Object>();
        ArrayList<ArrayList<NameValuePair>> datas = new ArrayList<ArrayList<NameValuePair>>();
        for(int i = 0 ; i < reports.size() ; i++)
        {
            ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
            BasicNameValuePair item = new BasicNameValuePair("method", WORK_REPORT_DELETE);
            items.add(item);
            item = new BasicNameValuePair("company_id", WorkReportManager.getInstance().oaUtils.mAccount.mCompanyId);
            items.add(item);
            item = new BasicNameValuePair("user_id", WorkReportManager.getInstance().oaUtils.mAccount.mRecordId);
            items.add(item);
            item = new BasicNameValuePair("report_id", reports.get(i).mRecordid);
            items.add(item);
            datas.add(items);
            urls.add(urlString);
            objects.add(reports.get(i));
        }
        PostNetListTask mPostNetTask = new PostNetListTask(urls, mHandler, EVENT_LIST_DELETE, mContext, datas,objects);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }
}
