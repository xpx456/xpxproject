package com.bigwiner.android.asks;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.ParcelUuid;

import com.bigwiner.R;
import com.bigwiner.android.entity.Notice;
import com.bigwiner.android.entity.Source;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.view.BigwinerApplication;

import org.json.JSONException;
import org.json.JSONObject;

import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostJsonNetTask;

public class SourceAsks {

    public static final String SOURCE_LIST_ALL= "/biggerWinner/get/resources/all";
    public static final String SOURCE_LIST_SEARCH= "/biggerWinner/resources/list/search";
    public static final String SOURCE_ITEM_COLLECT= "/biggerWinner/resources/my/collection";
    public static final String SOURCE_COLLECT_LIST= "/biggerWinner/resources/my/collection/list";
    public static final String SOURCE_DETIAL= "/biggerWinner/resources/detail";
    public static final String SOURCE_EDIT= "/biggerWinner/resources/my/edit";
    public static final String SOURCE_LIST_MY="/biggerWinner/resources/my/publish/list";
    public static final String SOURCE_DELETE= "/biggerWinner/resources/my/delete";
    public static final String SOURCE_ADD= "/biggerWinner/publish/resources/add";
    public static final String SOURCE_ADD_CHECK = "/biggerWinner/resources/user/publish/istransfinite";
    public static final String SOURCE_ADD_STATUE = "/biggerWinner/resources/my/publish/isclose";
    public static final int SOURCE_LIST_ALL_RESULT = 100600;
    public static final int SOURCE_LIST_SEARCH_RESULT = 100601;
    public static final int SOURCE_COLLECT_RESULT = 100602;
    public static final int SOURCE_COLLECT_LIST_RESULT = 100603;
    public static final int SOURCE_ADD_RESULT = 100604;
    public static final int SOURCE_DETIAL_RESULT = 100605;
    public static final int SOURCE_DELETE_RESULT = 100606;
    public static final int SOURCE_EDIT_RESULT = 100607;
    public static final int SOURCE_LIST_MY_RESULT = 100608;
    public static final int SOURCE_ADD_CHECK_RESULT = 100609;
    public static final int SOURCE_OPEN_RESULT = 100610;
    public static final int SOURCE_CLOSE_RESULT = 100611;

    public static final String ACIONT_SOURCE_CREAT = "ACIONT_SOURCE_CREAT";
    public static final String ACIONT_SOURCE_EDIT = "ACIONT_SOURCE_EDIT";
    public static final String ACIONT_SOURCE_DELETE = "ACIONT_SOURCE_DELETE";

    //# type 类型  0 全部资源 1 需求资源 2 提供资源
    public static void getSourceListAll(Context mContext , Handler mHandler, String type,int pagesize, int current) {
        String urlString = BigwinerApplication.BASE_NET_PATH + SOURCE_LIST_ALL;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("type", type);
            jsonObject.put("pagesize", pagesize);
            jsonObject.put("currentpage", current);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, SOURCE_LIST_ALL_RESULT,
                    mContext, postBody, type,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getSourceListMy(Context mContext , Handler mHandler, String type,int pagesize, int current) {
        String urlString = BigwinerApplication.BASE_NET_PATH + SOURCE_LIST_MY;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("type", type);
            jsonObject.put("pagesize", pagesize);
            jsonObject.put("currentpage", current);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, SOURCE_LIST_MY_RESULT,
                    mContext, postBody, type,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getSourceListSearch(Context mContext , Handler mHandler, String type,String keyword,int pagesize, int current) {
        String urlString = BigwinerApplication.BASE_NET_PATH + SOURCE_LIST_ALL;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("type", type);
            jsonObject.put("keyword", keyword);
            jsonObject.put("pagesize", pagesize);
            jsonObject.put("currentpage", current);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, SOURCE_LIST_SEARCH_RESULT,
                    mContext, postBody, type,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getSourceCollect(Context mContext , Handler mHandler, SourceData sourceData) {
        String urlString = BigwinerApplication.BASE_NET_PATH + SOURCE_ITEM_COLLECT;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("no", sourceData.id);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, SOURCE_COLLECT_RESULT,
                    mContext, postBody, sourceData,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getSourceListCollect(Context mContext , Handler mHandler,int pagesize, int current) {
        String urlString = BigwinerApplication.BASE_NET_PATH + SOURCE_COLLECT_LIST;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("pagesize", pagesize);
            jsonObject.put("currentpage", current);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, SOURCE_COLLECT_LIST_RESULT,
                    mContext, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getSourceAddCheck(Context mContext , Handler mHandler, Intent intent) {
        String urlString = BigwinerApplication.BASE_NET_PATH + SOURCE_ADD_CHECK;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, SOURCE_ADD_CHECK_RESULT,
                    mContext, postBody,intent,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getSourceAdd(Context mContext , Handler mHandler, SourceData sourceData) {
        String urlString = BigwinerApplication.BASE_NET_PATH + SOURCE_ADD;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            if(sourceData.sourcetype.equals(mContext.getString(R.string.source_type_want)))
            jsonObject.put("resourcestype", "1");
            else
                jsonObject.put("resourcestype", "2");
            jsonObject.put("tital", sourceData.name);
            jsonObject.put("port", sourceData.port);
            jsonObject.put("businesstype", sourceData.type);
            jsonObject.put("businessarea", sourceData.area);
            jsonObject.put("description", sourceData.memo);
            jsonObject.put("startdate", sourceData.start.substring(0,10));
            jsonObject.put("enddate", sourceData.end.substring(0,10));
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, SOURCE_ADD_RESULT,
                    mContext, postBody, sourceData,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getSourceEdit(Context mContext , Handler mHandler, SourceData sourceData) {
        String urlString = BigwinerApplication.BASE_NET_PATH + SOURCE_EDIT;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("no", sourceData.id);
            jsonObject.put("tital", sourceData.name);
            jsonObject.put("port", sourceData.port);
            jsonObject.put("businesstype", sourceData.type);
            jsonObject.put("businessarea", sourceData.area);
            jsonObject.put("description", sourceData.memo);
            jsonObject.put("startdate", sourceData.start.substring(0,10));
            jsonObject.put("enddate", sourceData.end.substring(0,10));
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, SOURCE_EDIT_RESULT,
                    mContext, postBody, sourceData,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getSourceDetial(Context mContext , Handler mHandler, SourceData sourceData) {
        String urlString = BigwinerApplication.BASE_NET_PATH + SOURCE_DETIAL;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("no", sourceData.id);
            jsonObject.put("uid", BigwinerApplication.mApp.mAccount.mRecordId);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, SOURCE_DETIAL_RESULT,
                    mContext, postBody, sourceData,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getSourceDelete(Context mContext , Handler mHandler, SourceData sourceData) {
        String urlString = BigwinerApplication.BASE_NET_PATH + SOURCE_DELETE;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("no", sourceData.id);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, SOURCE_DELETE_RESULT,
                    mContext, postBody, sourceData,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getSourceOpen(Context mContext , Handler mHandler, SourceData sourceData) {
        String urlString = BigwinerApplication.BASE_NET_PATH + SOURCE_ADD_STATUE;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("no", sourceData.id);
            jsonObject.put("status", 1);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, SOURCE_OPEN_RESULT,
                    mContext, postBody, sourceData,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getSourceClose(Context mContext , Handler mHandler, SourceData sourceData) {
        String urlString = BigwinerApplication.BASE_NET_PATH + SOURCE_ADD_STATUE;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("no", sourceData.id);
            jsonObject.put("status", 0);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, SOURCE_CLOSE_RESULT,
                    mContext, postBody, sourceData,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
