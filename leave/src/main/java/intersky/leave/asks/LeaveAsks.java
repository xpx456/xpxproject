package intersky.leave.asks;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import intersky.apputils.StringUtils;
import intersky.leave.LeaveManager;
import intersky.leave.entity.Leave;
import intersky.oa.OaUtils;
import intersky.xpxnet.net.BasicNameValuePair;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;

public class LeaveAsks {

    public static final String LEAVE_ADD_PICTURE = "leave_add_picture";
    public static final String LEAVE_SET_APPROVE = "leave_set_approve";
    public static final String LEAVE_PATH = "api/v1/Leave.html";
    public static final String LEAVE_TYPE_PATH = "api/v1/LeaveType.html";
    public static final String LEAVE_TYPE_LIST_PATH = "get.leavetype.list";
    public static final String LEAVE_ADD_PATH = "add.leave.item";
    public static final String LEAVE_SET_PATH = "set.leave.item";
    public static final String LEAVE_LIST_PATH = "get.leave.list";
    public static final String LEAVE_DETIAL_PATH = "get.leave.item";
    public static final String LEAVE_DELETE_PATH = "del.leave.item";
    public static final String LEAVE_HIT_PATH = "get.leave.total";
    public static final String LEAVE_APPROVE_PATH = "approval.leave.item";

    public static final int EVENT_GET_HIT_SUCCESS = 1190000;
    public static final int EVENT_GET_LIST_SUCCESS = 1190001;
    public static final int EVENT_GET_LEAVETYPE_SUCCESS = 1190002;
    public static final int EVENT_GET_DETIAL_SUCCESS = 1190003;
    public static final int EVENT_GET_DELETE_SUCCESS = 1190004;
    public static final int EVENT_GET_ACCEPT_SUCCESS = 1190005;
    public static final int EVENT_GET_REFUSE_SUCCESS = 1190006;
    public static final int EVENT_GET_SAVE_SUCCESS = 1190007;
    public static final int EVENT_GET_LIST_LIST_SUCCESS = 1190008;


    public static void getLeaveHit(Handler mHandler , Context mContext,String userid) {


        String urlString = OaUtils.mOaUtils.createURLStringoa(LeaveManager.getInstance().oaUtils.service,LEAVE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", LEAVE_HIT_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", LeaveManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", userid);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_HIT_SUCCESS, mContext, items);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }


    public static void getLeave(Handler mHandler , Context mContext,String userid,int ntype,int nowpage) {
        String urlString = OaUtils.mOaUtils.createURLStringoa(LeaveManager.getInstance().oaUtils.service,LEAVE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", LEAVE_LIST_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id",LeaveManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", userid);
        items.add(item);
        item = new BasicNameValuePair("notice_type", String.valueOf(ntype));
        items.add(item);
        item = new BasicNameValuePair("page_no", String.valueOf(nowpage));
        items.add(item);
        item = new BasicNameValuePair("page_size", "40");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_LIST_SUCCESS, mContext, items,ntype);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getLeaveList(Handler mHandler , Context mContext,String userid,int ntype,int nowpage) {
        String urlString = OaUtils.mOaUtils.createURLStringoa(LeaveManager.getInstance().oaUtils.service,LEAVE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", LEAVE_LIST_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id",LeaveManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", userid);
        items.add(item);
        item = new BasicNameValuePair("notice_type", String.valueOf(ntype));
        items.add(item);
        item = new BasicNameValuePair("page_no", String.valueOf(nowpage));
        items.add(item);
        item = new BasicNameValuePair("page_size", "40");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_LIST_LIST_SUCCESS, mContext, items,ntype);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getLeaveTypes(Handler mHandler , Context mContext) {
        String urlString = OaUtils.mOaUtils.createURLStringoa(LeaveManager.getInstance().oaUtils.service,LEAVE_TYPE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", LEAVE_TYPE_LIST_PATH);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_LEAVETYPE_SUCCESS, mContext, items);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getDetial(Handler mHandler , Context mContext, Leave leave) {

        String urlString = OaUtils.mOaUtils.createURLStringoa(LeaveManager.getInstance().oaUtils.service,LEAVE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", LEAVE_DETIAL_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", LeaveManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", LeaveManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("leave_id", leave.lid);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_DETIAL_SUCCESS, mContext, items,leave);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);


    }

    public static void doDelete(Handler mHandler , Context mContext, Leave leave) {

        String urlString = OaUtils.mOaUtils.createURLStringoa(LeaveManager.getInstance().oaUtils.service,LEAVE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", LEAVE_DELETE_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", LeaveManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", LeaveManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("leave_id", leave.lid);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_DELETE_SUCCESS, mContext, items,leave);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void doAccept(Handler mHandler , Context mContext, Leave leave)
    {

        String urlString = OaUtils.mOaUtils.createURLStringoa(LeaveManager.getInstance().oaUtils.service,LEAVE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", LEAVE_APPROVE_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", LeaveManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", LeaveManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("leave_id", leave.lid);
        items.add(item);
        item = new BasicNameValuePair("approval", "2");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_ACCEPT_SUCCESS, mContext, items,leave);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void doRefouse(Handler mHandler , Context mContext, Leave leave)
    {

        String urlString = OaUtils.mOaUtils.createURLStringoa(LeaveManager.getInstance().oaUtils.service,LEAVE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", LEAVE_APPROVE_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", LeaveManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", LeaveManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("leave_id", leave.lid);
        items.add(item);
        item = new BasicNameValuePair("approval", "3");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_REFUSE_SUCCESS, mContext, items,leave);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void saveLeave(Handler mHandler , Context mContext, Leave leave) {
        String urlString = OaUtils.mOaUtils.createURLStringoa(LeaveManager.getInstance().oaUtils.service,LEAVE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item;
        if(leave.lid.length() == 0)
        {
            item = new BasicNameValuePair("method", LEAVE_ADD_PATH);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("method", LEAVE_SET_PATH);
            items.add(item);
            item = new BasicNameValuePair("leave_id", leave.lid);
            items.add(item);
        }
        item = new BasicNameValuePair("image", leave.attachs);
        items.add(item);
        item = new BasicNameValuePair("company_id", LeaveManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", LeaveManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("leave_type_id", leave.leave_type_id);
        items.add(item);
        item = new BasicNameValuePair("begin_time", leave.start);
        items.add(item);
        item = new BasicNameValuePair("end_time", leave.end);
        items.add(item);
        item = new BasicNameValuePair("days", leave.count);
        items.add(item);
        item = new BasicNameValuePair("content", StringUtils.toHtmlSamle(leave.content));
        items.add(item);
        item = new BasicNameValuePair("sender_id", leave.senders);
        items.add(item);
        item = new BasicNameValuePair("copyer_id", leave.copyers);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_SAVE_SUCCESS, mContext, items,leave);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }
}
