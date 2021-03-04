package com.intersky.android.asks;

import android.content.Context;
import android.os.Handler;

import com.intersky.android.view.InterskyApplication;

import java.util.ArrayList;

import intersky.oa.OaUtils;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;

public class ConversationAsks {

    public static final String NOTATION_MESSAGE_PATH = "api/v1/Message.html";
    public static final String NOTATION_MESSAGE_LIST = "get.message.list";
    public static final String NOTATION_MESSAGE_LIST_READ = "set.message.read.list";
    public static final String NOTATION_MESSAGE_GET = "get.message.item";
    public static final String NOTATION_MESSAGE_MESSAGE_LIST = "get.message.mass.list";

    public static final int OAMESSAGE_SUCCESS = 1111000;
    public static final int OAMESSAGE_READ_SUCCESS = 1121001;
    public static final int OAMESSAGE_ONE_SUCCESS = 1121002;
    public static final int GROP_SUCCESS = 1121003;
    public static final int OAMESSAGE_OPEN_SUCCESS = 1121004;
    public static void getOaMessages(Context mContext, int page, Handler mHandler,String userid,String companyid) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(InterskyApplication.mApp.mService,NOTATION_MESSAGE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", NOTATION_MESSAGE_LIST);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", userid);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", companyid);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("source_type", "oa,iweb[workflow],iweb[mail],iCloud[workflow],iCloud[mail]");
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("is_read", "0");
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("page_no", String.valueOf(page));
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("page_size", "40");
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, OAMESSAGE_SUCCESS, mContext, nameValuePairs,page);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void setRead(Context mContext,Handler mHandler,String readid,String userid,String companyid) {
        String urlString = OaUtils.getOaUtils().createURLStringoa(InterskyApplication.mApp.mService,NOTATION_MESSAGE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", NOTATION_MESSAGE_LIST_READ);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", userid);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", companyid);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("message_id", readid);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, OAMESSAGE_READ_SUCCESS, mContext, nameValuePairs);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getFuncMessagesOne(Context mContext,Handler mHandler,String id,String userid,String companyid) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(InterskyApplication.mApp.mService,NOTATION_MESSAGE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", NOTATION_MESSAGE_GET);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", userid);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", companyid);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("message_id", id);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, OAMESSAGE_ONE_SUCCESS, mContext, nameValuePairs);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }


    public static void getFuncMessagesOne(Context mContext,Handler mHandler,String id,String userid,String companyid,String stype) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(InterskyApplication.mApp.mService,NOTATION_MESSAGE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", NOTATION_MESSAGE_GET);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", userid);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", companyid);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("message_id", id);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("source_type", stype);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, OAMESSAGE_ONE_SUCCESS, mContext, nameValuePairs);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void getFuncMessagesOpen(Context mContext,Handler mHandler,String id,String userid,String companyid,String stype) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(InterskyApplication.mApp.mService,NOTATION_MESSAGE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", NOTATION_MESSAGE_GET);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", userid);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", companyid);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("message_id", id);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("source_type", stype);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, OAMESSAGE_ONE_SUCCESS, mContext, nameValuePairs,userid);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void getFuncGropMessage(Context mContext,Handler mHandler,String zzImei) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(InterskyApplication.mApp.mService,NOTATION_MESSAGE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", NOTATION_MESSAGE_MESSAGE_LIST);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", "00000000-0000-0000-0000-000000000002");
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("device_token", zzImei);
        nameValuePairs.add(mNameValuePair);

        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GROP_SUCCESS, mContext, nameValuePairs);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void getFuncGropMessage(Context mContext,Handler mHandler,String zzImei,String msgid) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(InterskyApplication.mApp.mService,NOTATION_MESSAGE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", NOTATION_MESSAGE_MESSAGE_LIST);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", "00000000-0000-0000-0000-000000000002");
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("device_token", zzImei);
        nameValuePairs.add(mNameValuePair);

        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GROP_SUCCESS, mContext, nameValuePairs,msgid);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }
}
