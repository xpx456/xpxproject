package intersky.appbase.utils;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {

    /**
     * 绯荤粺涓婁笅鏂?
     */
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();

    }

    /**
     * 鑾峰彇绯荤粺涓婁笅鏂囷細鐢ㄤ簬ToastUtil绫?
     */
    public static Context getAppContext() {
        return mAppContext;
    }

}
