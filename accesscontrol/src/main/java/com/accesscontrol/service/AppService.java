package com.accesscontrol.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.Nullable;

import com.accesscontrol.handler.MqttHandler;
import com.accesscontrol.receiver.MqttReceiver;
import com.accesscontrol.view.activity.MainActivity;

import intersky.apputils.AppUtils;


public class AppService extends Service {

    public static final int CHECK_APP = 1010;
    public AppServiceHandler handler = new AppServiceHandler();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkAPP();
        return START_STICKY_COMPATIBILITY;
    }




    public class AppServiceHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case CHECK_APP:
                    checkAPP();
                    break;
            }
        }
    }

    public void checkAPP()
    {
        if(AppUtils.isAppAlive(this,"com.accesscontrol") == false)
        {
            //this.stopSelf();
            Intent start = new Intent(this, MainActivity.class).
                    setAction(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(start);

        }
        if(handler != null)
        handler.sendEmptyMessageDelayed(CHECK_APP,1000);
    }

    @Override
    public void onDestroy() {
        Intent localIntent = new Intent();
        localIntent.setClass(this, AppService.class); // 销毁时重新启动Service
        this.startService(localIntent);
        super.onDestroy();
    }

    public class myReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            context.startService(new Intent(context, AppService.class));
        }
    }
}
