package com.restaurant.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;


import com.restaurant.R;
import com.restaurant.asks.DeviceAsks;
import com.restaurant.prase.HttpPrase;
import com.restaurant.view.activity.SettingActivity;

import intersky.xpxnet.net.NetObject;


//01


public class SettingHandler extends Handler {

    public static final int SHOW_MEMBER = 100123;
    public static final int SET_IP = 100124;
    public SettingActivity theActivity;
    public SettingHandler(SettingActivity settingActivity) {
        theActivity = settingActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case DeviceAsks.EVENT_GET_DEVICE_SUCCESS:
                theActivity.mSettingPresenter.updataView(HttpPrase.praseDevice((NetObject) msg.obj,theActivity));
                break;
            case SHOW_MEMBER:
                theActivity.queryView.creatView(theActivity.findViewById(R.id.setting));
                break;
            case SET_IP:
                theActivity.mSettingPresenter.setGetip((Intent) msg.obj);
                break;
        }

    }
}
