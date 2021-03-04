package com.intersky.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.intersky.android.tools.IatHelper;
import com.intersky.android.view.InterskyApplication;

import intersky.appbase.BaseActivity;
import intersky.appbase.PermissionCode;
import intersky.appbase.entity.ShareItem;

public class ShareHandler extends Handler {

    public static final int DOSHARE = 101011;
    public BaseActivity baseActivity;
    public ShareItem shareItem;



    public ShareHandler(BaseActivity baseActivity,ShareItem shareItem) {
        this.baseActivity = baseActivity;
        this.shareItem = shareItem;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case DOSHARE:
                InterskyApplication.mApp.share.isPremission = true;
                InterskyApplication.mApp.share.doShare(baseActivity,shareItem);
                break;

        }

    }


}
