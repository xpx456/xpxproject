package com.bigwiner.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.view.BigwinerApplication;

import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class ContactsHeadHandler extends Handler {


    public final static int DOWNLOAD_FIAL = 1230600;
    public final static int DOWNLOAD_UPDATA = 1230601;
    public final static int DOWNLOAD_FINISH = 1230602;
    public final static int ADD_MESSAGE = 1230603;
    public final static int SET_PIC = 1230604;


    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case ContactsAsks.CONTACTS_DETIAL_RESULT:
                if(ContactsPrase.praseContactDetial((NetObject)msg.obj))
                {

                }
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
