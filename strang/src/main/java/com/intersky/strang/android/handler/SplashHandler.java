package com.intersky.strang.android.handler;

import android.Manifest;
import android.os.Handler;
import android.os.Message;

import com.intersky.strang.android.view.activity.SplashActivity;

import intersky.apputils.AppUtils;

public class SplashHandler extends Handler {

    public final static int EVENT_START_LOGIN = 1030000;
    public final static int PERMISSION_REQUEST_READ_PHONE_STATE = 1040001;
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
                theActivity.mSplashPresenter.startLogin();
                break;
            case PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE:
                AppUtils.getPermission(Manifest.permission.READ_EXTERNAL_STORAGE,theActivity, PERMISSION_REQUEST_READ_EXTERNAL_STORAGE,theActivity.mSplashPresenter.mSplashHandler);
                break;
            case PERMISSION_REQUEST_READ_EXTERNAL_STORAGE:
                AppUtils.getPermission(Manifest.permission.ACCESS_COARSE_LOCATION,theActivity, PERMISSION_REQUEST_READ_ACCESS_COARSE_LOCATION,theActivity.mSplashPresenter.mSplashHandler);
                break;
            case PERMISSION_REQUEST_READ_ACCESS_COARSE_LOCATION:
                AppUtils.getPermission(Manifest.permission.ACCESS_WIFI_STATE,theActivity,EVENT_START_LOGIN,theActivity.mSplashPresenter.mSplashHandler);
                break;
        }

    }
}
