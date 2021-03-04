package intersky.task.asks;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import intersky.appbase.entity.Contacts;
import intersky.apputils.TimeUtils;
import intersky.oa.OaUtils;
import intersky.select.entity.Select;
import intersky.task.TaskManager;
import intersky.task.entity.Contral;
import intersky.task.entity.Project;
import intersky.task.entity.Stage;
import intersky.task.entity.Task;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;
//07
public class TaskAsks {

    public static final String TASK_PATH = "api/v1/Task";
    public static final String TASK_COUNT = "get.task.left.count";
    public static final String TASK_LIST= "get.task.list";
    public static final String TASK_SON_LIST= "get.task.son.list";
    public static final String TASK_SET_FINISH= "set.task.complete";
    public static final String TASK_SET_TAG= "set.task.tag";
    public static final String TASK_SET_STARE= "set.task.star";
    public static final String TASK_SET_LOCK= "locked.task.item";
    public static final String TASK_DELETE= "del.task.item";
    public static final String TASK_ITEM_ADD = "add.task.item";
    public static final String TASK_SET_PROJECT = "set.task.project";
    public static final String TASK_SET_TASK_NAME = "set.task.name";
    public static final String TASK_SET_TASK_TIME = "set.task.time";
    public static final String TASK_SET_TASK_LEADER = "set.task.notice";
    public static final String TASK_REMOVE_MEMBER= "del.task.notice";
    public static final String TASK_ADD_MEMBER= "add.task.notice";
    public static final String TASK_SET_TASK_DES = "set.task.desc";
    public static final String TASK_CANCLE_PARENT = "cancel.task.parent";
    public static final String TASK_TASK_GET = "get.task.item";
    public static final String TASK_PARENT_TASK_GET = "get.task.parent.list";
    public static final String TASK_SET_STAGE = "set.task.stages";
    public static final String TASK_PARENT_LIST = "get.parent.list";
    public static final String TASK_SET_PARENT = "set.task.parent";
    public static final String TASK_GET_CONTRAL = "get.task.project";
    public static final String TASK_SET_CONTRAL = "set.task.project.value";

    public static final String ACTION_TASK_NAME_SUCCESS = "ACTION_NAME_SUCCESS";
    public static final String ACTION_TASK_DES_SUCCESS = "ACTION_DES_SUCCESS";
    public static final String ACTION_TASK_TIME = "ACTION_TASK_TIME";
    public static final String ACTION_TASK_TAG = "ACTION_TASK_TAG";
    public static final String ACTION_TASK_LOCK = "ACTION_TASK_LOCK";
    public static final String ACTION_TASK_STARE = "ACTION_TASK_STARE";
    public static final String ACTION_DELETE_TASK = "ACTION_DELETE_TASK";
    public static final String ACTION_EXIT_TASK = "ACTION_EXIT_TASK";
    public static final String ACTION_TASK_ADD = "ACTION_EXIT_TASK";
    public static final String ACTION_TASK_FINSH = "ACTION_TASK_FINSH";
    public static final String ACTION_TASK_LEADER = "ACTION_TASK_LEADER";
    public static final String ACTION_TASK_ADD_MEMBER = "ACTION_TASK_ADD_MEMBER";
    public static final String ACTION_TASK_CHANGE_STAGE = "ACTION_TASK_CHANGE_STAGE";
    public static final String ACTION_TASK_CHANGE_PROJECT = "ACTION_TASK_CHANGE_PROJECT";
    public static final String ACTION_TASK_PARENT = "ACTION_TASK_PARENT";
    public static final String ACTION_TASK_ATTACHMENT = "ACTION_TASK_ATTACHMENT";
    public static final String ACTION_TASK_CONTRAL = "ACTION_TASK_CONTRAL";

