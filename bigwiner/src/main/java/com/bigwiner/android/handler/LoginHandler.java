package com.bigwiner.android.handler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.entity.UserDefine;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.prase.LoginPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.LoginActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bumptech.glide.Glide;

import intersky.apputils.AppUtils;
import intersky.chat.ChatUtils;
import intersky.conversation.BigWinerConversationManager;
import intersky.conversation.database.BigWinerDBHelper;
import intersky.filetools.FileUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//01


public class LoginHandler extends Handler {


    public LoginActivity theActivity;
    public static final int EVENT_START_MAIN = 3000200;
    public static final int EVENT_START_RELOGIN = 3000201;
    public static final int EVENT_SET_AREA_CODE= 3000202;
    public LoginHandler(LoginActivity mLoginActivity) {
        theActivity = mLoginActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case LoginAsks.LOGIN_RESULT:
                theActivity.waitDialog.hide();
                if(LoginPrase.praseLogin(theActivity, (NetObject) msg.obj))
                {
                    BigwinerApplication.mApp.contactManager.updataKeyEX(BigwinerApplication.mApp);
                    BigwinerApplication.mApp.setNewModules();
                    LoginAsks.doPushlogin(theActivity.mLoginPresenter.mLoginHandler,theActivity);
                    ContactsAsks.getFriendList(theActivity,theActivity.mLoginPresenter.mLoginHandler,BigwinerApplication.mApp.contactManager.friendPage.pagesize, BigwinerApplication.mApp.contactManager.friendPage.currentpage);
                }
                break;
            case ContactsAsks.COMPANY_DETIAL_RESULT:
                theActivity.waitDialog.hide();
                ContactsPrase.praseCompany(theActivity, (NetObject) msg.obj);
                break;
            case LoginAsks.PUSH_LOGIN_AGAIN:
                theActivity.waitDialog.hide();
                LoginAsks.doPushlogin(theActivity.mLoginPresenter.mLoginHandler,theActivity);
                break;
            case EVENT_START_MAIN:
                theActivity.phoneNumber.setText(BigwinerApplication.mApp.mAccount.mUserName);
                theActivity.passWord.setText(BigwinerApplication.mApp.mAccount.mPassword);
                theActivity.mLoginPresenter.doLogin();
                break;
            case EVENT_START_RELOGIN:
                theActivity.phoneNumber.setText(BigwinerApplication.mApp.mAccount.mUserName);
                theActivity.passWord.setText("");
                SharedPreferences info = theActivity.getSharedPreferences(BigwinerApplication.mApp.mAccount.mUserName, 0);
                SharedPreferences.Editor e1 = info.edit();
                e1.putString(UserDefine.USER_PASSWORD, "");
                e1.commit();
                break;
            case NetUtils.NO_INTERFACE:
            case NetUtils.NO_NET_WORK:
                theActivity.waitDialog.hide();
                AppUtils.showMessage(theActivity,theActivity.getString(R.string.error_net_network));
                break;
            case MainHandler.BACK_TIME_UPDATE:
                theActivity.backflag = false;
                break;
            case ContactsAsks.FRIEND_LIST_RESULT:
                ContactsPrase.praseFriendList(theActivity, (NetObject) msg.obj);
                if(BigwinerApplication.mApp.contactManager.friendPage.currentszie < BigwinerApplication.mApp.contactManager.friendPage.totleszie)
                {
                    BigwinerApplication.mApp.contactManager.friendPage.currentpage++;
                    ContactsAsks.getFriendList(theActivity,theActivity.mLoginPresenter.mLoginHandler,BigwinerApplication.mApp.contactManager.friendPage.pagesize, BigwinerApplication.mApp.contactManager.friendPage.currentpage);
                }
                else
                {
                    theActivity.waitDialog.hide();
                    theActivity.mLoginPresenter.startMain();
                }
                break;
            case EVENT_SET_AREA_CODE:
                theActivity.mLoginPresenter.setArecode((Intent) msg.obj);
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
