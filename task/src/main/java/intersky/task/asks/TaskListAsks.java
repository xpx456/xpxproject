package intersky.task.asks;

import android.content.Context;
import android.os.Handler;
import android.widget.EditText;

import java.util.ArrayList;

import intersky.oa.OaUtils;
import intersky.task.TaskManager;
import intersky.task.entity.Task;
import intersky.task.entity.TaskList;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;
//09
public class TaskListAsks {


    public static final String TASK_LIST_PATH = "api/v1/TaskList";
    public static final String TASK_LIST_ADD = "add.task.list.item";
    public static final String TASK_LIST_ITEM_ADD = "add.task.list.itemname";
    public static final String TASK_LIST_LIST = "get.task.list.list";
    public static final String TASK_LIST_NAME = "rename.task.list.item";
    public static final String TASK_LIST_ITEM_NAME = "rename.task.list.itemname";
    public static final String TASK_LIST_DELETE = "del.task.list.item";
    public static final String TASK_LIST_ITEM_DELETE = "del.task.list.itemname";
    public static final String TASK_LIST_ITEM_FINISH = "mark.task.list.itemname";
    public static final String TASK_LIST_SOFT = "set.task.list.itemsort";

    public static final int TASK_LIST_SUCCESS = 1250900;
    public static final int TASK_LIST_ADD_SUCCESS = 1250901;
    public static final int LIST_DELETE_SUCCESS = 1250902;
    public static final int LIST_ITEM_DELETE_SUCCESS = 1250903;
    public static final int CHANGE_LIST_SUCCESS = 1250904;
    public static final int CHANGE_LIST_ITEM_NAME_SUCCESS = 1250905;
    public static final int CHANGE_LIST_NAME_SUCCESS = 1250906;
    public static final int LIST_ITEM_ADD_SUCCESS = 1250907;
    public static final int LIST_ITEM_FINISH_SUCCESS = 1250908;
    public static final int LIST_ITEM_UNFINISH_SUCCESS = 1250909;

    public static final String ACTION_TASK_LIST_UPDATA = "ACTION_TASK_LIST_UPDATA";

    public static void getList(Context mContext, Handler mHandler, Task task) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_LIST_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_LIST_LIST);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", task.taskId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, TASK_LIST_SUCCESS, mContext, nameValuePairs,task);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void addList(Context mContext, Handler mHandler, Task task,String name) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_LIST_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_LIST_ADD);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", task.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("name", name);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, TASK_LIST_ADD_SUCCESS, mContext, nameValuePairs,task);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void deleteList(Context mContext, Handler mHandler, Task task,TaskList list) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_LIST_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_LIST_DELETE);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", task.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_list_id", list.mId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, LIST_DELETE_SUCCESS, mContext, nameValuePairs,list);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }


    public static void deleteListItem(Context mContext, Handler mHandler, Task task,TaskList list) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_LIST_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_LIST_ITEM_DELETE);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", task.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_list_id", list.mId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_list_item_id", list.mListItemid);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, LIST_ITEM_DELETE_SUCCESS, mContext, nameValuePairs,list);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void changeList(Context mContext, Handler mHandler,String data) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_LIST_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_LIST_SOFT);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("list_data", data);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, CHANGE_LIST_SUCCESS, mContext, nameValuePairs);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void changeListItemName(Context mContext, Handler mHandler,TaskList tasklist,String name) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_LIST_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_LIST_ITEM_NAME);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("item_name", name);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", tasklist.mTaskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_list_id", tasklist.mId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_list_item_id", tasklist.mListItemid);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, CHANGE_LIST_ITEM_NAME_SUCCESS, mContext, nameValuePairs);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void changeListName(Context mContext, Handler mHandler,TaskList tasklist,String name) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_LIST_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_LIST_NAME);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("item_name", name);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", tasklist.mTaskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_list_id", tasklist.mId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, CHANGE_LIST_NAME_SUCCESS, mContext, nameValuePairs);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void addListItem(Context mContext, Handler mHandler, TaskList tasklist, String name, EditText editText) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_LIST_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_LIST_ITEM_ADD);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("item_name", name);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", tasklist.mTaskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_list_id", tasklist.mId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, LIST_ITEM_ADD_SUCCESS, mContext, nameValuePairs,editText);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void setListItemFinish(Context mContext, Handler mHandler,TaskList mListItem,int finish) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_LIST_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_LIST_ITEM_FINISH);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_list_item_id", mListItem.mListItemid);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", mListItem.mTaskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_list_id", mListItem.mId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("is_complete", String.valueOf(finish));
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask;
        if(finish == 1)
        {
            mPostNetTask = new PostNetTask(urlString, mHandler, LIST_ITEM_FINISH_SUCCESS, mContext, nameValuePairs);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        }
        else
        {
            mPostNetTask = new PostNetTask(urlString, mHandler, LIST_ITEM_UNFINISH_SUCCESS, mContext, nameValuePairs);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        }
    }
}
