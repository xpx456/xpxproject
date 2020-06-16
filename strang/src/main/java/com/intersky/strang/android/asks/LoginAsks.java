package com.intersky.strang.android.asks;

import android.content.Context;
import android.os.Handler;

import com.intersky.strang.android.handler.LoginHandler;
import com.intersky.strang.android.view.StrangApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import intersky.chat.ContactManager;
import intersky.oa.OaUtils;
import intersky.xpxnet.net.BasicNameValuePair;
import intersky.xpxnet.net.HTTP;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.URLEncodedUtils;
import intersky.xpxnet.net.nettask.NetTask;
import intersky.xpxnet.net.nettask.PostJsonNetTask;
import intersky.xpxnet.net.nettask.PostNetTask;


public class LoginAsks {
    public static final String PUSH_PATH = "api/v1/Ios";
    public static final String LOGIN_PATH = "user/login";
    public static final String EXIT_PATH = "user/logout";
    public static final String PUSH_LOGIN_IN = "add.ios.landing";
    public static final String PUSH_LOGOUT= "close.ios.token";
    public static final String GET_IP_PATH = "http://i-apps.e-desktop.org/getip?";
    public static final String LOGIN_PATH_SCAN = "strategy/app/login/allow";
    public static final int LOGIN_SUCCESS = 1010000;
    public static final int LOGOUT_SUCCESS = 1010002;
    public static final int PUSH_LOGINOUT_SUCCESS = 1010003;
    public static final int LOGIN_FAIL = 1020000;
    public static final int GET_ADDRESS_SUCCESS = 1010001;
    public static final int GET_ADDRESS_FAIL = 1020001;
    public static final int PUSH_LOGININ_SUCCESS = 1010004;
    public static final int SAFE_LOGIN_SUCCESS = 1010005;
    public static void doLoging(String account, String password, String szImei, String computername, Handler mHandler, Context mContext)
    {


        try {
            String urlString = NetUtils.getInstance().praseUrl(StrangApplication.mApp.mService,LOGIN_PATH);//+"?"+"username="+account+"&password="+password;
            JSONObject jsonObject = new JSONObject();
//            AppUtils.showMessage(mContext,"lingk:"+urlString);
            jsonObject.put("username",account);
            jsonObject.put("password",password);
            jsonObject.put("kind",2);
            jsonObject.put("device_id",szImei);
            jsonObject.put("computername",computername);
            String postBody =  jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, LOGIN_SUCCESS, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public static void getIpAddress(String code, Handler mHandler, Context mContext)
    {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("Operation", "1"));
        nvps.add(new BasicNameValuePair("IdentificationCodes",
                code));
        nvps.add(new BasicNameValuePair("Rand", "123"));
        URLEncodedUtils.format(nvps, HTTP.UTF_8);
        String url = GET_IP_PATH + URLEncodedUtils.format(nvps, HTTP.UTF_8);
        NetTask mNetTask = new NetTask(url, mHandler, GET_ADDRESS_SUCCESS, mContext);
        NetTaskManager.getInstance().addNetTask(mNetTask);
    }

    public static void doLogout(Handler mHandler,Context mContext) {


        try {
            String urlString = NetUtils.getInstance().praseUrl(StrangApplication.mApp.mService,EXIT_PATH);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            String postBody =  jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, LOGOUT_SUCCESS, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        doPushLogout(mHandler,mContext);
    }
    public static void doPushLogout(Handler mHandler,Context mContext) {


        String urlString = "http://push.cloud.seo.com.cn/"+PUSH_PATH;
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", PUSH_LOGOUT);
        items.add(item);
        item = new BasicNameValuePair("company_id", ContactManager.mContactManager.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", ContactManager.mContactManager.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("device_token", StrangApplication.mApp.szImei);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, PUSH_LOGINOUT_SUCCESS, mContext, items);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void doPushlogin(Handler mHandler,Context mContext) {
        if (StrangApplication.mApp.deviceId.length() != 0) {


            String urlString = "http://push.cloud.seo.com.cn/"+PUSH_PATH;
            ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
            BasicNameValuePair item = new BasicNameValuePair("method", PUSH_LOGIN_IN);
            items.add(item);
            item = new BasicNameValuePair("company_id", ContactManager.mContactManager.mAccount.mCompanyId);
            items.add(item);
            item = new BasicNameValuePair("user_id", ContactManager.mContactManager.mAccount.mRecordId);
            items.add(item);
            item = new BasicNameValuePair("source", "seo");
            items.add(item);
            item = new BasicNameValuePair("customer", "android");
            items.add(item);
            item = new BasicNameValuePair("device_token", StrangApplication.mApp.szImei);
            items.add(item);
            item = new BasicNameValuePair("device_id", StrangApplication.mApp.deviceId);
            items.add(item);
            PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, PUSH_LOGININ_SUCCESS, mContext, items);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } else {
            mHandler.sendEmptyMessageDelayed(LoginHandler.PUSH_LOGIN_AGAIN, 500);
        }
    }

    public static void safelogin(Handler mHandler,Context mContext,String serial,String type) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(StrangApplication.mApp.mService,LOGIN_PATH_SCAN);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("serial",serial);
            jsonObject.put("type",type);
            jsonObject.put("user_id",StrangApplication.mApp.mAccount.mRecordId);
            jsonObject.put("token", NetUtils.getInstance().token);
            String postBody =  jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, SAFE_LOGIN_SUCCESS, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
