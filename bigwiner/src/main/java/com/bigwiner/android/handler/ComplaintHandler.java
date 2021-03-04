package com.bigwiner.android.handler;

import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.SailAsks;
import com.bigwiner.android.prase.SailPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ComplaintActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//08

public class ComplaintHandler extends Handler {

    public ComplaintActivity theActivity;
    public ComplaintHandler(ComplaintActivity mComplaintActivity) {
        theActivity = mComplaintActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case SailAsks.SAIL_RED_RESULT:
                theActivity.waitDialog.hide();
                SailPrase.parasComplant(theActivity, (NetObject) msg.obj,theActivity.Complaints,theActivity.sail);
                theActivity.mComplaintAdapter.notifyDataSetChanged();
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
