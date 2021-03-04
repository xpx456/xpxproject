package intersky.filetools.entity;

import android.content.Context;
import android.util.DisplayMetrics;

import intersky.apputils.AppUtils;

public class ScreenDefine {

    public int verticalMinDistance = 40;
    public int mPullScollY = 0;
    public int mGridMaxWidth = 320;
    public int mFunctionWidth = 0;
    public int mFunctionTextSpacing = 0;
    public int mFunctionhImageSpacing = 0;
    public int ScreenWidth;
    public int ScreenHeight;
    public float density;

    public ScreenDefine(Context mContext)
    {
        DisplayMetrics metric = AppUtils.getWindowInfo(mContext);
        if(metric.widthPixels > 1080)
        {
            verticalMinDistance = 40*metric.widthPixels/1080;

        }
        if(metric.heightPixels > 1920)
        {
            mPullScollY = 50*1920/metric.heightPixels;
        }
        mGridMaxWidth = metric.widthPixels/3;
        density = metric.density;
        mFunctionWidth = metric.widthPixels/4;
        mFunctionhImageSpacing = mFunctionWidth/2;
        mFunctionTextSpacing = mFunctionWidth/5;
        ScreenWidth = metric.widthPixels;
        ScreenHeight = metric.heightPixels;
    }

}
