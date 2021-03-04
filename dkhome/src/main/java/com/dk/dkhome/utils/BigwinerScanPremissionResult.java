package com.dk.dkhome.utils;

import android.app.Activity;
import android.content.pm.PackageManager;

import com.dk.dkhome.view.DkhomeApplication;

import intersky.appbase.PermissionCode;
import intersky.appbase.PermissionResult;
import intersky.apputils.AppUtils;

public class BigwinerScanPremissionResult implements PermissionResult {


    public Activity context;
    public String className = "";
    public String title = "";
    public BigwinerScanPremissionResult(Activity context, String className, String title) {
        this.context = context;
        this.className = className;
        this.title = title;
    }


    @Override
    public void doResult(int requestCode ,int[] grantResults) {
        switch (requestCode) {
            case PermissionCode.PERMISSION_REQUEST_CAMERA_CAMERA:
                if(grantResults.length > 0)
                {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // MyPermission Granted
                        if(className.length() > 0)
                            DkhomeApplication.mApp.startScan(context,className,title);
                        else
                            DkhomeApplication.mApp.startScan(context,className,title);
                    }
                    else
                    {
                        AppUtils.showMessage(context,context.getString(intersky.scan.R.string.promiss_error_camera));
                    }
                }
                else
                {
                    AppUtils.showMessage(context,context.getString(intersky.scan.R.string.promiss_error_camera));
                }
                break;
        }
    }


}
