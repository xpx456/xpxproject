package com.interskypad.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.interskypad.asks.LoginAsks;
import com.interskypad.prase.LoginPrase;
import com.interskypad.view.InterskyPadApplication;
import com.interskypad.view.activity.LoginActivity;

import java.lang.ref.WeakReference;

import intersky.xpxnet.net.NetObject;

public class LoginHandler extends Handler {

    public static final int UPDATA_SNIPER = 1030001;
    public static final int DELETE_SNIPER = 1030002;
    public static final int START_MAIN = 1030003;
    public static final int LOGIN_OUT = 1030005;
    public static final int PUSH_LOGIN_AGAIN = 1030006;

    public LoginActivity theActivity;

    public LoginHandler(LoginActivity mLoginActivity) {
        theActivity = mLoginActivity;
    }


    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case LoginAsks.LOGIN_SUCCESS:
                theActivity.waitDialog.hide();
                LoginPrase.praseLogin(theActivity, (NetObject) msg.obj, theActivity.mLoginPresenter.mLoginHandler);
                break;
            case LoginAsks.LOGIN_FAIL:
                theActivity.mLoginPresenter.loginFail((String) msg.obj);
                break;
            case START_MAIN:
                theActivity.mLoginPresenter.startMain();
                break;
            case LoginAsks.GET_ADDRESS_SUCCESS:
                if (((String) msg.obj).length() == 0) {
                    theActivity.waitDialog.hide();
//                    ViewUtils.showMessage(theActivity,theActivity.getString(R.string.error_msg_ip));
                } else {
                    InterskyPadApplication.mApp.mService.sAddress = (String) msg.obj;
                    theActivity.mLoginPresenter.doLogin();
                }
                theActivity.mLoginPresenter.doLogin();
                break;
            case LoginAsks.GET_ADDRESS_FAIL:
                break;
            case UPDATA_SNIPER:
                theActivity.mLoginPresenter.upDataList((Intent) msg.obj);
                break;
            case DELETE_SNIPER:
                theActivity.mLoginPresenter.deleteList((Intent) msg.obj);
                break;
            case LOGIN_OUT:
                theActivity.waitDialog.hide();
                theActivity.eTxtPassword.setText("");
                break;
        }
    }
}
