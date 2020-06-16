package com.bigwiner.android.handler;

import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.prase.ConversationPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.HistoryActivity;

import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//08

public class HistoryHandler extends Handler {

    public HistoryActivity theActivity;
    public static final int UPDATA_MAIN_FRIENDS = 300400;
    public HistoryHandler(HistoryActivity mHistoryActivity) {
        theActivity = mHistoryActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case ConversationAsks.HIS_RESULT:
                theActivity.waitDialog.hide();
                ConversationPrase.praseConversationHis(theActivity, (NetObject) msg.obj);
                int type = (int) ((NetObject) msg.obj).item;
                if(type == 1)
                {
                    theActivity.mConversationAdapter1.notifyDataSetChanged();
                }
                else
                {
                    theActivity.mConversationAdapter2.notifyDataSetChanged();
                }
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
