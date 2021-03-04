package com.accesscontrol.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.accesscontrol.R;
import com.accesscontrol.asks.DeviceAsks;
import com.accesscontrol.asks.MqttAsks;
import com.accesscontrol.view.AccessControlApplication;
import com.accesscontrol.view.activity.MainActivity;

import intersky.xpxnet.net.NetObject;


//01


public class MainHandler extends Handler {


    public MainActivity theActivity;
    public static final int UPDATA_TIME = 100000;
    public static final int SHOW_SUCCESS_VIEW = 110000;
    public static final int UPDATA_BTN = 110001;
    public static final int FINGER_SUCCESS = 110002;
    public static final int FINGER_FAIL = 110003;
    public static final int CLOSE_SUCCESS = 11004;
    public static final int INIT_DEVICE = 11005;
    public static final int CHECK_CLEAN = 11006;
    public MainHandler(MainActivity mLoginActivity) {
        theActivity = mLoginActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {

            case UPDATA_TIME:
                theActivity.mMainPresenter.updataTime();
                break;
            case SHOW_SUCCESS_VIEW:
                AccessControlApplication.mApp.showSuccessView((Intent) msg.obj,theActivity.successView,
                        theActivity.findViewById(R.id.activity_main));
                break;
            case UPDATA_BTN:
                theActivity.mMainPresenter.updataBtn();
                if(AccessControlApplication.mApp.canConnect())
                {
                    MqttAsks.sendDeviceInfo(AccessControlApplication.mApp,AccessControlApplication.mApp.aPublic);
                }
                break;
            case FINGER_SUCCESS:
                theActivity.mMainPresenter.accessSuccess((Intent) msg.obj);
                break;
            case FINGER_FAIL:
                theActivity.mMainPresenter.accessFail();
                break;
            case CLOSE_SUCCESS:
                theActivity.successView.hidView();
                break;
            case INIT_DEVICE:
                theActivity.mMainPresenter.checkInitDevice();
                break;
            case DeviceAsks.EVENT_GET_LIVE:
                theActivity.mMainPresenter.checkMaster((NetObject) msg.obj);
                break;
            case CHECK_CLEAN:
                theActivity.mMainPresenter.checkClean();
                break;
        }

    }
}
