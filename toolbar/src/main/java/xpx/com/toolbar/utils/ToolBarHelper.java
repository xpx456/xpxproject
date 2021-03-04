package xpx.com.toolbar.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import java.lang.reflect.Method;

import intersky.apputils.AppUtils;
import intersky.apputils.NavigationBarUtil;
import intersky.apputils.Rom;
import xpx.com.toolbar.R;

import static android.view.View.NO_ID;


/**
 * Created by moon.zhong on 2015/6/12.
 * time : 10:45
 */
public class ToolBarHelper {

    /*上下文，创建view的时候需要用到*/
    private Context mContext;

    public int toolBarSize = 0;
    /*base view*/
    private FrameLayout mContentView;

    /*用户定义的view*/
    private View mUserView;

    /*toolbar*/
    private Toolbar mToolBar;

    /*视图构造器*/
    private LayoutInflater mInflater;

    private View ToolbarRoot;

    public int statusBarHeight = 0;
    /*
     * 两个属性
     * 1、toolbar是否悬浮在窗口之上
     * 2、toolbar的高度获取
     * */
    private static int[] ATTRS = {
            R.attr.windowActionBarOverlay,
            R.attr.actionBarSize
    };

    public ToolBarHelper(Context context, int layoutId) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        statusBarHeight = resources.getDimensionPixelSize(resourceId);
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        /*初始化整个内容*/
        initContentView();
        /*初始化用户定义的布局*/
        initUserView(layoutId);
        /*初始化toolbar*/
        initToolBar();
//        toolBarSize = (int) mContext.getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
    }

    private void initContentView() {
        /*直接创建一个帧布局，作为视图容器的父容器*/
        mContentView = new FrameLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);

    }

    private void initToolBar() {
        /*通过inflater获取toolbar的布局文件*/
        View toolbar = mInflater.inflate(R.layout.tool_bar, mContentView);
        ToolbarRoot = toolbar;
        RelativeLayout mRelativeLayout = (RelativeLayout) toolbar.findViewById(R.id.tool);
        mToolBar = (Toolbar) toolbar.findViewById(R.id.id_tool_bar);
    }

    private void initUserView(int id) {
        mUserView = mInflater.inflate(id, null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(ATTRS);
        /*获取主题中定义的悬浮标志*/
        boolean overly = typedArray.getBoolean(0, false);
        /*获取主题中定义的toolbar的高度*/
        //(int) mContext.getResources().getDimension(R.dimen.abc_action_bar_default_height_material)
        //int toolBarSize = (int)InterskyApplication.mApp.mAppScreenDenineModel.density*55;//(int) typedArray.getDimension(1,R.dimen.toolbar_height);
        toolBarSize = (int) mContext.getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
        typedArray.recycle();
        /*如果是悬浮状态，则不需要设置间距*/
        params.topMargin = overly ? 0 : toolBarSize;
        mContentView.addView(mUserView, params);

    }

    public FrameLayout getContentView() {
        return mContentView;
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }

    public void showToolbar() {
        mToolBar.setVisibility(View.INVISIBLE);
        View mView = mContentView.getChildAt(0);
//        int toolBarSize = (int) mContext.getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mView.getLayoutParams();
        params.topMargin = toolBarSize;
        mView.setLayoutParams(params);
    }

    public void hidToolbar() {
        mToolBar.setVisibility(View.INVISIBLE);
        View mView = mContentView.getChildAt(0);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mView.getLayoutParams();
        params.topMargin = 0;

    }

    //144 //2340/2130
    public boolean hidToolbar(Activity context, RelativeLayout statuebar) {
        mToolBar.setVisibility(View.INVISIBLE);
        View mView = mContentView.getChildAt(0);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mView.getLayoutParams();
        params.topMargin = 0;
        Window window = context.getWindow();
        setStatusBarTextColor(window, true);
        int size[] = getScreenSize(context);
        int nheight = getNavigationHeight(context);
        int show = getScreenShowHeight(context);
        //2340 / 126 /2139 /75
        //2280 /130 2150  /82
        //2400/118/2277/123
        //AppUtils.showMessage(mContext,"screensize:"+String.valueOf(size[1])+"////"+"naveheight:"+String.valueOf(nheight)+"///show:"+String.valueOf(show)+"///staute"+String.valueOf(statusBarHeight));
        if (nheight > 0) {
            if(size[1] >=  nheight+show && statusBarHeight < nheight )
            {
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) statuebar.getLayoutParams();
                params1.height = nheight;
                statuebar.setLayoutParams(params1);
                return true;
            }
            else
            {

                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) statuebar.getLayoutParams();

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                {
                    params1.height = (int) context.getResources().getDimension(R.dimen.buttom_height);
                    statuebar.setBackgroundColor(Color.parseColor("#00000000"));
                }
                else
                {
                    params1.height = 0;
                }
                statuebar.setLayoutParams(params1);
                return false;
            }
        } else {
            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) statuebar.getLayoutParams();
            params1.height = 0;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            {
                params1.height = (int) context.getResources().getDimension(R.dimen.buttom_height);
                statuebar.setBackgroundColor(Color.parseColor("#00000000"));
            }
            else
            {
                params1.height = 0;
            }
            statuebar.setLayoutParams(params1);
            return false;
        }

    }


    public static boolean isNavigationBarShown(Activity activity){
        //虚拟键的view,为空或者不可见时是隐藏状态
        View view  = activity.findViewById(android.R.id.navigationBarBackground);
        if(view == null){
            return false;
        }
        int visible = view.getVisibility();
        if(visible == View.GONE || visible == View.INVISIBLE){
            return false ;
        }else{
            return true;
        }
    }

    public static int getCurrentNavigationBarHeight(Activity activity){
        if(isNavigationBarShown(activity)){
            return getNavigationBarHeight2(activity);
        } else{
            return 0;
        }
    }

    public static int getNavigationBarHeightIfRoom(Context context) {
        if(navigationGestureEnabled(context)){
            return 0;
        }
        return getCurrentNavigationBarHeight(((Activity) context));
    }

    public static boolean navigationGestureEnabled(Context context) {
        int val = Settings.Global.getInt(context.getContentResolver(), getDeviceInfo(), 0);
        return val != 0;
    }

    public static int getNavigationBarHeight2(Context context){
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height","dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    /**
     * 获取设备信息（目前支持几大主流的全面屏手机，亲测华为、小米、oppo、魅族、vivo都可以）
     *
     * @return
     */
    public static String getDeviceInfo() {
        String brand = Build.BRAND;
        if(TextUtils.isEmpty(brand)) return "navigationbar_is_min";

        if (brand.equalsIgnoreCase("HUAWEI")) {
            return "navigationbar_is_min";
        } else if (brand.equalsIgnoreCase("XIAOMI")) {
            return "force_fsg_nav_bar";
        } else if (brand.equalsIgnoreCase("VIVO")) {
            return "navigation_gesture_on";
        } else if (brand.equalsIgnoreCase("OPPO")) {
            return "navigation_gesture_on";
        } else {
            return "navigationbar_is_min";
        }
    }


    public static int getNavigationHeight(Context activity) {
        if (activity == null) {
            return 0;
        }
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        int height = 0;
        if (resourceId > 0) {
            //获取NavigationBar的高度
            height = resources.getDimensionPixelSize(resourceId);
        }
        return height;
    }

    private static final String NAVIGATION = "navigationBarBackground";

    public static boolean isNavigationBarExist(Activity activity) {
        ViewGroup vp = (ViewGroup) activity.getWindow().getDecorView();
        if (vp != null) {
            for (int i = 0; i < vp.getChildCount(); i++) {
                vp.getChildAt(i).getContext().getPackageName();

                if (vp.getChildAt(i).getId() != -1 && NAVIGATION.equals(activity.getResources().getResourceEntryName(vp.getChildAt(i).getId()))) {
                    return true;
                }
            }
        }
        return false;
    }


    public void hidToolbar2(Activity context) {
        mToolBar.setVisibility(View.INVISIBLE);
        View mView = mContentView.getChildAt(0);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mView.getLayoutParams();
        params.topMargin = 0;

        Window window = context.getWindow();
        setStatusBarTextColor(window, true);
    }

    public void hidToolbar2(Activity context, RelativeLayout statuebar) {
        mToolBar.setVisibility(View.INVISIBLE);
        View mView = mContentView.getChildAt(0);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mView.getLayoutParams();
        params.topMargin = 0;

        Window window = context.getWindow();
        setStatusBarTextColor(window, false);

        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) statuebar.getLayoutParams();
        params1.height = ToolBarHelper.getNavigationBarHeight(context);
        statuebar.setLayoutParams(params1);
    }

    public void setStatusBarTextColor(Window window, boolean lightStatusBar) {
        // 设置状态栏字体颜色 白色与深色
        View decor = window.getDecorView();
        int ui = decor.getSystemUiVisibility();
        ui |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (lightStatusBar) {
                ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
        }
        decor.setSystemUiVisibility(ui);
    }

    public View getToolbarRoot() {
        return ToolbarRoot;
    }

    public static final void setSutColor(Activity mContext, int color) {
        int height;
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = mContext.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);

            ViewGroup mContentView = (ViewGroup) mContext.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }

        } else {
            Window window = mContext.getWindow();
            ViewGroup mContentView = (ViewGroup) mContext.findViewById(Window.ID_ANDROID_CONTENT);

            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = getStatusBarHeight(mContext);

            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
                //如果已经为 ChildView 设置过了 marginTop, 再次调用时直接跳过
                if (lp != null && lp.topMargin < statusBarHeight && lp.height != statusBarHeight) {
                    //不预留系统空间
                    ViewCompat.setFitsSystemWindows(mChildView, false);
                    lp.topMargin += statusBarHeight;
                    mChildView.setLayoutParams(lp);
                }
            }

            View statusBarView = mContentView.getChildAt(0);
            if (statusBarView != null && statusBarView.getLayoutParams() != null && statusBarView.getLayoutParams().height == statusBarHeight) {
                //避免重复调用时多次添加 View
                statusBarView.setBackgroundColor(color);
                return;
            }
            statusBarView = new View(mContext);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            statusBarView.setBackgroundColor(color);
            mContentView.addView(statusBarView, 0, lp);
        }

    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static final void setTitle(Toolbar bar, String title) {
        View v = bar.getRootView();
        TextView tv = (TextView) v.findViewById(R.id.title);
        tv.setText(title);
    }

    public static final void setTitle(Toolbar bar, View.OnClickListener titleclicklistener, String title) {
        View v = bar.getRootView();
        TextView tv = (TextView) v.findViewById(R.id.title);
        tv.setOnClickListener(titleclicklistener);
        tv.setText(title);
    }

    public static final void setTitle(Toolbar bar, View.OnClickListener titleclicklistener, String title1, String title2) {
        View v = bar.getRootView();
        TextView tv = (TextView) v.findViewById(R.id.title);
        tv.setVisibility(View.GONE);
        RelativeLayout dtitle = v.findViewById(R.id.title2layer);
        dtitle.setVisibility(View.VISIBLE);
        TextView tv1 = (TextView) v.findViewById(R.id.title1);
        TextView tv2 = (TextView) v.findViewById(R.id.title2);
        dtitle.setOnClickListener(titleclicklistener);
        tv1.setText(title1);
        tv2.setText(title2);
    }

    public static final void setTitle3(Toolbar bar, String title, View.OnClickListener mlistener) {
        View v = bar.getRootView();
        TextView tv = (TextView) v.findViewById(R.id.title);
        tv.setVisibility(View.GONE);
        RelativeLayout mRelativeLayout = (RelativeLayout) v.findViewById(R.id.title2layer);
        mRelativeLayout.setVisibility(View.GONE);
        mRelativeLayout = (RelativeLayout) v.findViewById(R.id.title3layer);
        mRelativeLayout.setVisibility(View.VISIBLE);
        tv = (TextView) v.findViewById(R.id.title22);
        mRelativeLayout.setOnClickListener(mlistener);
        tv.setText(title);
    }

    public static final void setTitle(Toolbar bar, String title, int color) {
        View v = bar.getRootView();
        TextView tv = (TextView) v.findViewById(R.id.title);
        tv.setTextColor(color);
        tv.setText(title);
    }

    public static void setRightBtnText(Toolbar bar, View.OnClickListener mlistener, String name) {
        View v = bar.getRootView();
        TextView tv = (TextView) v.findViewById(R.id.right_btn);
        ImageView tv1 = (ImageView) v.findViewById(R.id.navRightBtn2);
        tv1.setVisibility(View.GONE);
        tv.setText(name);
        tv.setOnClickListener(mlistener);
        tv.setVisibility(View.VISIBLE);
    }

    public static void setRightBtnText(Toolbar bar, View.OnClickListener mlistener, String name, int color) {
        View v = bar.getRootView();
        TextView tv = (TextView) v.findViewById(R.id.right_btn);
        tv.setText(name);
        tv.setTextColor(color);
        tv.setOnClickListener(mlistener);
        tv.setVisibility(View.VISIBLE);
    }

    public static void setRightBtnText(Toolbar bar, View.OnClickListener mlistener, String name, boolean type) {
        View v = bar.getRootView();
        TextView tv = (TextView) v.findViewById(R.id.right_btn);
        tv.setText(name);
        tv.setOnClickListener(mlistener);
        tv.setVisibility(View.VISIBLE);
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
    }

    public static final void setRightBtn(Toolbar bar, View.OnClickListener mlistener, int id) {
        View v = bar.getRootView();
        ImageView tv = (ImageView) v.findViewById(R.id.navRightBtn2);
        TextView tv1 = (TextView) v.findViewById(R.id.right_btn);
        tv1.setVisibility(View.GONE);
        tv.setImageResource(id);
        tv.setOnClickListener(mlistener);
        tv.setVisibility(View.VISIBLE);
    }

    public static final void setBackListenr(Toolbar bar, View.OnClickListener mlistener) {
        View v = bar.getRootView();
        bar.setNavigationIcon(R.drawable.back);
        bar.setNavigationOnClickListener(mlistener);
    }

    public static final void setBackListenr(Toolbar bar, View.OnClickListener mlistener, String name) {
        View v = bar.getRootView();
        bar.setNavigationIcon(R.drawable.back);
        TextView tv = (TextView) v.findViewById(R.id.backText);
        tv.setVisibility(View.GONE);
        bar.setNavigationOnClickListener(mlistener);
    }

    public static final void hidBack(Toolbar bar) {
        bar.setNavigationIcon(null);
    }

    public static final void setBack(Toolbar bar, int id) {
        bar.setNavigationIcon(id);
    }

    public static final void setBgColor(Activity mContext, Toolbar bar, int color) {
        View v = bar.getRootView();
        Toolbar mToolbar = (Toolbar) v.findViewById(R.id.id_tool_bar);
        mToolbar.setBackgroundColor(color);
        setSutColor(mContext, color);
    }

    public static final void setRightBtn(Toolbar bar, View.OnClickListener mlistener, View.OnClickListener mlistener1, int id, String name) {
        View v = bar.getRootView();
        ImageView tv = (ImageView) v.findViewById(R.id.navRightBtn3);
        tv.setImageResource(id);
        tv.setOnClickListener(mlistener);
        tv.setVisibility(View.VISIBLE);
        TextView tv1 = (TextView) v.findViewById(R.id.navRightBtnText);
        tv1.setText(name);
        tv1.setOnClickListener(mlistener1);
        tv1.setVisibility(View.VISIBLE);
    }

    public static final void setRightBtn(Toolbar bar, View.OnClickListener mlistener, View.OnClickListener mlistener1, int id, int id2) {
        View v = bar.getRootView();
        ImageView tv = (ImageView) v.findViewById(R.id.navRightBtn3);
        tv.setImageResource(id);
        tv.setOnClickListener(mlistener);
        tv.setVisibility(View.VISIBLE);
        TextView tv1 = (TextView) v.findViewById(R.id.navRightBtnText);
        tv1.setVisibility(View.GONE);
        ImageView tv2 = (ImageView) v.findViewById(R.id.navRightBtn2);
        tv2.setImageResource(id2);
        tv2.setOnClickListener(mlistener1);
        tv2.setVisibility(View.VISIBLE);
    }

    public static final void hidRight(Toolbar bar) {
        View v = bar.getRootView();
        TextView tv = (TextView) v.findViewById(R.id.navRightBtnText);
        ImageView tv2 = (ImageView) v.findViewById(R.id.navRightBtn2);
        tv.setVisibility(View.GONE);
        tv2.setVisibility(View.GONE);
    }

    public static final void setBackBtnText2(Toolbar v, View.OnClickListener mlistener, String name, int Color) {
        hidBack(v);
        RelativeLayout layer = (RelativeLayout) v.findViewById(R.id.leftbtn);
        layer.setVisibility(View.VISIBLE);
        TextView tv = (TextView) v.findViewById(R.id.navleftBtnText3);
        tv.setTextColor(Color);
        tv.setText(name);
//		tv.setVisibility(View.VISIBLE);
//		tv.setOnClickListener(mlistener);
        layer.setOnClickListener(mlistener);
    }

    public static final void setLeftBtnText22(TextView textView, View.OnClickListener mlistener, String name) {
        TextView tv = textView;
        tv.setText(name);
        tv.setVisibility(View.VISIBLE);
        tv.setOnClickListener(mlistener);
    }


    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }

    public static int getNavigationBar(Context context) {
//        AppUtils.showMessage(context,"sh"+String.valueOf(getScreenHeight(context))+"/"+"rh"+String.valueOf(getScreenShowHeight(context)));
        return getScreenHeight(context) - getScreenShowHeight(context);
    }

    //屏幕的全高
    public static int getScreenHeight(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    //屏幕显示内容的高
    public static int getScreenShowHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static void setLightNavigationBar(Activity activity, boolean light) {
        int vis = activity.getWindow().getDecorView().getSystemUiVisibility();
        if (light) {
            vis |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;  // 黑色
        } else {
            //白色
            vis &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            activity.getWindow().getDecorView().setSystemUiVisibility(vis);
        }

    }

    public static void setNavbarColor(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setNavigationBarColor(Color.rgb(0, 0, 0)); //#fafafa
        }
    }

    public static void hidToolbar(ToolBarHelper toolBarHelper) {
        toolBarHelper.mContentView.removeView(toolBarHelper.mToolBar);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) toolBarHelper.mUserView.getLayoutParams();
        params.topMargin = 0;
        toolBarHelper.mUserView.setLayoutParams(params);
    }

    public static void showToolbar(Context mContext, ToolBarHelper toolBarHelper) {
        toolBarHelper.mContentView.removeView(toolBarHelper.mToolBar);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) toolBarHelper.mUserView.getLayoutParams();
        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(ATTRS);
        /*获取主题中定义的悬浮标志*/
        boolean overly = typedArray.getBoolean(0, false);
        /*获取主题中定义的toolbar的高度*/
        int toolBarSize = (int) mContext.getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
        typedArray.recycle();
        /*如果是悬浮状态，则不需要设置间距*/
        params.topMargin = overly ? 0 : toolBarSize;
        toolBarHelper.mUserView.setLayoutParams(params);
        toolBarHelper.mContentView.addView(toolBarHelper.mToolBar, params);
    }

    public static int[] getScreenSize(Context context) {
        int[] size = new int[2];

        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored) {
            }
        size[0] = widthPixels;
        size[1] = heightPixels;
        return size;
    }


}