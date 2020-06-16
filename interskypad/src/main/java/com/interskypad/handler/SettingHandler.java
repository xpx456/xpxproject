package com.interskypad.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.interskypad.view.activity.SettingActivity;

import java.lang.ref.WeakReference;

public class SettingHandler extends Handler {

    public static final int LOGIN_OUT = 1040000;


    public SettingActivity mSettingActivity;

    public SettingHandler(SettingActivity mSettingActivity) {
        this.mSettingActivity = mSettingActivity;
    }


    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case LOGIN_OUT:
                mSettingActivity.waitDialog.hide();
                break;
        }
    }
}
