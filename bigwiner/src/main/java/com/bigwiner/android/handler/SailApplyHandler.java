package com.bigwiner.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.SailAsks;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.SailActivity;
import com.bigwiner.android.view.activity.SailApplyActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//08

public class SailApplyHandler extends Handler {

    public SailApplyActivity theActivity;
    public SailApplyHandler(SailApplyActivity mSailApplyActivity) {
        theActivity = mSailApplyActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case SailAsks.SAIL_APPLY_RESULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj))
                {
                    BigwinerApplication.mApp.mAccount.issail = true;
                    theActivity.finish();
                    Intent intent = new Intent(SailActivity.ACTION_SAIL_APPLY_SUCCESS);
                    intent.setPackage(theActivity.getPackageName());
                    theActivity.sendBroadcast(intent);
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
