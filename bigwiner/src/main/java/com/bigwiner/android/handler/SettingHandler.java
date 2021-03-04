package com.bigwiner.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.AttdenceActivity;
import com.bigwiner.android.view.activity.SettingActivity;

import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//08

public class SettingHandler extends Handler {

    public SettingActivity theActivity;
    public SettingHandler(SettingActivity mSettingActivity) {
        theActivity = mSettingActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case LoginAsks.CONFIRM_RESULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj)) {
//                    AppUtils.showMessage(theActivity,theActivity.getString(R.string.csetting_upload_success));
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
