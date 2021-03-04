package com.screen;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;

public class ScreenManager {

    public static volatile ScreenManager screenManager;
    public Context context;
    public Display display;

    public static ScreenManager init(Context context) {
        if (screenManager == null) {
            synchronized (ScreenManager.class) {
                if (screenManager == null) {
                    screenManager = new ScreenManager();
                    screenManager.context = context;

                } else {
                    screenManager.context = context;
                }
            }
        }
        return screenManager;
    }


}
