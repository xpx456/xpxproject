package com.dk.dkpad.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dk.dkpad.view.activity.SplashActivity;

public class StartReceive extends BroadcastReceiver {

    public String TAG = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent mBootIntent = new Intent(context, SplashActivity.class);
        // 下面这句话必须加上才能开机自动运行app的界面
        mBootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mBootIntent);
    }

}
