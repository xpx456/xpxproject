package com.bigwiner.android.handler;

import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.prase.LoginPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ForgetActivity;
import com.bigwiner.android.view.activity.LoginActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;


//03

public class ForgetHandler extends Handler {

    public ForgetActivity theActivity;
    public ForgetHandler(ForgetActivity mForgetActivity) {
        theActivity = mForgetActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case ForgetActivity.EVENT_UPDATA_CODE_SECOND:
                theActivity.mForgetPresenter.updataTime();
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

    }
}
