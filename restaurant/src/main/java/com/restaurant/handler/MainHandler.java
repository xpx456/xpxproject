package com.restaurant.handler;

import android.os.Handler;
import android.os.Message;

import com.restaurant.view.activity.MainActivity;

//01


public class MainHandler extends Handler {


    public MainActivity theActivity;
    public static final int UPDATA_TIME = 100000;
    public static final int CLOSE_SUCCESS = 100001;
    public MainHandler(MainActivity mLoginActivity) {
        theActivity = mLoginActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case UPDATA_TIME:
                theActivity.mMainPresenter.updataTime();
                break;
            case CLOSE_SUCCESS:
                theActivity.mMainPresenter.closeSuccess();
                break;
        }

    }
}
