package com.accesscontrol.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.accesscontrol.R;
import com.accesscontrol.asks.DeviceAsks;
import com.accesscontrol.prase.HttpPrase;
import com.accesscontrol.view.AccessControlApplication;
import com.accesscontrol.view.activity.SettingActivity;
import com.accesscontrol.view.activity.VideoActivity;

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
