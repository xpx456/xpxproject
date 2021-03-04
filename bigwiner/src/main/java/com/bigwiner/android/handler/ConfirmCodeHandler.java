package com.bigwiner.android.handler;

import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ConfirmCodeActivity;
import com.bigwiner.android.view.activity.RegisterActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetUtils;

//02

public class ConfirmCodeHandler extends Handler {

    public ConfirmCodeActivity theActivity;
    public ConfirmCodeHandler(ConfirmCodeActivity mRegisterActivity) {
        theActivity = mRegisterActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case RegisterActivity.EVENT_UPDATA_CODE_SECOND:
                theActivity.mConfirmCodePresenter.updataTime();
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
