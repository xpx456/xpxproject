package com.bigwiner.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ContactDetialActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//09

public class ContactsDetialHandler extends Handler {

    public ContactDetialActivity theActivity;
    public ContactsDetialHandler(ContactDetialActivity mContactsDetialActivity) {
        theActivity = mContactsDetialActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case ContactsAsks.CONTACTS_DETIAL_RESULT:
                theActivity.waitDialog.hide();
                ContactsPrase.praseContactDetial(theActivity, (NetObject) msg.obj);
                theActivity.mContactDetialPresenter.initData();
                break;
            case ContactsAsks.CONTACTS_ADD_RESULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj))
                {
                    theActivity.contacts.isadd = true;
                    theActivity.mContactDetialPresenter.initData();
                    Intent intent = new Intent(ContactsAsks.ACTION_FRIEND_CHANGE);
                    intent.putExtra("contacts",theActivity.contacts);
                    intent.putExtra("add",true);
                    intent.setPackage(theActivity.getPackageName());
                    theActivity.sendBroadcast(intent);
                    AppUtils.showMessage(theActivity,theActivity.getString(R.string.contacts_add_success));
                }
                break;
            case ContactsAsks.CONTACTS_DEL_RESULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj))
                {
                    theActivity.contacts.isadd = false;
                    theActivity.mContactDetialPresenter.initData();
                    Intent intent = new Intent(ContactsAsks.ACTION_FRIEND_CHANGE);
                    intent.putExtra("contacts",theActivity.contacts);
                    intent.putExtra("add",false);
                    intent.setPackage(theActivity.getPackageName());
                    theActivity.sendBroadcast(intent);
                    AppUtils.showMessage(theActivity,theActivity.getString(R.string.contacts_del_success));
                }
                break;
            case NetUtils.NO_INTERFACE:
                theActivity.waitDialog.hide();
                AppUtils.showMessage(theActivity,theActivity.getString(R.string.error_net_network));
                break;
            case LoginAsks.CONFIRM_RESULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj)) {
//                    AppUtils.showMessage(theActivity,theActivity.getString(R.string.csetting_upload_success));
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
