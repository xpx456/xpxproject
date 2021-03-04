package com.intersky.strang.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.intersky.strang.android.tools.IatHelper;

import intersky.appbase.PermissionCode;

public class IfHandler extends Handler {

    public IatHelper iatHelper;

    public IfHandler(IatHelper iatHelper) {
        this.iatHelper = iatHelper;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case PermissionCode.PERMISSION_REQUEST_AUDIORECORD:
                iatHelper.start();
                break;

        }

    }

}
