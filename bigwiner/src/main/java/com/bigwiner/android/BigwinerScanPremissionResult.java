package com.bigwiner.android;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.entity.UserDefine;
import com.bigwiner.android.view.BigwinerApplication;

import intersky.appbase.PermissionCode;
import intersky.appbase.PermissionResult;
import intersky.apputils.AppUtils;
import intersky.scan.ScanUtils;

public class BigwinerScanPremissionResult implements PermissionResult {


    public Activity context;
    public String className = "";
    public Meeting meeting;
    public String title = "";
    public BigwinerScanPremissionResult(Activity context, String className, Meeting meeting,String title) {
        this.context = context;
        this.className = className;
        this.meeting = meeting;
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
                            BigwinerApplication.mApp.startScan(context,className,meeting,title);
                        else
                            BigwinerApplication.mApp.startScan(context,className,meeting,title);
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
