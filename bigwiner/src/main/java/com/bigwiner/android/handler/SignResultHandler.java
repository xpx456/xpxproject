package com.bigwiner.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.prase.DetialPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.SignResultActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//08

public class SignResultHandler extends Handler {

    public SignResultActivity theActivity;
    public SignResultHandler(SignResultActivity mSignResultActivity) {
        theActivity = mSignResultActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case DetialAsks.MEETING_SIGN_RESULT:
                theActivity.waitDialog.hide();
                String count = DetialPrase.praseData(theActivity, (NetObject) msg.obj);
                if(count.length() > 0)
                {
                    NetObject object = (NetObject) msg.obj;
                    String id = (String) object.item;
                    Intent intent = new Intent(DetialAsks.ACTION_MEETING_SIGN_SUCCESS);
                    intent.putExtra("id",id);
                    intent.setPackage(theActivity.getPackageName());
                    theActivity.sendBroadcast(intent);
                    theActivity.count.setText(count);
                    theActivity.icon.setVisibility(View.VISIBLE);
                    theActivity.txt1.setVisibility(View.VISIBLE);
                    theActivity.txt3.setVisibility(View.VISIBLE);
                    theActivity.imf.setVisibility(View.VISIBLE);
                    theActivity.title.setText(theActivity.getString(R.string.attdence_success));
                }
                else
                {
                    theActivity.imf.setText(theActivity.getString(R.string.meeting_try_again));
                    theActivity.title.setText(theActivity.getString(R.string.meeting_sign_fail));
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
