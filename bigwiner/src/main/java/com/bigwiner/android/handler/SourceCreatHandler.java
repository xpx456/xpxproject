package com.bigwiner.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.asks.SourceAsks;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.prase.LoginPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.SourceCreatActivity;
import com.bigwiner.android.view.activity.UserInfoActivity;

import intersky.appbase.entity.Account;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//08

public class SourceCreatHandler extends Handler {

    public static final int EVENT_SET_AREA = 3010000;
    public static final int EVENT_SET_TYPE = 3010001;
    public static final int EVENT_SET_PORT = 3010002;
    public SourceCreatActivity theActivity;
    public SourceCreatHandler(SourceCreatActivity theActivity) {
        this.theActivity = theActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case EVENT_SET_TYPE:
                theActivity.mSourceCreatPresenter.setType((Intent) msg.obj);
                break;
            case EVENT_SET_PORT:
                theActivity.mSourceCreatPresenter.setPort((Intent) msg.obj);
                break;
            case EVENT_SET_AREA:
                theActivity.mSourceCreatPresenter.setArea((Intent) msg.obj);
                break;
            case SourceAsks.SOURCE_ADD_RESULT:
                theActivity.waitDialog.hide();
                NetObject netObject = (NetObject) msg.obj;
                SourceData sourceData = (SourceData) netObject.item;
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj))
                {
                    theActivity.finish();
                    Intent intent = new Intent(SourceAsks.ACIONT_SOURCE_CREAT);
                    intent.putExtra("source",sourceData);
                    intent.setPackage(theActivity.getPackageName());
                    theActivity.sendBroadcast(intent);
                }
                break;
            case SourceAsks.SOURCE_EDIT_RESULT:
                theActivity.waitDialog.hide();
                theActivity.iscreating = false;
                NetObject netObject1 = (NetObject) msg.obj;
                SourceData sourceData1 = (SourceData) netObject1.item;
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj))
                {
                    theActivity.finish();
                    Intent intent = new Intent(SourceAsks.ACIONT_SOURCE_EDIT);
                    intent.putExtra("source",sourceData1);
                    intent.setPackage(theActivity.getPackageName());
                    theActivity.sendBroadcast(intent);
                }
                break;
            case NetUtils.NO_INTERFACE:
            case NetUtils.NO_NET_WORK:
                theActivity.waitDialog.hide();
                theActivity.iscreating = false;
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
