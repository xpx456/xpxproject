package com.bigwiner.android.handler;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.prase.LoginPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.LoginActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.PopPushActivity;

import intersky.apputils.AppUtils;
import intersky.chat.ChatUtils;
import intersky.conversation.BigWinerConversationManager;
import intersky.conversation.database.BigWinerDBHelper;
import intersky.guide.GuideUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//00


public class PopPushHandler extends Handler {

    public final static int EVENT_START_LOGIN = 1030000;
    public final static int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1040002;
    public final static int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1040003;
    public final static int PERMISSION_REQUEST_READ_ACCESS_COARSE_LOCATION = 1040004;

    public PopPushActivity theActivity;

    public PopPushHandler(PopPushActivity mPopPushActivity) {
        theActivity = mPopPushActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case EVENT_START_LOGIN:
                theActivity.mPopPushPresenter.startMain();
                break;
            case PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE:
                AppUtils.getPermission(Manifest.permission.READ_EXTERNAL_STORAGE,theActivity, PERMISSION_REQUEST_READ_EXTERNAL_STORAGE,theActivity.mPopPushPresenter.mPopPushHandler);
                break;
            case PERMISSION_REQUEST_READ_EXTERNAL_STORAGE:
                theActivity.mPopPushPresenter.initdata();
                AppUtils.getPermission(Manifest.permission.ACCESS_COARSE_LOCATION,theActivity, PERMISSION_REQUEST_READ_ACCESS_COARSE_LOCATION,theActivity.mPopPushPresenter.mPopPushHandler);
                break;
            case PERMISSION_REQUEST_READ_ACCESS_COARSE_LOCATION:
                AppUtils.getPermission(Manifest.permission.ACCESS_WIFI_STATE,theActivity,EVENT_START_LOGIN,theActivity.mPopPushPresenter.mPopPushHandler);
                break;
            case LoginAsks.PUSH_LOGIN_AGAIN:
                if(BigwinerApplication.mAccount.islogin == true)
                LoginAsks.doPushlogin(theActivity.mPopPushPresenter.mPopPushHandler,theActivity);
                break;
            case LoginAsks.PUSH_LOGININ_SUCCESS:
                theActivity.finish();
                break;
            case NetUtils.NO_INTERFACE:
            case NetUtils.NO_NET_WORK:
                break;
            case NetUtils.TOKEN_ERROR:
                if(BigwinerApplication.mApp.mAccount.islogin == true) {
                    BigwinerApplication.mApp.logout(BigwinerApplication.mApp.mAppHandler,BigwinerApplication.mApp.appActivityManager.getCurrentActivity());
                    NetUtils.getInstance().cleanTasks();
                }
                break;
        }

    }
}
