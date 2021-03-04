package com.dk.dkhome.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.dk.dkhome.entity.Course;
import com.dk.dkhome.view.activity.ScheduleActivity;

import intersky.appbase.bus.Bus;
import intersky.apputils.TimeUtils;

import intersky.xpxnet.net.NetObject;

//01
public class ScheduleHandler extends Handler {

    public static final int UPDATA_EVENT = 3230101;

    public ScheduleActivity theActivity;

    public ScheduleHandler(ScheduleActivity mScheduleActivity) {
        theActivity = mScheduleActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case UPDATA_EVENT:
                theActivity.mSchedulePresenter.updataSelect();
                break;

        }

    }

}
