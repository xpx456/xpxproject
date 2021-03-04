package com.bigwiner.android.handler;

import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.prase.ConversationPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.SourceSelectActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//08

public class SourceSelectHandler extends Handler {

    public SourceSelectActivity theActivity;
    public SourceSelectHandler(SourceSelectActivity mSourceSelectActivity) {
        theActivity = mSourceSelectActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case ConversationAsks.SOURCE_RESULT:
                theActivity.waitDialog.hide();
//                ConversationPrase.praseSourceData(theActivity, (NetObject) msg.obj);
                break;
            case ConversationAsks.BASE_DATA_RESULT:
                theActivity.waitDialog.hide();
                ConversationPrase.praseBaseData(theActivity, (NetObject) msg.obj);
//                ConversationPrase.praseSourceData(theActivity, (NetObject) msg.obj);
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
