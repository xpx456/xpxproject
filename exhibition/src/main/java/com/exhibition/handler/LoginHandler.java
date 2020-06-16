package com.exhibition.handler;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;

import com.exhibition.view.ExhibitionApplication;
import com.exhibition.view.activity.LoginActivity;
import intersky.xpxnet.net.NetObject;

//01


public class LoginHandler extends Handler {


    public LoginActivity theActivity;
    public static final int UPDATA_TIME = 100001;
    public LoginHandler(LoginActivity mLoginActivity) {
        theActivity = mLoginActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case UPDATA_TIME:
                theActivity.lastSecond.setText(String.valueOf(ExhibitionApplication.mApp.timeoud));
                break;
        }

    }
}
