package com.test;

import android.app.Application;
import intersky.appbase.AppActivityManager;

public class DkPadApplication extends Application {

    public AppActivityManager appActivityManager;
    public static DkPadApplication mApp;

    public void onCreate() {
        mApp = this;
        appActivityManager = AppActivityManager.getAppActivityManager(mApp);
        super.onCreate();
    }


}
