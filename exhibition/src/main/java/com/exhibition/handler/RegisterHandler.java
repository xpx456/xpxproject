package com.exhibition.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.exhibition.view.activity.MainActivity;
import com.exhibition.view.activity.RegisterActivity;

//01


public class RegisterHandler extends Handler {

    public static final int EVENT_GET_FINGER = 15000;
    public RegisterActivity theActivity;
    public RegisterHandler(RegisterActivity registerActivity) {
        theActivity = registerActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case EVENT_GET_FINGER:
                theActivity.mRegisterPresenter.addFinger((Intent) msg.obj);
                break;
        }

    }
}
