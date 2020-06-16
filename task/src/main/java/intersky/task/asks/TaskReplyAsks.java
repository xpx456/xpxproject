package intersky.task.asks;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import intersky.appbase.entity.Reply;
import intersky.oa.OaUtils;
import intersky.task.TaskManager;
import intersky.task.entity.Task;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;
//11
public class TaskReplyAsks {

    public static final String TASK_REPLYA_PATH =  "api/v1/TaskReply";
    public static final String TASK_REPLYA_LIST =  "get.task.reply.list";
    public static final String TASK_REPLYA_DELETE =  "del.task.reply.item";
    public static final String TASK_REPLYA_ADD =  "add.task.reply.item";

    public static final int GET_REPLAY_SUCCESS = 1251100;
    public static final int DELETE_REPLY_SUCCESS = 1251101;
    public static final int ADD_REPLY_SUCCESS = 1251102;


    public static final String ACTION_TASK_REPLY_UPDATA = "ACTION_TASK_REPLY_UPDATA";

    public static void getReplays(Context mContext, Handler mHandler, Task task, int logPage) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_REPLYA_PATH);

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_REPLYA_LIST);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", task.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("page_no", String.valueOf(logPage));
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("page_size", "40");
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GET_REPLAY_SUCCESS, mContext, nameValuePairs,task);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void deleteReply(Context mContext, Handler mHandler, Reply mReplyModel) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_REPLYA_PATH);

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_REPLYA_DELETE);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", mReplyModel.mReportId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_reply_id", mReplyModel.mReplyId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, DELETE_REPLY_SUCCESS, mContext, nameValuePairs,mReplyModel);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void mSendReply(Context mContext, Handler mHandler,Task task,String content) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_REPLYA_PATH);

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_REPLYA_ADD);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", task.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("reply_content", content);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, ADD_REPLY_SUCCESS, mContext, nameValuePairs,content);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }
}
