package com.exhibition.handler;

import android.content.Intent;
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
    public static final int GET_FINGER_IMG_SUCCESS = 100002;
    public static final int GET_LOGIN_SUCCESS = 100003;
    public static final int GET_LOGIN_FAIL = 100004;
    public static final int INIT_DATA = 100005;
    public LoginHandler(LoginActivity mLoginActivity) {
        theActivity = mLoginActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case UPDATA_TIME:
                theActivity.lastSecond.setText(String.valueOf(ExhibitionApplication.mApp.timeoud));
                break;
            case GET_FINGER_IMG_SUCCESS:
                theActivity.imageView.setImageBitmap(ExhibitionApplication.mApp.fingerManger.lastgetFinger.sampleimg.get(0));
                break;
            case GET_LOGIN_SUCCESS:
                theActivity.mLoginPresenter.praseLoginImf((Intent) msg.obj);
                break;
            case GET_LOGIN_FAIL:
                theActivity.mLoginPresenter.praseLoginImf((Intent) msg.obj);
                break;
            case INIT_DATA:
                ExhibitionApplication.mApp.initData((Intent) msg.obj);
                break;
        }

    }
}
