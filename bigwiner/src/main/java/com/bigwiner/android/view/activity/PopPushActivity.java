package com.bigwiner.android.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.alibaba.sdk.android.push.AndroidPopupActivity;
import com.bigwiner.android.handler.PopPushHandler;
import com.bigwiner.android.presenter.PopPushPresenter;

import java.util.Map;

import intersky.apputils.AppUtils;

public class PopPushActivity extends AndroidPopupActivity
{
	public final static int SPLASH_DISPLAY_LENGHT = 3000; // 延迟3秒
	public final static String BG_PATH_21 = "http://47.56.104.229:81/static/bigwinner_static/images/default@3x.png";
	public final static String BG_PATH_16 = "http://47.56.104.229:81/static/bigwinner_static/images/guide.png";
	public final static String BG_PATH_S = "http://47.56.104.229:81/static/bigwinner_static/images/guide_s.png";
	public final static String key = "0";
	public String val[];
	public Map<String, String> map;
	public String title = "";
	public String content = "";
	public PopPushPresenter mPopPushPresenter = new PopPushPresenter(this);

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
		mPopPushPresenter.praseIntent(getIntent());
        mPopPushPresenter.Create();

        // ATTENTION: This was auto-generated to handle app links.

    }
	
	@Override
	protected void onDestroy()
	{
		mPopPushPresenter.Destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		mPopPushPresenter.Start();
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
		mPopPushPresenter.Pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		mPopPushPresenter.Resume();
		super.onResume();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		//super.onNewIntent(intent);
		mPopPushPresenter.praseIntent(intent);
		super.onResume();
	}



	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case PopPushHandler.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE:
				if(grantResults.length > 0)
				{
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						AppUtils.getPermission(Manifest.permission.READ_EXTERNAL_STORAGE,this, PopPushHandler.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE,this.mPopPushPresenter.mPopPushHandler);
					} else {
						finish();
					}
				}
				else
				{
					finish();
				}
				break;
			case PopPushHandler.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE:
				if(grantResults.length > 0)
				{
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						mPopPushPresenter.initdata();
						AppUtils.getPermission(Manifest.permission.ACCESS_COARSE_LOCATION,this, PopPushHandler.PERMISSION_REQUEST_READ_ACCESS_COARSE_LOCATION,this.mPopPushPresenter.mPopPushHandler);
					} else {
						finish();
					}
				}
				else
				{
					finish();
				}
				break;
			case PopPushHandler.PERMISSION_REQUEST_READ_ACCESS_COARSE_LOCATION:
				if(grantResults.length > 0)
				{
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						AppUtils.getPermission(Manifest.permission.ACCESS_WIFI_STATE,this,PopPushHandler.EVENT_START_LOGIN,this.mPopPushPresenter.mPopPushHandler);
					} else {

						finish();
					}
				}
				else
				{
					finish();
				}
				break;
			case PopPushHandler.EVENT_START_LOGIN:
				if(grantResults.length > 0)
				{
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						this.mPopPushPresenter.startMain();
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
		this.title= s;
		this.content = content;
		this.map = map;
	}
}
