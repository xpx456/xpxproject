package com.bigwiner.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.AttdenceActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//08

public class AttdenceHandler extends Handler {

    public static final int UPDTATA_SOURCE = 3004500;
    public AttdenceActivity theActivity;
    public AttdenceHandler(AttdenceActivity mAttdenceActivity) {
        theActivity = mAttdenceActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case DetialAsks.MEETING_JOIN_RESULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj))
                {
                    theActivity.meeting.isjionin = true;
                    Intent intent = new Intent(DetialAsks.ACTION_MEETING_JOIN_SUCCESS);
                    intent.putExtra("id",theActivity.meeting.recordid);
                    intent.putExtra("meeting",theActivity.meeting);
                    intent.setPackage(theActivity.getPackageName());
                    theActivity.sendBroadcast(intent);
                    theActivity.finish();
                    AppUtils.showMessage(theActivity,theActivity.getString(R.string.join_success));
                }
                break;
            case UPDTATA_SOURCE:
                theActivity.mAttdencePresenter.measureJoinData();
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
