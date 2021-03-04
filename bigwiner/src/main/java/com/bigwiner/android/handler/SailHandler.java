package com.bigwiner.android.handler;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.SailActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//08

public class SailHandler extends Handler {

    public static final int SAIL_SUCCESS = 302500;

    public SailActivity theActivity;
    public SailHandler(SailActivity mSailActivity) {
        theActivity = mSailActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case SAIL_SUCCESS:
                if(BigwinerApplication.mApp.mAccount.issail)
                {
                    theActivity.btnApply.setVisibility(View.INVISIBLE);
                }
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
