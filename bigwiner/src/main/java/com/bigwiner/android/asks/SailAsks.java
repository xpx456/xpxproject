package com.bigwiner.android.asks;

import android.content.Context;
import android.os.Handler;

import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.entity.Notice;
import com.bigwiner.android.view.BigwinerApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.HTTP;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.URLEncodedUtils;
import intersky.xpxnet.net.nettask.PostJsonNetTask;

public class SailAsks {

    public static final String SAIL_APPLY_PATH = "/bigwinner/add/sale/apply";
    public static final String SAIL_COMPANY_MEMBER_PATH = "/bigwinner/query/sale/list";
    public static final String SAIL_MEMBER_PATH = "/bigwinner/query/member/list";
    public static final String SAIL_RED_PATH = "/bigwinner/query/complaint/list";
    public static final String SAIL_COMPLANT_DETIAL = "/bigwinner/query/complaint/manager";
    public static final int SAIL_APPLY_RESULT = 100400;
    public static final int SAIL_COMPANY_MEMBER_RESULT = 100401;
    public static final int SAIL_MEMBER_RESULT = 100402;
    public static final int SAIL_RED_RESULT = 100403;
    public static void getSailCompanyMember(Context mContext , Handler mHandler,int currentpage,int size,String keyword) {
        String urlString = BigwinerApplication.BASE_NET_PATH+SAIL_COMPANY_MEMBER_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token",NetUtils.getInstance().token);
            jsonObject.put("pagesize",size);
            jsonObject.put("currentpage",currentpage);
            jsonObject.put("keyword",keyword);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, SAIL_COMPANY_MEMBER_RESULT,
                    mContext, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getComplantMember(Context mContext , Handler mHandler,int currentpage,int size,String kewword) {
        String urlString = BigwinerApplication.BASE_NET_PATH+SAIL_RED_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token",NetUtils.getInstance().token);
            jsonObject.put("pagesize",size);
            jsonObject.put("currentpage",currentpage);
            jsonObject.put("keyword",kewword);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, SAIL_RED_RESULT,
                    mContext, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void applaySail(Context mContext , Handler mHandler,String name,String mobile,String business) {
        String urlString = BigwinerApplication.BASE_NET_PATH+SAIL_APPLY_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token",NetUtils.getInstance().token);
            jsonObject.put("companyname",BigwinerApplication.mApp.company.name);
            jsonObject.put("companyaddr",BigwinerApplication.mApp.company.address);
            jsonObject.put("contacter",name);
            jsonObject.put("mobile",mobile);
            jsonObject.put("mainbusiness",business);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, SAIL_APPLY_RESULT,
                    mContext, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String praseComplantDetialUrl(String id) {
        try {
            return BigwinerApplication.BASE_NET_PATH+SAIL_COMPLANT_DETIAL+"?"+"token="+ URLEncoder.encode(NetUtils.getInstance().token, HTTP.UTF_8)
            +"&no="+id;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return BigwinerApplication.BASE_NET_PATH+SAIL_COMPLANT_DETIAL+"?"+"token="+ NetUtils.getInstance().token
                    +"&no="+id;
        }
    }
}
