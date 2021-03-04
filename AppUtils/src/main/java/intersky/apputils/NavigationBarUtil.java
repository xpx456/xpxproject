package intersky.apputils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.lang.reflect.Method;

public class NavigationBarUtil {
    public static void initActivity(View content) {
        new NavigationBarUtil(content);
    }

    private View mObserved;//被监听的视图
    private int usableHeightView;//视图变化前的可用高度
    private ViewGroup.LayoutParams layoutParams;

    private NavigationBarUtil(View content) {
        mObserved = content;
        //给View添加全局的布局监听器监听视图的变化
        mObserved.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                resetViewHeight();
            }
        });
        layoutParams = mObserved.getLayoutParams();
    }

    /**
     * 重置视图的高度，使不被底部虚拟键遮挡
     */
    private void resetViewHeight() {
        int usableHeightViewNow = CalculateAvailableHeight();
        //比较布局变化前后的View的可用高度
        if (usableHeightViewNow != usableHeightView) {
            //如果两次高度不一致
            //将当前的View的可用高度设置成View的实际高度
            layoutParams.height = usableHeightViewNow;
            mObserved.requestLayout();//请求重新布局
            usableHeightView = usableHeightViewNow;
        }
    }

    /**
     * 计算试图高度
     * @return
     */
    private int CalculateAvailableHeight() {
        Rect r = new Rect();
        mObserved.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);//如果不是沉浸状态栏，需要减去顶部高度
//        return (r.bottom );//如果是沉浸状态栏
    }

    /**
     * 判断底部是否有虚拟键
     * @param context
     * @return
     */
    public static boolean hasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }


    //检查设备是否具有NavigationBar - (全面屏返回 Ture)
    public static boolean checkDeviceHasNavigationBar(Activity activity) {
        boolean hasNavigationBar = false;
        Resources rs = activity.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

    public static boolean isNavigationBarShow(Activity context){

        View view = context.getWindow().getDecorView();
        int height = view.getHeight();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = context.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y!=size.y;
        }else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if(menu || back) {
                return false;
            }else {
                return true;
            }
        }

    }

    public static int getNavigationBarHeight(Activity activity) {
        if (!isNavigationBarShow(activity)){
            return 0;
        }
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }


    public static int getSceenHeight(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getHeight()+getNavigationBarHeight(activity);
    }
}
