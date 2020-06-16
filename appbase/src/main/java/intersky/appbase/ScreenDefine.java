package intersky.appbase;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import intersky.apputils.AppUtils;

import static com.amap.api.mapcore2d.q.d;

public class ScreenDefine {

    public int verticalMinDistance = 80;
    public int mPullScollY = 0;
    public int mGridMaxWidth = 320;
    public int mFunctionWidth = 0;
    public int mFunctionTextSpacing = 0;
    public int mFunctionhImageSpacing = 0;
    public int ScreenWidth;
    public int ScreenHeight;
    public float density;
    public DisplayMetrics metric;
    public int rx;
    public int ry;
    public ScreenDefine(Context mContext)
    {
        metric = AppUtils.getWindowInfo(mContext);
        if(metric.widthPixels > 1080)
        {
//            verticalMinDistance = 40*metric.widthPixels/1080;
            verticalMinDistance = (int) (20*metric.density);
        }
        if(metric.heightPixels > 1920)
        {
//            mPullScollY = 50*1920/metric.heightPixels;
            mPullScollY = (int) (50*metric.density);
        }
        mGridMaxWidth = metric.widthPixels/3;
        density = metric.density;
        mFunctionWidth = metric.widthPixels/4;
        mFunctionhImageSpacing = mFunctionWidth/2;
        mFunctionTextSpacing = mFunctionWidth/5;
        ScreenWidth = metric.widthPixels;
        ScreenHeight = metric.heightPixels;
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        final Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= 19) {
            // 可能有虚拟按键的情况
            display.getRealSize(outPoint);
        } else {
            // 不可能有虚拟按键
            display.getSize(outPoint);
        }
        int mRealSizeHeight;//手机屏幕真实高度
        ry = outPoint.y;
        rx = outPoint.x;
    }

}