    public static final int TASK_HIT_SUCCESS = 1250700;
    public static final int TASK_LIST_SUCCESS = 1250701;
    public static final int TASK_DETIAL_SUCCESS = 1250702;
    public static final int GET_PARENT_SUCCESS = 1250703;
    public static final int GET_SON_SUCCESS = 1250704;
    public static final int GET_CONTRAL_SUCCESS = 1250705;
    public static final int SET_DES_SUCCESS = 1250706;
    public static final int SET_NAME_SUCCESS = 1250707;
    public static final int SET_BEGIN_TIME_SUCCESS = 1250708;
    public static final int SET_END_TIME_SUCCESS = 1250709;
    public static final int SET_TAG_SUCCESS = 1250710;
    public static final int SET_IS_LOCK = 1250711;
    public static final int SET_IS_STARE = 1250712;
    public static final int DELETE_TASK_SUCCESS = 1250713;
    public static final int EXIT_TASK_SUCCESS = 1250714;
    public static final int TASK_ADD_SUCCESS = 1250715;
    public static final int TASK_FINISH_SET_FINISH_SUCCESS = 1250716;
    public static final int TASK_FINISH_SET_FINISH_ALL_SUCCESS = 1250717;
    public static final int TASK_FINISH_SET_UNFINISH_SUCCESS = 1250718;
    public static final int TASK_FINISH_SET_UNFINISH_ALL_SUCCESS = 1250719;
    public static final int SET_LEADER_SUCCESS = 1250720;
    public static final int SET_CONTRAL_SUCCESS = 1250721;
    public static final int CHANGE_STAGE_SUCCESS = 1250722;
    public static final int GET_TASK_SETPARENT_SUCCESS = 1250723;
    public static final int ADD_TASK_MEMBER_SUCCESS = 1250724;
    public static final int SET_TASK_PROJECT_SUCCESS = 1250725;
    public static final int SET_TASK_PARENT_SUCCESS = 1250726;
    public static final int DELETE_TASK_BYID_SUCCESS = 1250714;
    public static final int TASK_CANCLE_PARENT_SUCCESS = 1250727;

