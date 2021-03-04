package com.bigwiner.android.handler;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.SailActivity;
import com.bigwiner.android.view.activity.ShowCodeActivity;

import intersky.apputils.AppUtils;
import intersky.scan.QRCodeUtil;
import intersky.xpxnet.net.NetUtils;

//08

public class ShowCodeHandler extends Handler {

    public static final int SET_BITMAT = 302600;

    public ShowCodeActivity theActivity;
    public ShowCodeHandler(ShowCodeActivity mShowCodeActivity) {
        theActivity = mShowCodeActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case SET_BITMAT:
                theActivity.headimg.setImageBitmap((Bitmap) msg.obj);
                theActivity.code.setImageBitmap( QRCodeUtil.createQRCodeBitmap(ContactsAsks.codeUrl(BigwinerApplication.mApp.mAccount.mRecordId), (int) (theActivity.mBasePresenter.mScreenDefine.density*270), (Bitmap) msg.obj,0.2f));
                break;
        }

    }
}
