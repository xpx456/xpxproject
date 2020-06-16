package com.bigwiner.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.entity.Company;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.prase.DetialPrase;
import com.bigwiner.android.prase.LoginPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.CompanyInfoActivity;

import java.io.File;

import intersky.apputils.AppUtils;
import intersky.apputils.SystemUtil;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//08

public class CompanyInfoHandler extends Handler {

    public static final int EVENT_SET_CIYT = 3010002;
    public static final int EVENT_SET_AREA = 3010003;
    public CompanyInfoActivity theActivity;

    public CompanyInfoHandler(CompanyInfoActivity mCompanyInfoActivity) {
        theActivity = mCompanyInfoActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case EVENT_SET_CIYT:
                theActivity.mCompanyInfoPresenter.setCity((Intent) msg.obj);
                break;
            case EVENT_SET_AREA:
                theActivity.mCompanyInfoPresenter.setProvience((Intent) msg.obj);
                break;
            case LoginAsks.UPLOAD_HEAD_C_RESULT:
                theActivity.waitDialog.hide();
                String headid = LoginPrase.companyHUpload(theActivity, (NetObject) msg.obj);
                if (headid.length() > 0) {
//                    BigwinerApplication.mApp.setContactHead(theActivity, new File(theActivity.logoid), theActivity.logo);
                    Company company =(Company) ((NetObject) msg.obj).item;
                    if (!company.bg.equals(theActivity.bgid)) {
                        theActivity.waitDialog.show();
                        LoginAsks.setUploadCompanyBg(theActivity, theActivity.mCompanyInfoPresenter.mCompanyInfoHandler, new File(theActivity.bgid), company);
                    }
                    else
                    {
                        theActivity.mCompanyInfoPresenter.doSubmit(company);
                    }
                }
                break;
            case LoginAsks.UPLOAD_BG_C_RESULT:
                theActivity.waitDialog.hide();
                String bgid = LoginPrase.companyBUpload(theActivity, (NetObject) msg.obj);
                if (bgid.length() > 0) {
//                    BigwinerApplication.mApp.setContactBg(theActivity, new File(theActivity.logoid), theActivity.logo);
                    theActivity.mCompanyInfoPresenter.doSubmit((Company) ((NetObject) msg.obj).item);
                }
                break;
            case LoginAsks.C_EDIT_RESULT:
                theActivity.waitDialog.hide();
                if (LoginPrase.praseUserCompany(theActivity, (NetObject) msg.obj)) {
                    Intent intent = new Intent(MainActivity.ACTION_UPDATE_MY);
                    intent.setPackage(theActivity.getPackageName());
                    theActivity.sendBroadcast(intent);
                    theActivity.finish();
                }
                break;
            case NetUtils.NO_INTERFACE:
                theActivity.waitDialog.hide();
                AppUtils.showMessage(theActivity, theActivity.getString(R.string.error_net_network));
                break;
            case ContactsAsks.COMPANY_DETIAL_RESULT:
                theActivity.waitDialog.hide();
                ContactsPrase.praseCompany(theActivity, (NetObject) msg.obj);
                theActivity.mCompanyInfoPresenter.initData();
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
