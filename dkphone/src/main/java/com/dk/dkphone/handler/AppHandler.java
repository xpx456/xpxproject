package com.dk.dkphone.handler;

import android.os.Handler;
import android.os.Message;

import com.dk.dkphone.view.DkPadApplication;

public class AppHandler extends Handler {

    public static final int SET_PROTECT_TIME = 10000;
    public static final int SET_UPDATA_SCREEN_PROTECT = 10001;
    public static final int SET_SCREEN_PROTECT_STATE = 10002;
    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {

            case SET_PROTECT_TIME:
                DkPadApplication.mApp.setProtectTime((Integer) msg.obj);
                break;
            case SET_UPDATA_SCREEN_PROTECT:
                DkPadApplication.mApp.updtatScreenProtect();
                break;
            case SET_SCREEN_PROTECT_STATE:
                DkPadApplication.mApp.setProtectState((Boolean) msg.obj);
                break;
        }

    }

}
