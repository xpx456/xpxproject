package intersky.vote.asks;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import intersky.appbase.entity.Reply;
import intersky.apputils.StringUtils;
import intersky.oa.OaUtils;
import intersky.vote.VoteManager;
import intersky.vote.entity.Vote;
import intersky.xpxnet.net.BasicNameValuePair;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;

public class VoteAsks {

    public static final String VOTE_PATH = "api/v1/Vote.html";
    public static final String VOTE_CLOSE_PATH = "close.vote.item";
    public static final String VOTE_ADD_PATH = "add.vote.item";
    public static final String VOTE_ACCEDING_PATH = "acceding.vote.item";
    public static final String VOTE_LIST_PATH = "get.vote.list";
    public static final String VOTE_DETIAL_PATH = "get.vote.item";
    public static final String VOTE_DELETE_PATH = "del.vote.item";
    public static final String VOTE_REPLY_ADD_PATH = "add.vote.reply.item";
    public static final String VOTE_REPLY_DELETE_PATH = "del.vote.reply.item";

    public static final int EVENT_GET_LIST_SUCCESS = 1260001;
    public static final int EVENT_GET_DETIAL_SUCCESS = 1260002;
    public static final int EVENT_GET_DELETE_SUCCESS = 1260003;
    public static final int EVENT_GET_CLOSE_SUCCESS = 1260004;
    public static final int EVENT_VOTE_SUCCESS = 1260009;
    public static final int EVENT_ADD_REPLY_SUCCESS = 1260005;
    public static final int EVENT_DELETE_REPLY_SUCCESS = 1260006;
    public static final int EVENT_GET_SAVE_SUCCESS = 1260007;
    public static final int EVENT_GET_LIST_LIST_SUCCESS = 1260008;
    public static void getList(Context mContext, Handler mHandler,int page) {
        String urlString = OaUtils.getOaUtils().createURLStringoa(VoteManager.getInstance().oaUtils.service,VOTE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", VOTE_LIST_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", VoteManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", VoteManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("page_no", String.valueOf(page));
        items.add(item);
        item = new BasicNameValuePair("page_size", "20");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_LIST_SUCCESS, mContext, items);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void getListList(Context mContext, Handler mHandler,int page) {
        String urlString = OaUtils.getOaUtils().createURLStringoa(VoteManager.getInstance().oaUtils.service,VOTE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", VOTE_LIST_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", VoteManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", VoteManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("page_no", String.valueOf(page));
        items.add(item);
        item = new BasicNameValuePair("page_size", "20");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_LIST_LIST_SUCCESS, mContext, items);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void getDetial(Context mContext, Handler mHandler, Vote vote)
    {
        String urlString = OaUtils.getOaUtils().createURLStringoa(VoteManager.getInstance().oaUtils.service,VOTE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", VOTE_DETIAL_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", VoteManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", VoteManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("vote_id", vote.voteid);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_DETIAL_SUCCESS, mContext, items,vote);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void deleteVote(Context mContext, Handler mHandler, Vote vote) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(VoteManager.getInstance().oaUtils.service,VOTE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", VOTE_DELETE_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", VoteManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", VoteManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("vote_id", vote.voteid);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_CLOSE_SUCCESS, mContext, items,vote);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void closeVote(Context mContext, Handler mHandler, Vote vote) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(VoteManager.getInstance().oaUtils.service,VOTE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", VOTE_CLOSE_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", VoteManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", VoteManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("vote_id", vote.voteid);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_DELETE_SUCCESS, mContext, items,vote);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void doVote(Context mContext, Handler mHandler, Vote vote)
    {

        String urlString = OaUtils.getOaUtils().createURLStringoa(VoteManager.getInstance().oaUtils.service,VOTE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", VOTE_ACCEDING_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", VoteManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", VoteManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("vote_id", vote.voteid);
        items.add(item);
        item = new BasicNameValuePair("vote_item_id", vote.myvoteid);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_VOTE_SUCCESS, mContext, items,vote);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void saveVote(Context mContext, Handler mHandler, Vote vote) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(VoteManager.getInstance().oaUtils.service,VOTE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", VOTE_ADD_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", VoteManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", VoteManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("type", String.valueOf(vote.type));
        items.add(item);
        item = new BasicNameValuePair("is_incognito", String.valueOf(vote.is_incognito));
        items.add(item);
        item = new BasicNameValuePair("content", StringUtils.toHtmlSamle(vote.mContent));
        items.add(item);
        item = new BasicNameValuePair("sender_id", vote.voterid);
        items.add(item);
        item = new BasicNameValuePair("end_time", vote.endTime);
        items.add(item);
        item = new BasicNameValuePair("vote_item", vote.selectJson);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_SAVE_SUCCESS, mContext, items,vote);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void sendReply(Context mContext,Handler mHandler,Vote vote,String text) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(VoteManager.getInstance().oaUtils.service,VOTE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", VOTE_REPLY_ADD_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", VoteManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", VoteManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("vote_id", vote.voteid);
        items.add(item);
        item = new BasicNameValuePair("reply_content", text);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_ADD_REPLY_SUCCESS, mContext, items,vote);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);


    }

    public static void deleteReply(Context mContext, Handler mHandler, Reply reply) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(VoteManager.getInstance().oaUtils.service,VOTE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", VOTE_REPLY_DELETE_PATH);
        items.add(item);
        item = new BasicNameValuePair("company_id", VoteManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", VoteManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("vote_reply_id", reply.mReplyId);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_DELETE_REPLY_SUCCESS, mContext, items,reply);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }


}
