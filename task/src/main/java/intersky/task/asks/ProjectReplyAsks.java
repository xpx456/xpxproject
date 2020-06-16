package intersky.task.asks;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import intersky.appbase.entity.Reply;
import intersky.oa.OaUtils;
import intersky.task.TaskManager;
import intersky.task.entity.Project;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;
//03
public class ProjectReplyAsks {

    public static final String PROJECT_REPLYA_PATH =  "api/v1/ProjectReply";
    public static final String PROJECT_REPLYA_LIST =  "get.project.reply.list";
    public static final String PROJECT_REPLYA_DELETE =  "del.project.reply.item";
    public static final String PROJECT_REPLYA_ADD =  "add.project.reply.item";

    public static final int GET_REPLAY_SUCCESS = 1250300;
    public static final int DELETE_REPLY_SUCCESS = 1250301;
    public static final int ADD_REPLY_SUCCESS = 1250302;

    public static final String ACTION_PROJECT_REPLY = "ACTION_PROJECT_REPLY";

    public static void getReplays(Context mContext, Handler mHandler, Project project, int logPage) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_REPLYA_PATH);

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_REPLYA_LIST);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("is_me", "1");
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("page_no", String.valueOf(logPage));
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("page_size", "40");
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GET_REPLAY_SUCCESS, mContext, nameValuePairs,project);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void deleteReply(Context mContext, Handler mHandler, Reply mReplyModel) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_REPLYA_PATH);

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_REPLYA_DELETE);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", mReplyModel.mReportId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_reply_id", mReplyModel.mReplyId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, DELETE_REPLY_SUCCESS, mContext, nameValuePairs,mReplyModel);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void mSendReply(Context mContext, Handler mHandler,Project project,String content) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_REPLYA_PATH);

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_REPLYA_ADD);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("reply_content", content);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, ADD_REPLY_SUCCESS, mContext, nameValuePairs,project);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }
}
