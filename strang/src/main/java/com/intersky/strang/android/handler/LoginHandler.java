package com.intersky.strang.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.intersky.strang.R;
import com.intersky.strang.android.asks.LoginAsks;
import com.intersky.strang.android.prase.LoginPrase;
import com.intersky.strang.android.view.StrangApplication;
import com.intersky.strang.android.view.activity.LoginActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

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
                LoginPrase.praseLogin(theActivity,(NetObject) msg.obj,theActivity.mLoginPresenter.mLoginHandler,StrangApplication.mApp.mAccount);
                break;
            case START_MAIN:
                LoginAsks.doPushlogin(theActivity.mLoginPresenter.mLoginHandler,theActivity);
                theActivity.mLoginPresenter.startMain();
                break;
            case LoginAsks.GET_ADDRESS_SUCCESS:
                NetObject netObject = (NetObject) msg.obj;
                if(netObject.result.length() == 0)
                {
                    theActivity.waitDialog.hide();
                    AppUtils.showMessage(theActivity,theActivity.getString(R.string.get_ip_error));
                }
                else
                {
                    StrangApplication.mApp.mService.sAddress = netObject.result;
                    theActivity.mLoginPresenter.doLogin();
                }
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
            case PUSH_LOGIN_AGAIN:
                LoginAsks.doPushlogin(theActivity.mLoginPresenter.mLoginHandler,theActivity);
                theActivity.waitDialog.hide();
                break;
            case NetUtils.NO_NET_WORK:
                NetObject netObject1 = (NetObject) msg.obj;
                String url = (String) netObject1.result;
                if(url.contains("api/v1/Ios"))
                {
                    AppUtils.showMessage(theActivity,"注册云推送失败");
                }
                else
                AppUtils.showMessage(theActivity,theActivity.getString(R.string.error_net_network));
                break;
        }

    }
}
