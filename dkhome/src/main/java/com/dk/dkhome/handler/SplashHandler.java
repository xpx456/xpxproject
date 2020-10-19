package com.dk.dkhome.handler;

import android.Manifest;
import android.os.Handler;
import android.os.Message;

import com.dk.dkhome.view.activity.SplashActivity;

import intersky.apputils.AppUtils;

public class SplashHandler extends Handler {

    public final static int EVENT_START_LOGIN = 1030000;
    public final static int PERMISSION_REQUEST_READ_PHONE_STATE = 1040001;

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

        }

    }
}
