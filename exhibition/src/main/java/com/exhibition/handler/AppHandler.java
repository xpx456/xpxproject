package com.exhibition.handler;

import android.os.Handler;
import android.os.Message;

import com.exhibition.view.ExhibitionApplication;

//04

public class AppHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case ExhibitionApplication.CHECK_TIME_OUT:
                ExhibitionApplication.mApp.checkTimeOut();
                break;
            case ExhibitionApplication.SET_TIME_MAX:
                ExhibitionApplication.mApp.timeoud = ExhibitionApplication.TIME_MAX;
                break;
            case ExhibitionApplication.SET_VIDEO_SHOW:
                ExhibitionApplication.mApp.videoshow = true;
                break;
            case ExhibitionApplication.SET_VIDEO_HID:
                ExhibitionApplication.mApp.videoshow = false;
                break;
        }

    }
}
