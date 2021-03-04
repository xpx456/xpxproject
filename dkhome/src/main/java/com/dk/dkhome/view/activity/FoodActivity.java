package com.dk.dkhome.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.dk.dkhome.presenter.FoodPresenter;


public class FoodActivity extends BaseActivity
{
	public FoodPresenter mFoodPresenter = new FoodPresenter(this);

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		mFoodPresenter.Create();
	}
	
	@Override
	protected void onDestroy()
	{
		mFoodPresenter.Destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		mFoodPresenter.Start();
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
		mFoodPresenter.Pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		mFoodPresenter.Resume();
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

	}
}
