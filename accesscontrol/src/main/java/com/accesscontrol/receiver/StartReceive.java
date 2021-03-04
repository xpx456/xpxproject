package com.accesscontrol.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.accesscontrol.view.AccessControlApplication;
import com.accesscontrol.view.activity.MainActivity;

import java.util.List;

public class StartReceive extends BroadcastReceiver {

    public String TAG = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean start = false;
        Intent mBootIntent = new Intent(context, MainActivity.class);
        // 下面这句话必须加上才能开机自动运行app的界面
        mBootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mBootIntent);
    }

}
