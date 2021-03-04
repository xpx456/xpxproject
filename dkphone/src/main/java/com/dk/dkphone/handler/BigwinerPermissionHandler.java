package com.dk.dkphone.handler;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import com.dk.dkphone.view.DkPhoneApplication;

import intersky.appbase.PermissionCode;

public class BigwinerPermissionHandler extends Handler {



    public Activity activity;
    public String className;
    public String title;

    public BigwinerPermissionHandler(Activity activity, String className, String title) {
        this.activity = activity;
        this.className = className;
        this.title = title;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case PermissionCode.PERMISSION_REQUEST_CAMERA_CAMERA:
                DkPhoneApplication.mApp.startScan(activity,  className,this.title);
                break;
        }

    }
}
