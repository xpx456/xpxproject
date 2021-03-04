package com.exhibition.handler;

import android.os.Handler;
import android.os.Message;

import com.exhibition.view.activity.AboutActivity;
import com.exhibition.view.activity.MainActivity;

//01


public class AboutHandler extends Handler {

    public static final int HIDE_OPER = 16001;
    public AboutActivity theActivity;
    public AboutHandler(AboutActivity mAboutActivity) {
        theActivity = mAboutActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case HIDE_OPER:
                theActivity.mAboutPresenter.hideOper();
                break;
        }

    }
}
