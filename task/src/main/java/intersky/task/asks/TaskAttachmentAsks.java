package intersky.task.asks;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import intersky.appbase.entity.Attachment;
import intersky.oa.OaUtils;
import intersky.task.TaskManager;
import intersky.task.entity.Task;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;
//08
public class TaskAttachmentAsks {

    public static final String TASK_ATTACHMENT_PATH =  "api/v1/TaskAttence";
    public static final String TASK_ATTACHMENT_LIST =  "get.task.attence.list";
    public static final String TASK_ATTACHMENT_ADD =  "add.task.attence.list";
    public static final String TASK_ATTACHMENT_DEL =  "del.task.attence.list";

    public static final int GET_ATTACHMENT_SUCCESS = 1250800;
    public static final int ADD_ATTACHMENT_SUCCESS = 1250801;
    public static final int DEL_ATTACHMENT_SUCCESS = 1250802;
    public static void getAttachments(Context mContext, Handler mHandler, Task task) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_ATTACHMENT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_ATTACHMENT_LIST);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", task.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("type", "task");
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("page_size", "40");
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GET_ATTACHMENT_SUCCESS, mContext, nameValuePairs,task);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void sentTaskAttachment(Context mContext, Handler mHandler, Task mTask,String names,String hashs,String type)
    {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_ATTACHMENT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_ATTACHMENT_ADD);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", mTask.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("type", type);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("name", names);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("hash", hashs);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, ADD_ATTACHMENT_SUCCESS, mContext, nameValuePairs,mTask);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void delAttachments(Context mContext, Handler mHandler, Task mTask,Attachment model) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_ATTACHMENT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_ATTACHMENT_DEL);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", mTask.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_attence_id", model.taskattid);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, DEL_ATTACHMENT_SUCCESS, mContext, nameValuePairs,model);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

}
