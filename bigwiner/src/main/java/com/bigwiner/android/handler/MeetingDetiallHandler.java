package com.bigwiner.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.prase.DetialPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.MeetingDetialActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//07

public class MeetingDetiallHandler extends Handler {
    public static final int JOIN_SUCCESS = 303000;
    public static final int SIGN_SUCCESS = 303001;
    public MeetingDetialActivity theActivity;

    public MeetingDetiallHandler(MeetingDetialActivity mMeetingDetialActivity) {
        theActivity = mMeetingDetialActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case DetialAsks.MEETING_DETIAL_RESULT:
                theActivity.waitDialog.hide();
                if (DetialPrase.praseMeeting(theActivity, (NetObject) msg.obj)) {
                    theActivity.mMeetingDetialPresenter.initData();
                }
                break;
            case JOIN_SUCCESS:
                Intent intent = (Intent) msg.obj;
                if (intent.getStringExtra("id").equals(theActivity.meeting.recordid)) {
                    theActivity.meeting.isjionin = true;
                    theActivity.mMeetingDetialPresenter.initData();
                }
                break;
            case SIGN_SUCCESS:
                Intent intent1 = (Intent) msg.obj;
                if (intent1.getStringExtra("id").equals(theActivity.meeting.recordid)) {
                    theActivity.meeting.issign = true;
                    theActivity.mMeetingDetialPresenter.initData();
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
