package intersky.function.asks;

import android.content.Context;
import android.os.Handler;


import org.json.JSONException;
import org.json.JSONObject;

import intersky.appbase.entity.Conversation;
import intersky.function.FunctionUtils;
import intersky.function.receiver.entity.BussinessWarnItem;
import intersky.function.receiver.entity.Function;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostJsonNetTask;

//01
public class WorkFlowAsks {

    public static final String WORKFLOW_LIST = "workflow/list";
    public static final String REMIND_LIST = "reminder/list";
    public static final String WORKFLOW_SEND = "workflow/flowout";
    public static final String REMIND_DISMISS = "reminder/dismiss";
    public static final String PATH_EXAMINE_DETIAL = "workflow/task";
    public static final int WORKFLOW_LIST_SUCCESS = 1180100;
    public static final int REMIND_LIST_SUCCESS = 1180101;
    public static final int REMIND_DISMISS_SUCCESS = 1180102;
    public static final int WORKFLOW_TASK_SUCCESS = 1180103;
    public static final int WORKFLOW_ACCEPT_SUCCESS = 1180104;
    public static final int WORKFLOW_VETO_SUCCESS = 1180105;

    public static void getWorkFlowList(Context mContext, Handler mHandler,int state) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,WORKFLOW_LIST);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("state", state);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, WORKFLOW_LIST_SUCCESS, mContext, postBody,state);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void getWorkFlowList(Context mContext, Handler mHandler) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,WORKFLOW_LIST);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, WORKFLOW_LIST_SUCCESS,
                     mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getWorkFlowTask(Context mContext, Handler mHandler, String taskid) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,PATH_EXAMINE_DETIAL);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("task_id", taskid);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, WORKFLOW_TASK_SUCCESS, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doAccept(Context mContext, Handler mHandler, String taskid,String content) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,WORKFLOW_SEND);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("task_id", taskid);
            jsonObject.put("accept", true);
            jsonObject.put("content", content);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, WORKFLOW_ACCEPT_SUCCESS,
                     mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doVeto(Context mContext, Handler mHandler, String taskid, String content) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,WORKFLOW_SEND);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("task_id", taskid);
            jsonObject.put("accept", false);
            jsonObject.put("content", content);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, WORKFLOW_VETO_SUCCESS,
                     mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getRemindList(Context mContext, Handler mHandler) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,REMIND_LIST);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, REMIND_LIST_SUCCESS,
                    mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void getRemindDismiss(Context mContext, Handler mHandler, BussinessWarnItem mBussinessWarnItem) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,REMIND_DISMISS);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("serial_id", mBussinessWarnItem.serialID);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, REMIND_DISMISS_SUCCESS,
                     mContext, postBody,mBussinessWarnItem);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
