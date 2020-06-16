package com.bigwiner.android.handler;

import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.WebMessageActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetUtils;


public class WebMessageHandler extends Handler {
    public WebMessageActivity theActivity;

    public WebMessageHandler(WebMessageActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case WebMessageActivity.EVENT_GET_INFO:
                    break;
                case NetUtils.NO_INTERFACE:
                    theActivity.waitDialog.hide();
                    AppUtils.showMessage(theActivity,theActivity.getString(R.string.error_net_network));
                    break;
                case NetUtils.TOKEN_ERROR:
                    if(BigwinerApplication.mApp.mAccount.islogin == true) {
                        BigwinerApplication.mApp.logout(BigwinerApplication.mApp.mAppHandler,BigwinerApplication.mApp.appActivityManager.getCurrentActivity());
                        NetUtils.getInstance().cleanTasks();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
