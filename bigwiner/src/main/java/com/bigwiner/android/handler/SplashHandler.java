package com.bigwiner.android.handler;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.prase.LoginPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.LoginActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.SplashActivity;

import java.lang.ref.WeakReference;

import intersky.apputils.AppUtils;
import intersky.chat.ChatUtils;
import intersky.conversation.BigWinerConversationManager;
import intersky.conversation.database.BigWinerDBHelper;
import intersky.filetools.FileUtils;
import intersky.guide.GuideUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//00


public class SplashHandler extends Handler {

    public final static int EVENT_START_LOGIN = 1030000;
    public final static int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1040002;
    public final static int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1040003;
    public final static int PERMISSION_REQUEST_READ_ACCESS_COARSE_LOCATION = 1040004;

    public SplashActivity theActivity;

    public SplashHandler(SplashActivity mSplashActivity) {
        theActivity = mSplashActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case EVENT_START_LOGIN:
                theActivity.mSplashPresenter.startMain();
                break;
            case PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE:
                AppUtils.getPermission(Manifest.permission.READ_EXTERNAL_STORAGE,theActivity, PERMISSION_REQUEST_READ_EXTERNAL_STORAGE,theActivity.mSplashPresenter.mSplashHandler);
                break;
            case PERMISSION_REQUEST_READ_EXTERNAL_STORAGE:
                theActivity.mSplashPresenter.initdata();
                AppUtils.getPermission(Manifest.permission.ACCESS_COARSE_LOCATION,theActivity, PERMISSION_REQUEST_READ_ACCESS_COARSE_LOCATION,theActivity.mSplashPresenter.mSplashHandler);
                break;
            case PERMISSION_REQUEST_READ_ACCESS_COARSE_LOCATION:
                AppUtils.getPermission(Manifest.permission.ACCESS_WIFI_STATE,theActivity,EVENT_START_LOGIN,theActivity.mSplashPresenter.mSplashHandler);
                break;
            case LoginAsks.PUSH_LOGIN_AGAIN:
                if(BigwinerApplication.mAccount.islogin == true)
                LoginAsks.doPushlogin(theActivity.mSplashPresenter.mSplashHandler,theActivity);
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
