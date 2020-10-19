package com.exhibition.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.exhibition.view.ExhibitionApplication;
import com.exhibition.view.activity.MainActivity;

//01


public class MainHandler extends Handler {

    public static final int EVENT_SET_NAME = 15001;
    public MainActivity theActivity;
    public MainHandler(MainActivity mMainActivity) {
        theActivity = mMainActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case EVENT_SET_NAME:
                theActivity.mMainPresenter.updataName();
                break;
        }

    }
}
