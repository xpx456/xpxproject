package com.accessmaster.handler;

import android.os.Handler;
import android.os.Message;

import com.accessmaster.asks.DeviceAsks;
import com.accessmaster.prase.HttpPrase;
import com.accessmaster.view.activity.SettingActivity;

import intersky.xpxnet.net.NetObject;


//01


public class SettingHandler extends Handler {


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
        }

    }
}
