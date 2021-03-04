package com.bigwiner.android.asks;

import android.content.Context;
import android.os.Handler;
import android.os.ParcelUuid;

import com.bigwiner.android.entity.JoinData;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.entity.Notice;
import com.bigwiner.android.view.BigwinerApplication;

import org.json.JSONException;
import org.json.JSONObject;

import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostJsonNetTask;

public class DetialAsks {

    public static final String NEW_NOTICE_DETIAL_PATH = "/bigwinner/query/newornotice_detail_info";
    public static final String MEETING_DETIAL_PATH = "/bigwinner/query/conferance_detail_info";
    public static final String MEETING_JOIN_PATH = "/bigwinner/conference/enroll";
    public static final String MEETING_SIGN_PATH = "/bigwinner/conference/check/signed";
    public static final String TOOL_PATH = "/bigwinner/query/tool";
    public static final String NOTICE_NEW_PATH = "/bigwinner/query/newornotice_detail_info";
    public static final String REQUEST_PATH = "/bigwinner/query/question/investigation";
    public static final String COMPANY_LIST_PATH = "/bigwinner/company/list";
    public static final int NEW_NOTICE_DETIAL_RESULT = 100300;
    public static final int MEETING_DETIAL_RESULT = 100301;
    public static final int MEETING_JOIN_RESULT= 100303;
    public static final int MEETING_SIGN_RESULT= 100304;
    public static final int COMPANY_LIST_RESULT= 100305;
    public static final String ACTION_MEETING_JOIN_SUCCESS = "ACTION_MEETING_JOIN_SUCCESS";
    public static final String ACTION_MEETING_SIGN_SUCCESS = "ACTION_MEETING_SIGN_SUCCESS";
    public static void getNewsAndNoticesDetial(Context mContext , Handler mHandler, Notice notice) {
        String urlString = BigwinerApplication.BASE_NET_PATH+NEW_NOTICE_DETIAL_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token",NetUtils.getInstance().token);
            jsonObject.put("no",notice.recordid);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, NEW_NOTICE_DETIAL_RESULT,
                    mContext, postBody,notice,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getMettingsDetial(Context mContext , Handler mHandler, Meeting meeting) {
        String urlString = BigwinerApplication.BASE_NET_PATH+MEETING_DETIAL_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
//            jsonObject.put("token",NetUtils.getInstance().token);
            jsonObject.put("no",meeting.recordid);
            jsonObject.put("uid",BigwinerApplication.mApp.mAccount.mRecordId);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MEETING_DETIAL_RESULT,
                    mContext, postBody,meeting,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getMettingsJoin(Context mContext , Handler mHandler, String id) {
        String urlString = BigwinerApplication.BASE_NET_PATH+MEETING_JOIN_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("no",id);
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("myports",BigwinerApplication.mApp.my.ports.getString());
            jsonObject.put("mypositions",BigwinerApplication.mApp.my.positions.mName);
            jsonObject.put("mybusinessareas",BigwinerApplication.mApp.my.businessareaSelect.getString());
            jsonObject.put("mybusinesstypes",BigwinerApplication.mApp.my.businesstypeSelect.getString());
            jsonObject.put("needports",BigwinerApplication.mApp.want.ports.getString());
            jsonObject.put("needpositions",BigwinerApplication.mApp.want.positions.mName);
            jsonObject.put("needbusinessareas",BigwinerApplication.mApp.want.businessareaSelect.getString());
            jsonObject.put("needbusinesstypes",BigwinerApplication.mApp.want.businesstypeSelect.getString());

//            jsonObject.put("myports",BigwinerApplication.mApp.my.ports.mName);
//            jsonObject.put("mypositions",BigwinerApplication.mApp.my.positions.mName);
//            jsonObject.put("myroutes",BigwinerApplication.mApp.my.routes.mName);
//            jsonObject.put("myserices",BigwinerApplication.mApp.my.serices.mName);
//            jsonObject.put("needports",BigwinerApplication.mApp.want.ports.mName);
//            jsonObject.put("needpositions",BigwinerApplication.mApp.want.positions.mName);
//            jsonObject.put("needroutes",BigwinerApplication.mApp.want.routes.mName);
//            jsonObject.put("needserices",BigwinerApplication.mApp.want.serices.mName);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MEETING_JOIN_RESULT,
                    mContext, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getMettingsSign(Context mContext , Handler mHandler, String id) {
        String urlString = BigwinerApplication.BASE_NET_PATH+MEETING_SIGN_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token",NetUtils.getInstance().token);
            jsonObject.put("qrcode",id);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MEETING_SIGN_RESULT,
                    mContext, postBody,id,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getComplanyList(Context mContext , Handler mHandler) {
        String urlString = BigwinerApplication.BASE_NET_PATH+COMPANY_LIST_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token",NetUtils.getInstance().token);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, COMPANY_LIST_RESULT,
                    mContext, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getRequestUrl(String id)
    {
        String urlString = BigwinerApplication.BASE_NET_PATH+REQUEST_PATH+"?no="+id+"&uid="+BigwinerApplication.mApp.mAccount.mRecordId;
        return urlString;
    }

    public static String getNoticeNewUrl(String id)
    {
        String urlString = BigwinerApplication.BASE_NET_PATH+NOTICE_NEW_PATH+"?no="+id+"&uid="+BigwinerApplication.mApp.mAccount.mRecordId;
        return urlString;
    }
}
