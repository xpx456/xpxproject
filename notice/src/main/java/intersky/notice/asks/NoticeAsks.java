package intersky.notice.asks;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import intersky.appbase.entity.Reply;
import intersky.apputils.StringUtils;
import intersky.notice.NoticeManager;
import intersky.notice.entity.Notice;
import intersky.oa.OaUtils;
import intersky.xpxnet.net.BasicNameValuePair;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;

public class NoticeAsks {

    public static final String NOTICE_PATH = "api/v1/Notice.html";
    public static final String NOTICE_ADD_PATH = "add.notice.item";
    public static final String NOTICE_SET_PATH = "set.notice.item";
    public static final String NOTICE_LIST_PATH = "get.notice.list";
    public static final String NOTICE_DETIAL_PATH = "get.notice.item";
    public static final String NOTICE_DELETE_PATH = "del.notice.item";
    public static final String NOTICE_REPLY_ADD_PATH = "add.notice.reply.item";
    public static final String NOTICE_REPLY_DELETE_PATH = "del.notice.reply.item";

    public static final int EVENT_GET_LIST_SUCCESS = 1210000;
    public static final int EVENT_GET_DETIAL_SUCCESS = 1210001;
    public static final int EVENT_GET_DELETE_SUCCESS = 1210002;
    public static final int EVENT_GET_SAVE_SUCCESS = 1210005;
    public static final int EVENT_ADD_REPLY_SUCCESS = 1210003;
    public static final int EVENT_DELETE_REPLY_SUCCESS = 1210004;
    public static final int EVENT_GET_LIST_LIST_SUCCESS = 1210005;
    public static void getNoticesList(Context mContext, Handler mHandler,int isread, int page) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(NoticeManager.getInstance().oaUtils.service,NOTICE_PATH);

        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", NOTICE_LIST_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", NoticeManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", NoticeManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("is_read", String.valueOf(isread));
        items.add(item);
        item = new BasicNameValuePair("page_no", String.valueOf(page));
        items.add(item);
        item = new BasicNameValuePair("page_size", "40");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_LIST_SUCCESS, mContext, items,isread);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getNoticesListList(Context mContext, Handler mHandler,int isread, int page) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(NoticeManager.getInstance().oaUtils.service,NOTICE_PATH);

        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", NOTICE_LIST_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", NoticeManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", NoticeManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("is_read", String.valueOf(isread));
        items.add(item);
        item = new BasicNameValuePair("page_no", String.valueOf(page));
        items.add(item);
        item = new BasicNameValuePair("page_size", "40");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_LIST_LIST_SUCCESS, mContext, items,isread);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getDetial(Context mContext, Handler mHandler,Notice notice) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(NoticeManager.getInstance().oaUtils.service,NOTICE_PATH);

        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", NOTICE_DETIAL_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", NoticeManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", NoticeManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("notice_id", notice.nid);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_DETIAL_SUCCESS, mContext, items,notice);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void doDelete(Context mContext, Handler mHandler,Notice notice) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(NoticeManager.getInstance().oaUtils.service,NOTICE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", NOTICE_DELETE_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", NoticeManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", NoticeManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("notice_id", notice.nid);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_DELETE_SUCCESS, mContext, items,notice);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void sendReply(Context mContext,Handler mHandler,Notice notice,String text) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(NoticeManager.getInstance().oaUtils.service,NOTICE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", NOTICE_REPLY_ADD_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", NoticeManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", NoticeManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("notice_id", notice.nid);
        items.add(item);
        item = new BasicNameValuePair("reply_content", text);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_ADD_REPLY_SUCCESS, mContext, items,notice);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void deleteReply(Context mContext, Handler mHandler, Reply reply) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(NoticeManager.getInstance().oaUtils.service,NOTICE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", NOTICE_REPLY_DELETE_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", NoticeManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", NoticeManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("notice_reply_id", reply.mReplyId);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_DELETE_REPLY_SUCCESS, mContext, items,reply);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void saveNotice(Context mContext,Handler mHandler,Notice notice) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(NoticeManager.getInstance().oaUtils.service,NOTICE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item;
        if(notice.nid.length() > 0)
        {
            item = new BasicNameValuePair("method", NOTICE_SET_PATH);
            items.add(item);
            item = new BasicNameValuePair("notice_id", notice.nid);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("method", NOTICE_ADD_PATH);
            items.add(item);
        }
        item = new BasicNameValuePair("company_id", NoticeManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", NoticeManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("title", notice.title);
        items.add(item);
        item = new BasicNameValuePair("content", StringUtils.toHtmlSamle(notice.content));
        items.add(item);
        item = new BasicNameValuePair("dept_name", notice.dept_name);
        items.add(item);
        item = new BasicNameValuePair("attence", notice.attachment);
        items.add(item);
        item = new BasicNameValuePair("sender_id", notice.sender_id);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_SAVE_SUCCESS, mContext, items,notice);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }
}
