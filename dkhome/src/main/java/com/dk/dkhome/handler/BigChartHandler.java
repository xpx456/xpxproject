package com.dk.dkhome.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.dk.dkhome.R;
import com.dk.dkhome.view.DkhomeApplication;
import com.dk.dkhome.view.ProgressView;
import com.dk.dkhome.view.activity.BigChartActivity;
import com.dk.dkhome.view.activity.DeviceActivity;

import intersky.appbase.PermissionCode;


//01


public class BigChartHandler extends Handler {


    public BigChartActivity theActivity;
    public static final int UP_DATA_VIEW = 100402;
    public BigChartHandler(BigChartActivity deviceActivity) {
        theActivity = deviceActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {

            case UP_DATA_VIEW:
                theActivity.mBigChartPresenter.updataView((Intent) msg.obj);
                break;
        }

    }
}
