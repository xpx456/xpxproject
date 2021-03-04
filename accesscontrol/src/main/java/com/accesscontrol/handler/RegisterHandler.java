package com.accesscontrol.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.accesscontrol.asks.DeviceAsks;
import com.accesscontrol.prase.HttpPrase;
import com.accesscontrol.view.activity.RegisterActivity;

import intersky.xpxnet.net.NetObject;


//01


public class RegisterHandler extends Handler {

    public static final int EVENT_SET_ADDRESS = 230001;

    public RegisterActivity theActivity;
    public RegisterHandler(RegisterActivity registerActivity) {
        theActivity = registerActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {

            case DeviceAsks.EVENT_REGISTER_SUCCESS:
                theActivity.mRegisterPresenter.updataView(HttpPrase.praseDevice((NetObject) msg.obj,theActivity));
                break;
            case EVENT_SET_ADDRESS:
                theActivity.mRegisterPresenter.setLocation((Intent) msg.obj);
                break;
            case SettingHandler.SET_IP:
                theActivity.mRegisterPresenter.setGetip((Intent) msg.obj);
                break;
        }

    }
}
