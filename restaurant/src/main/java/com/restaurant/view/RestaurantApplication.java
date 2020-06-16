package com.restaurant.view;

import android.app.Application;
import android.content.SharedPreferences;

import com.restaurant.service.MyMqttService;

import intersky.appbase.AppActivityManager;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetUtils;

public class RestaurantApplication extends Application {


    public AppActivityManager appActivityManager;
    public NetUtils netUtils;
    public MyMqttService myMqttService;
    public static RestaurantApplication mApp;
    public String clidenid;
    public void onCreate() {
        mApp = this;
        appActivityManager = AppActivityManager.getAppActivityManager(mApp);
        netUtils = NetUtils.init(mApp);
        getClientid();
        super.onCreate();
    }

    public void getClientid() {
        SharedPreferences sharedPre = mApp.getSharedPreferences("AppDate", 0);
        clidenid = sharedPre.getString("clidenid", AppUtils.getguid());
        SharedPreferences.Editor e = sharedPre.edit();
        e.putString("clidenid",clidenid);
        e.apply();
    }

}
