package com.dk.dkhome.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.dk.dkhome.presenter.RegisterPresenter;


public class RegisterActivity extends BaseActivity
{
	public static final String IS_REGISTER = "IS_REGISTER";
	public static final int CHOSE_PICTURE_HEAD = 0x6;
	public static final int TAKE_PHOTO_HEAD = 0x4;
	public static final int CROP_HEAD = 0x8;
	public RegisterPresenter mRegisterPresenter = new RegisterPresenter(this);

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		mRegisterPresenter.Create();
	}
	
	@Override
	protected void onDestroy()
	{
		mRegisterPresenter.Destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		mRegisterPresenter.Start();
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
		mRegisterPresenter.Pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		mRegisterPresenter.Resume();
		super.onResume();
	}

	@Override
	public void onGetPermissionFinish()
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		mRegisterPresenter.takePhotoResult(requestCode, resultCode, data);

	}
}
