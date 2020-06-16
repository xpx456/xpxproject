package com.bigwiner.android.handler;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.prase.LoginPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.LoginActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.PasswordActivity;

import intersky.apputils.AppUtils;
import intersky.conversation.BigWinerConversationManager;
import intersky.conversation.database.BigWinerDBHelper;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//01


public class PasswordHandler extends Handler {


    public PasswordActivity theActivity;
    public PasswordHandler(PasswordActivity mLoginActivity) {
        theActivity = mLoginActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case LoginAsks.PASS_WORD_EDIT_RUSULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj))
                {
                    Intent intent10 = new Intent(MainActivity.ACTION_LOGIN_OUT);
                    intent10.setPackage(theActivity.getPackageName());
                    theActivity.sendBroadcast(intent10);
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
