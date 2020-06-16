package com.restaurant.view;

import android.app.Application;

import intersky.appbase.AppActivityManager;

public class RestaurantApplication extends Application {


    public AppActivityManager appActivityManager;
    public static RestaurantApplication mApp;
    public void onCreate() {
        mApp = this;
        appActivityManager = AppActivityManager.getAppActivityManager(mApp);
        super.onCreate();
    }


}
