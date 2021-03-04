package com.bigwiner.android.handler;

import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.AttdenceActivity;
import com.bigwiner.android.view.activity.AttdencePersonActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//08

public class AttdencePersonHandler extends Handler {

    public AttdencePersonActivity theActivity;
    public AttdencePersonHandler(AttdencePersonActivity mAttdencePersonActivity) {
        theActivity = mAttdencePersonActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case ContactsAsks.FRIEND_LIST_RESULT:
                theActivity.waitDialog.hide();
                ContactsPrase.praseFriendList(theActivity, (NetObject) msg.obj);
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
