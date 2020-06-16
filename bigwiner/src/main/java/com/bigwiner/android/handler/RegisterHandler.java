package com.bigwiner.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ForgetActivity;
import com.bigwiner.android.view.activity.LoginActivity;
import com.bigwiner.android.view.activity.RegisterActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//02

public class RegisterHandler extends Handler {

    public static final int EVENT_REGISTER_SUCCESS = 3000300;
    public static final int EVENT_SET_TYPE = 3000001;
    public static final int EVENT_SET_CIYT = 3000002;
    public static final int EVENT_SET_AREA= 3000003;
    public static final int EVENT_SET_AREA_CODE= 3000004;
    public RegisterActivity theActivity;

    public RegisterHandler(RegisterActivity mRegisterActivity) {
        theActivity = mRegisterActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case RegisterActivity.EVENT_UPDATA_CODE_SECOND:
                theActivity.mRegisterPresenter.updataTime();
                break;
            case LoginAsks.REGISTER_RESULT:
                theActivity.waitDialog.hide();
                if (ContactsPrase.praseData(theActivity, (NetObject) msg.obj)) {
                    if(theActivity.getIntent().getStringExtra("title").equals(theActivity.getString(R.string.btn_register)))
                    {
                        BigwinerApplication.mApp.mAccount.mUserName = theActivity.phoneNumber.getText().toString();
                        BigwinerApplication.mApp.mAccount.mMobile = theActivity.phoneNumber.getText().toString();
                        BigwinerApplication.mApp.mAccount.mPassword = theActivity.passWord.getText().toString();
                        Intent intent10 = new Intent(LoginActivity.ACTION_REGIST_SUCCESS);
                        intent10.setPackage(theActivity.getPackageName());
                        theActivity.sendBroadcast(intent10);

                    }
                    else
                    {
                        BigwinerApplication.mApp.mAccount.mUserName = theActivity.phoneNumber.getText().toString();
                        Intent intent = new Intent(LoginActivity.ACTION_CHANGE_SUCCESS);
                        intent.setPackage(theActivity.getPackageName());
                        theActivity.sendBroadcast(intent);
                    }
                    theActivity.finish();
                }
                break;
            case LoginAsks.CODE_RESULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj)) {
                    theActivity.mRegisterPresenter.startTime();
                }
                break;
            case NetUtils.NO_INTERFACE:
                theActivity.waitDialog.hide();
                AppUtils.showMessage(theActivity,theActivity.getString(R.string.error_net_network));
                break;
            case EVENT_SET_TYPE:
                theActivity.mRegisterPresenter.setType((Intent) msg.obj);
                break;
            case EVENT_SET_CIYT:
                theActivity.mRegisterPresenter.setCity((Intent) msg.obj);
                break;
            case EVENT_SET_AREA:
                theActivity.mRegisterPresenter.setArea((Intent) msg.obj);
                break;
            case EVENT_SET_AREA_CODE:
                theActivity.mRegisterPresenter.setArecode((Intent) msg.obj);
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
