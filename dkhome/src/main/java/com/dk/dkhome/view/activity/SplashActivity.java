package com.dk.dkhome.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.dk.dkhome.handler.SplashHandler;
import com.dk.dkhome.presenter.SplashPresenter;

import intersky.apputils.AppUtils;


public class SplashActivity extends Activity
{
	public final static int SPLASH_DISPLAY_LENGHT = 3000; // 延迟3秒
	public View popupWindow;
	public RelativeLayout shade;
	public SplashPresenter mSplashPresenter = new SplashPresenter(this);

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		mSplashPresenter.Create();
	}
	
	@Override
	protected void onDestroy()
	{
		mSplashPresenter.Destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		mSplashPresenter.Start();
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
	@Override
	protected void onPause()
	{
		mSplashPresenter.Pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		mSplashPresenter.Resume();
		super.onResume();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case AppUtils.PERMISSION_REQUEST:
				boolean hasPermissionDismiss = false;      //有权限没有通过
				for (int i = 0; i < grantResults.length; i++) {
					if (grantResults[i] == -1) {
						hasPermissionDismiss = true;   //发现有未通过权限
						break;
					}
				}
				if(hasPermissionDismiss)
				{
					finish();
				}
				else
				{
					mSplashPresenter.startMain();
				}

				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
}