    public static void getTaskHint(Context mContext, Handler mHandler) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_COUNT);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, TASK_HIT_SUCCESS, mContext, nameValuePairs);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void addTask(Context mContext, Handler mHandler,Task task) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_ITEM_ADD);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("name", task.taskName);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("leader_id", task.leaderId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("description", task.des);
        nameValuePairs.add(mNameValuePair);
        if(task.parentId.length() > 0) {
            mNameValuePair = new NameValuePair("parent_id", task.parentId);
            nameValuePairs.add(mNameValuePair);
        }
        else
        {
            mNameValuePair = new NameValuePair("parent_id", "0");
            nameValuePairs.add(mNameValuePair);
        }
        if(task.projectId.length() > 0) {
            mNameValuePair = new NameValuePair("project_id", task.projectId);
            nameValuePairs.add(mNameValuePair);
            mNameValuePair = new NameValuePair("project_stages_id", task.stageId);
            nameValuePairs.add(mNameValuePair);
        }
        else
        {
            mNameValuePair = new NameValuePair("project_id", "0");
            nameValuePairs.add(mNameValuePair);
            mNameValuePair = new NameValuePair("project_stages_id", "0");
            nameValuePairs.add(mNameValuePair);
        }
        if(task.beginTime.length() > 0) {
            mNameValuePair = new NameValuePair("begin_time", task.beginTime);
            nameValuePairs.add(mNameValuePair);
        }
        if(task.endTime.length() > 0) {
            mNameValuePair = new NameValuePair("end_time", task.endTime);
            nameValuePairs.add(mNameValuePair);
        }
        mNameValuePair = new NameValuePair("sender_id", task.senderIdList);
        nameValuePairs.add(mNameValuePair);
        if(task.attachmentHash.length() > 0)
        {
            mNameValuePair = new NameValuePair("attence_name", task.attachmentName);
            nameValuePairs.add(mNameValuePair);
            mNameValuePair = new NameValuePair("attence_hash", task.attachmentHash);
            nameValuePairs.add(mNameValuePair);
        }
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, TASK_ADD_SUCCESS, mContext, nameValuePairs,task);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getTask(Context mContext, Handler mHandler,String projectid,String tags,String ntype,String filter,String order,String orderfilt,String keyword,int page) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_LIST);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("notice_type", ntype);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_filter", filter);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("order_type", order);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("order_field", orderfilt);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("tag", tags);
        nameValuePairs.add(mNameValuePair);
        if(projectid.length() > 0)
        {
            mNameValuePair = new NameValuePair("project_id", projectid);
            nameValuePairs.add(mNameValuePair);
        }

        if(keyword.length() > 0)
        {
            mNameValuePair = new NameValuePair("keyword", keyword);
            nameValuePairs.add(mNameValuePair);
        }
        mNameValuePair = new NameValuePair("page_no", String.valueOf(page));
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("page_size", "20");
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, TASK_LIST_SUCCESS, mContext, nameValuePairs,keyword);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void getTaskDetial(Context mContext, Handler mHandler, Task task) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_TASK_GET);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", task.taskId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, TASK_DETIAL_SUCCESS, mContext, nameValuePairs,task);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }


    public static void getParentTask(Context mContext, Handler mHandler, Task task) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_PARENT_TASK_GET);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", task.taskId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GET_PARENT_SUCCESS, mContext, nameValuePairs,task);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getSon(Context mContext, Handler mHandler,Task task) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_SON_LIST);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", task.taskId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GET_SON_SUCCESS, mContext, nameValuePairs,task);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void getContrals(Context mContext, Handler mHandler,Task task) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_GET_CONTRAL);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", task.taskId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GET_CONTRAL_SUCCESS, mContext, nameValuePairs,task);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void setDes(Context mContext, Handler mHandler,Task mTask,String des) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_SET_TASK_DES);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", mTask.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("description", des);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, SET_DES_SUCCESS, mContext, nameValuePairs,des);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void setTaskName(Context mContext, Handler mHandler,Task mTask,String neme) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_SET_TASK_NAME);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", mTask.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("name", neme);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, SET_NAME_SUCCESS, mContext, nameValuePairs,neme);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void setBeginTime(Context mContext, Handler mHandler,Task mTask,String time) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_SET_TASK_TIME);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", mTask.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("begin_time", TimeUtils.dateToStamp(time+":00"));
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, SET_BEGIN_TIME_SUCCESS, mContext, nameValuePairs,time);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void setEndTime(Context mContext, Handler mHandler,Task mTask,String time) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_SET_TASK_TIME);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", mTask.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("end_time", TimeUtils.dateToStamp(time+":00"));
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, SET_END_TIME_SUCCESS, mContext, nameValuePairs,time);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void setTag(Context mContext, Handler mHandler,Task mTask,ArrayList<Select> mTags) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_SET_TAG);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", mTask.taskId);
        nameValuePairs.add(mNameValuePair);
        String ids = "";
        for (int i = 0; i < mTags.size(); i++) {
            if (mTags.get(i).iselect == true) {
                if (ids.length() == 0) {
                    ids += mTags.get(i).mId;
                } else {
                    ids += "," + mTags.get(i).mId;
                }
            }
        }
        mNameValuePair = new NameValuePair("tag", ids);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, SET_TAG_SUCCESS, mContext, nameValuePairs,mTags);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);


    }

    public static void setLock(Context mContext, Handler mHandler,Task mTask,int onoff) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_SET_LOCK);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", mTask.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("is_locked", String.valueOf(onoff));
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, SET_IS_LOCK, mContext, nameValuePairs,onoff);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void setStar(Context mContext, Handler mHandler,Task mTask,int onoff) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_SET_STARE);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", mTask.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("is_star", String.valueOf(onoff));
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, SET_IS_STARE, mContext, nameValuePairs,onoff);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void doDelete(Context mContext, Handler mHandler,Task mTask,int all) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_DELETE);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", mTask.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("is_son", String.valueOf(all));
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, DELETE_TASK_SUCCESS, mContext, nameValuePairs,all);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void doDeleteById(Context mContext, Handler mHandler,String id,int all) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_DELETE);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", id);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("is_son", String.valueOf(all));
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, DELETE_TASK_BYID_SUCCESS, mContext, nameValuePairs,all);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void setExistTask(Context mContext, Handler mHandler,Task mTask,Contacts contacts) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_REMOVE_MEMBER);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", mTask.taskId);
        nameValuePairs.add(mNameValuePair);
        if(contacts != null)
        mNameValuePair = new NameValuePair("sender_id", contacts.mRecordid);
        else
            mNameValuePair = new NameValuePair("sender_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EXIT_TASK_SUCCESS, mContext, nameValuePairs,contacts);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void setFinish(Context mContext, Handler mHandler,Task mTask,int finish,int son) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_SET_FINISH);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", mTask.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("is_complete", String.valueOf(finish));
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("is_son", String.valueOf(son));
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask;
        if(finish == 1 && son == 0) {
            mPostNetTask = new PostNetTask(urlString, mHandler, TASK_FINISH_SET_FINISH_SUCCESS, mContext, nameValuePairs,mTask);
        }
        else if(finish == 1 && son == 1) {
            mPostNetTask = new PostNetTask(urlString, mHandler, TASK_FINISH_SET_FINISH_ALL_SUCCESS, mContext, nameValuePairs,mTask);
        }
        else if(finish == 0 && son == 0) {
            mPostNetTask = new PostNetTask(urlString, mHandler, TASK_FINISH_SET_UNFINISH_ALL_SUCCESS, mContext, nameValuePairs,mTask);
        }
        else{
            mPostNetTask = new PostNetTask(urlString, mHandler, TASK_FINISH_SET_UNFINISH_ALL_SUCCESS, mContext, nameValuePairs,mTask);
        }
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void setLeader(Context mContext, Handler mHandler, Task mTask, Contacts contacts) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_SET_TASK_LEADER);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", mTask.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("leader_id", contacts.mRecordid);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, SET_LEADER_SUCCESS, mContext, nameValuePairs,contacts);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void setContrals(Context mContext, Handler mHandler, Task mTask,Contral contralModel) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_SET_CONTRAL);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", mTask.taskId);
        nameValuePairs.add(mNameValuePair);
        if(!mTask.projectId.equals("0"))
        {
            mNameValuePair = new NameValuePair("project_id", mTask.projectId);
            nameValuePairs.add(mNameValuePair);
        }
        mNameValuePair = new NameValuePair("project_item_id", contralModel.id);
        nameValuePairs.add(mNameValuePair);
        if(contralModel.type.equals(TaskManager.CONTRAL_DATE))
        {
            if (contralModel.dayeType == 0) {
                mNameValuePair = new NameValuePair("project_value", TimeUtils.dateToStamp(contralModel.testvalue + " 00:00:00"));
                nameValuePairs.add(mNameValuePair);
            } else {
                mNameValuePair = new NameValuePair("project_value", TimeUtils.dateToStamp(contralModel.testvalue + ":00"));
                nameValuePairs.add(mNameValuePair);
            }
        }else {
            mNameValuePair = new NameValuePair("project_value", contralModel.testvalue);
            nameValuePairs.add(mNameValuePair);
        }
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, SET_CONTRAL_SUCCESS, mContext, nameValuePairs,contralModel);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void changeTaskStage(Context mContext, Handler mHandler,Task task, Stage stage) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_SET_STAGE);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", task.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_stages_id", stage.stageId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, CHANGE_STAGE_SUCCESS, mContext, nameValuePairs,task);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void getSetParentTask(Context mContext, Handler mHandler,Task task,int page) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_PARENT_LIST);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", task.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("page_no", String.valueOf(page));
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("page_size", "40");
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GET_TASK_SETPARENT_SUCCESS, mContext, nameValuePairs,task);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }


    public static void addTaskMember(Context mContext, Handler mHandler,Task task,String ids) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_ADD_MEMBER);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", task.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("sender_id", ids);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, ADD_TASK_MEMBER_SUCCESS, mContext, nameValuePairs,task);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void setTaskProject(Context mContext, Handler mHandler, Task task, Project project) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_SET_PROJECT);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", task.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, SET_TASK_PROJECT_SUCCESS, mContext, nameValuePairs,project);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void setTaskCancleProject(Context mContext, Handler mHandler, Task task) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_CANCLE_PARENT);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", task.taskId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, TASK_CANCLE_PARENT_SUCCESS, mContext, nameValuePairs);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }


    public static void setTaskParent(Context mContext, Handler mHandler, Task task,Task parent) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_SET_PARENT);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("task_id", task.taskId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("parent_id", parent.taskId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, SET_TASK_PARENT_SUCCESS, mContext, nameValuePairs,parent);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }


}
