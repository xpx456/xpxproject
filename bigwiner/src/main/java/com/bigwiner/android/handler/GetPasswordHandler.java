package com.bigwiner.android.handler;

import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.GetPasswordActivity;
import com.bigwiner.android.view.activity.RegisterActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetUtils;

//02

public class GetPasswordHandler extends Handler {

    public GetPasswordActivity theActivity;
    public GetPasswordHandler(GetPasswordActivity mGetPasswordActivity) {
        theActivity = mGetPasswordActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case RegisterActivity.EVENT_UPDATA_CODE_SECOND:

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
