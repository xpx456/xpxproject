package com.intersky.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.intersky.android.tools.IatHelper;

import intersky.appbase.PermissionCode;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.conversation.BigWinerConversationManager;
import intersky.mywidget.CustomScrollView;
import intersky.xpxnet.net.NetUtils;

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
