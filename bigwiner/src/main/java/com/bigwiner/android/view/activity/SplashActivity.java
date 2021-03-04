package com.bigwiner.android.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.bigwiner.android.handler.SplashHandler;
import com.bigwiner.android.presenter.SplashPresenter;

import intersky.appbase.BaseActivity;
import intersky.apputils.AppUtils;

public class SplashActivity extends BaseActivity
{
	public final static int SPLASH_DISPLAY_LENGHT = 3000; // 延迟3秒
	public final static String BG_PATH_21 = "http://47.56.104.229:81/static/bigwinner_static/images/default@3x.png";
	public final static String BG_PATH_16 = "http://47.56.104.229:81/static/bigwinner_static/images/guide.png";
	public final static String BG_PATH_S = "http://47.56.104.229:81/static/bigwinner_static/images/guide_s.png";
	public final static String key = "0";
	public String val[];
	public SplashPresenter mSplashPresenter = new SplashPresenter(this);

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
		mSplashPresenter.praseIntent(getIntent());
        mSplashPresenter.Create();

        // ATTENTION: This was auto-generated to handle app links.

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
	protected void onNewIntent(Intent intent) {
		//super.onNewIntent(intent);
		mSplashPresenter.praseIntent(intent);
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
						mSplashPresenter.initdata();
						AppUtils.getPermission(Manifest.permission.ACCESS_COARSE_LOCATION,this, SplashHandler.PERMISSION_REQUEST_READ_ACCESS_COARSE_LOCATION,this.mSplashPresenter.mSplashHandler);
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
						this.mSplashPresenter.startMain();
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
