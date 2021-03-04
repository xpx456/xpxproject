package com.intersky.android.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.intersky.android.handler.SplashHandler;
import com.intersky.android.presenter.SplashPresenter;

import intersky.appbase.BaseActivity;
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
			case SplashHandler.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE:
				if(grantResults.length > 0)
				{
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						AppUtils.getPermission(Manifest.permission.READ_EXTERNAL_STORAGE,this, SplashHandler.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE,this.mSplashPresenter.mSplashHandler);
					} else {
						finish();
					}
				}
				else
				{
					finish();
				}
				break;
			case SplashHandler.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE:
				if(grantResults.length > 0)
				{
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						AppUtils.getPermission(Manifest.permission.ACCESS_COARSE_LOCATION,this, SplashHandler.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE,this.mSplashPresenter.mSplashHandler);
					} else {
						finish();
					}
				}
				else
				{
					finish();
				}
				break;
			case SplashHandler.PERMISSION_REQUEST_READ_ACCESS_COARSE_LOCATION:
				if(grantResults.length > 0)
				{
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						AppUtils.getPermission(Manifest.permission.ACCESS_FINE_LOCATION,this,SplashHandler.PERMISSION_REQUEST_READ_ACCESS_FINE_LOCATION,this.mSplashPresenter.mSplashHandler);
					} else {

						finish();
					}
				}
				else
				{
					finish();
				}
				break;
			case SplashHandler.PERMISSION_REQUEST_READ_ACCESS_FINE_LOCATION:
				if(grantResults.length > 0)
				{
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						AppUtils.getPermission(Manifest.permission.READ_PHONE_STATE,this,SplashHandler.PERMISSION_REQUEST_READ_PHONE_STATE,this.mSplashPresenter.mSplashHandler);
					} else {

						finish();
					}
				}
				else
				{
					finish();
				}
				break;
			case SplashHandler.PERMISSION_REQUEST_READ_PHONE_STATE:
				if(grantResults.length > 0)
				{
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						AppUtils.getPermission(Manifest.permission.ACCESS_WIFI_STATE,this,SplashHandler.EVENT_START_LOGIN,this.mSplashPresenter.mSplashHandler);
					} else {

						finish();
					}
				}
				else
				{
					finish();
				}
				break;
			case SplashHandler.EVENT_START_LOGIN:
				if(grantResults.length > 0)
				{
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						this.mSplashPresenter.startLogin();
					} else {

						finish();
					}
				}
				else
				{
					finish();
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
}
