package com.bigwiner.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.CompanyDetialActivity;
import com.bigwiner.android.view.activity.ContactDetialActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//09

public class CompanyDetialHandler extends Handler {



    public CompanyDetialActivity theActivity;
    public CompanyDetialHandler(CompanyDetialActivity mCompanyDetialActivity) {
        theActivity = mCompanyDetialActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case ContactsAsks.COMPANY_DETIAL_RESULT:
                theActivity.waitDialog.hide();
                ContactsPrase.praseCompany(theActivity, (NetObject) msg.obj);
                theActivity.mCompanyDetialPresenter.initData();
                break;
            case ContactsAsks.COMPANY_CONTACTS_RESULT:
                theActivity.waitDialog.hide();
                ContactsPrase.praseCompanyMemberList(theActivity, (NetObject) msg.obj,theActivity.company);
                theActivity.contactsAdapter.notifyDataSetChanged();
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
