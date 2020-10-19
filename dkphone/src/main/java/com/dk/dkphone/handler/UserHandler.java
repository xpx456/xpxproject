package com.dk.dkphone.handler;

import android.os.Handler;
import android.os.Message;

import com.dk.dkphone.view.activity.UserActivity;


//01


public class UserHandler extends Handler {

    public static final int UPDATA_WEIGHT_CHART = 230001;

    public UserActivity theActivity;
    public UserHandler(UserActivity userActivity) {
        theActivity = userActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case UPDATA_WEIGHT_CHART:
                theActivity.mUserPresenter.updataChart();
                break;

        }

    }
}
