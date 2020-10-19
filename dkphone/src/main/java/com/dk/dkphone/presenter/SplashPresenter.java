package com.dk.dkphone.presenter;


import android.Manifest;
import android.content.Intent;

import com.dk.dkphone.R;
import com.dk.dkphone.handler.SplashHandler;
import com.dk.dkphone.view.activity.MainActivity;
import com.dk.dkphone.view.activity.SplashActivity;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;


public class SplashPresenter implements Presenter {

    public SplashActivity mSplashActivity;
    public SplashHandler mSplashHandler;

    public SplashPresenter(SplashActivity mSplashActivity) {
        this.mSplashActivity = mSplashActivity;
        mSplashHandler = new SplashHandler(mSplashActivity);
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        mSplashActivity.setContentView(R.layout.activity_splash);
        AppUtils.getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, mSplashActivity, SplashHandler.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE, mSplashHandler);
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
        mSplashHandler = null;
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }


    public void startMain()
    {
        Intent mainIntent = new Intent(mSplashActivity,
                MainActivity.class);
        mSplashActivity.startActivity(mainIntent);
        mSplashActivity.finish();
    }




}