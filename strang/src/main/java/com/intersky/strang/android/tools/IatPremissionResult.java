package com.intersky.strang.android.tools;

import android.app.Activity;
import android.content.pm.PackageManager;

import intersky.appbase.PermissionCode;
import intersky.appbase.PermissionResult;
import intersky.apputils.AppUtils;

public class IatPremissionResult implements PermissionResult {

    public Activity context;
    public IatHelper iatHelper;
    public IatPremissionResult( Activity context,IatHelper iatHelper) {
        this.context = context;
        this.iatHelper = iatHelper;
    }

    @Override
    public void doResult(int requestCode, int[] grantResults) {
        switch (requestCode) {
            case PermissionCode.PERMISSION_REQUEST_AUDIORECORD:
                if(grantResults.length > 0)
                {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        this.iatHelper.start();
                    }
                    else
                    {
                        AppUtils.showMessage(context,context.getString(intersky.talk.R.string.promiss_error_audio));
                    }
                }
                else
                {
                    AppUtils.showMessage(context,context.getString(intersky.talk.R.string.promiss_error_audio));
                }
                break;
        }
    }
}
