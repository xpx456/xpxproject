package com.dk.dktest.presenter;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.dk.dktest.R;
import com.dk.dktest.handler.SplashHandler;
import com.dk.dktest.view.activity.MainActivity;
import com.dk.dktest.view.activity.SplashActivity;

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
