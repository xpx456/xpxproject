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
import com.bigwiner.android.view.activity.ScanResultActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//08

public class ScanResultHandler extends Handler {

    public ScanResultActivity theActivity;
    public ScanResultHandler(ScanResultActivity mScanResultActivity) {
        theActivity = mScanResultActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case ContactsAsks.CONTACTS_SCAN_RESULT:
                theActivity.waitDialog.hide();
                theActivity.imf.setText(ContactsPrase.praseDataImf(theActivity, (NetObject) msg.obj));
                theActivity.finish();
                break;
            case NetUtils.NO_INTERFACE:
                theActivity.waitDialog.hide();
                AppUtils.showMessage(theActivity,theActivity.getString(R.string.error_net_network));
                break;
        }

    }
}
