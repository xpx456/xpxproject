package intersky.appbase;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.Stack;

import static android.content.Context.ACTIVITY_SERVICE;

public class AppActivityManager {

    public static Stack<Activity> activityStack;
    public ScreenDefine mScreenDefine;
    public boolean isActivity = false;
    private Context context;
    private volatile static AppActivityManager appActivityManager = null;


    private AppActivityManager(Context context){
        this.context = context;
        mScreenDefine = new ScreenDefine(context.getApplicationContext());
    }
    /**
     * 鍗曚竴瀹炰緥
     */
    public static AppActivityManager getAppActivityManager(Context context){
        if (appActivityManager == null) {
            synchronized (AppActivityManager.class) {
                if (appActivityManager == null) {
                    appActivityManager = new AppActivityManager(context);
                }
                else
                {
                    appActivityManager.context = context;
                }
            }
        }
        return appActivityManager;
    }

    public static AppActivityManager getInstance() {
        return appActivityManager;
    }
    /**
     * 娣诲姞Activity鍒板爢鏍?
     */
    public void addActivity(Activity activity){
        if(activityStack==null){
            activityStack=new Stack<Activity>();
        }
        activityStack.add(activity);
    }
    /**
     * 鑾峰彇褰撳墠Activity锛堝爢鏍堜腑鏈€鍚庝竴涓帇鍏ョ殑锛?
     */
    public Activity getCurrentActivity(){
        Activity activity=activityStack.lastElement();
        return activity;
    }

    /**
     * 鑾峰彇鎸囧畾绫诲悕鐨凙ctivity
     */
    public Activity getActivity(Class<?> cls){
        for (Activity activity : activityStack) {
            if(activity.getClass().equals(cls) ){
                return activity;
            }
        }
        return null;
    }


    /**
     * 缁撴潫褰撳墠Activity锛堝爢鏍堜腑鏈€鍚庝竴涓帇鍏ョ殑锛?
     */
    public void finishLastActivity(){
        Activity activity=activityStack.lastElement();
        finishActivity(activity);
    }
    /**
     * 缁撴潫鎸囧畾鐨凙ctivity
     */
    public void finishActivity(Activity activity){
        if(activity!=null){
            activityStack.remove(activity);
            activity.finish();
            activity=null;
        }
    }
    /**
     * 缁撴潫鎸囧畾绫诲悕鐨凙ctivity
     */
    public void finishActivity(Class<?> cls){
        for (Activity activity : activityStack) {
            if(activity.getClass().equals(cls) ){
                finishActivity(activity);
            }
        }
    }
    /**
     * 缁撴潫鎵€鏈堿ctivity
     */
    public void finishAllActivity(){
        for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }
    /**
     * 閫€鍑哄簲鐢ㄧ▼搴?
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityManager= (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {}
    }


}
