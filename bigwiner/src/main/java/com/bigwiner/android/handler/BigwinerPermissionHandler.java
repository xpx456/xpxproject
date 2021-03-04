package com.bigwiner.android.handler;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.view.BigwinerApplication;

import intersky.appbase.PermissionCode;
import intersky.scan.ScanUtils;

public class BigwinerPermissionHandler extends Handler {



    public Activity activity;
    public String className;
    public Meeting meeting;
    public String title;

    public BigwinerPermissionHandler(Activity activity, String className,Meeting meeting,String title) {
        this.activity = activity;
        this.className = className;
        this.meeting = meeting;
        this.title = title;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case PermissionCode.PERMISSION_REQUEST_CAMERA_CAMERA:
                BigwinerApplication.mApp.startScan(activity,  className,meeting,this.title);
                break;
        }

    }
}
