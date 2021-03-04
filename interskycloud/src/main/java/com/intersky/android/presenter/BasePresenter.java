package com.intersky.android.presenter;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.intersky.android.view.activity.BaseActivity;

import java.util.List;

import intersky.appbase.AppActivityManager;
import intersky.appbase.PermissionResult;
import intersky.appbase.Presenter;
import intersky.appbase.ScreenDefine;
import intersky.apputils.AppUtils;
import intersky.apputils.WaitDialog;
import xpx.com.toolbar.utils.ToolBarHelper;


//import com.umeng.analytics.MobclickAgent;
//import cn.jpush.android.api.JPushInterface;

public class BasePresenter implements Presenter {

	private BaseActivity mBaseActivity;
	public ScreenDefine mScreenDefine;

	public BasePresenter(BaseActivity mBaseActivity)
	{
		
		this.mBaseActivity = mBaseActivity;
	}

	@Override
	public void Create() {
		// TODO Auto-generated method stub
		mScreenDefine = new ScreenDefine(mBaseActivity);
		AppActivityManager.getInstance().addActivity(mBaseActivity);
		mBaseActivity.mGestureDetector = new GestureDetector(mBaseActivity, mBaseActivity);
		mBaseActivity.rid = AppUtils.getguid();
		mBaseActivity.waitDialog = new WaitDialog(mBaseActivity);
		mBaseActivity.waitDialog.setCancelable(true);

		ToolBarHelper.setSutColor(mBaseActivity, Color.rgb(255,255,255));
        if(mBaseActivity.baseReceiver != null) {
            mBaseActivity.registerReceiver(mBaseActivity.baseReceiver,mBaseActivity.baseReceiver.intentFilter);
        }

		final View decorView = mBaseActivity.getWindow().getDecorView();
		mBaseActivity.decorviewHeight = decorView.getHeight();

	}
	
	@Override
	public void Start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void Pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		mBaseActivity.waitDialog.dismiss();
        if(mBaseActivity.baseReceiver != null) {
            mBaseActivity.unregisterReceiver(mBaseActivity.baseReceiver);
        }
		AppActivityManager.getInstance().finishActivity(mBaseActivity);

	}

	public void setPermissionsRequest(PermissionResult mPermissionResult) {
		mBaseActivity.permissionRepuest = mPermissionResult;
	}

	public void oPermissionsRequest(int requestCode,int[] grantResults) {
		if(mBaseActivity.permissionRepuest != null)
		{
			mBaseActivity.permissionRepuest.doResult(requestCode,grantResults);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			mBaseActivity.finish();

			return false;
		}
		else {
			return false;
		}
	}

	public void setGesture(int layoutResID)
	{
		LayoutInflater mInflater = LayoutInflater.from(mBaseActivity);
		View mUserView = mInflater.inflate(layoutResID, null);
		mUserView.setOnTouchListener(mBaseActivity);
	}

	public void setToolBar(int layoutResID)
	{
		// 设置导航栏
		mBaseActivity.mToolBarHelper = new ToolBarHelper(mBaseActivity,layoutResID) ;
		mBaseActivity.mActionBar = mBaseActivity.mToolBarHelper.getToolBar() ;
		mBaseActivity.setContentView(mBaseActivity.mToolBarHelper.getContentView()); /*把 toolbar 设置到Activity 中*/
		mBaseActivity.mActionBar.setTitleTextColor(Color.BLACK);
		mBaseActivity.mActionBar.setTitle("");
		mBaseActivity.mActionBar.setNavigationIcon(intersky.appbase.R.drawable.back);
		mBaseActivity.setSupportActionBar(mBaseActivity.mActionBar); /*自定义的一些操作*/
		mBaseActivity.mActionBar.setNavigationOnClickListener(mBaseActivity.mBackListener);
		mBaseActivity.mActionBar.setContentInsetsRelative(0,0);
	}


	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device
        
        ActivityManager activityManager = (ActivityManager) mBaseActivity.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = mBaseActivity.getApplicationContext().getPackageName();

        List<RunningAppProcessInfo> appProcesses = activityManager
                        .getRunningAppProcesses();
        if (appProcesses == null)
                return false;

        for (RunningAppProcessInfo appProcess : appProcesses) {
                // The name of the process that this object is associated with.
                if (appProcess.processName.equals(packageName)
                                && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return true;
                }
        }
        return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		if (e1.getX() - e2.getX() > mScreenDefine.verticalMinDistance*mScreenDefine.density && Math.abs(velocityX) > 0)
		{
			return false;
		}
		else if (e2.getX() - e1.getX() > mScreenDefine.verticalMinDistance*mScreenDefine.density && Math.abs(velocityX) > 0)
		{

			if (e2.getX() - e1.getX() > e2.getY() - e1.getY() && e2.getX() - e1.getX() > e1.getY() - e2.getY() && mBaseActivity.flagFillBack == true)
			{
				mBaseActivity.isdestory = true;
				mBaseActivity.finish();
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;
	}
}
