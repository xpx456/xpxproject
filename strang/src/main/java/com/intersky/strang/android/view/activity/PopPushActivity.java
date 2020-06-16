package com.intersky.strang.android.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.sdk.android.push.AndroidPopupActivity;
import com.intersky.strang.android.handler.SplashHandler;
import com.intersky.strang.android.presenter.PopPushPresenter;

import java.util.Map;

import intersky.apputils.AppUtils;

public class PopPushActivity extends AndroidPopupActivity
{
	public final static int SPLASH_DISPLAY_LENGHT = 3000; // 延迟3秒

	public String title = "";
	public String content = "";
	public Map<String, String> map;
	public View popupWindow;
	public RelativeLayout shade;
	public PopPushPresenter mSplashPresenter = new PopPushPresenter(this);

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
						AppUtils.getPermission(Manifest.permission.READ_EXTERNAL_STORAGE,this, SplashHandler.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE,this.mSplashPresenter.mPopPushHandler);
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
						AppUtils.getPermission(Manifest.permission.ACCESS_COARSE_LOCATION,this, SplashHandler.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE,this.mSplashPresenter.mPopPushHandler);
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
						AppUtils.getPermission(Manifest.permission.ACCESS_WIFI_STATE,this,SplashHandler.EVENT_START_LOGIN,this.mSplashPresenter.mPopPushHandler);
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

	@Override
	protected void onSysNoticeOpened(String s, String s1, Map<String, String> map) {
		//AppUtils.showMessage(mSplashActivity,"poppushloging!!!!!!!!!!!!!");
		this.title= s;
		this.content = content;
		this.map = map;
	}
}
