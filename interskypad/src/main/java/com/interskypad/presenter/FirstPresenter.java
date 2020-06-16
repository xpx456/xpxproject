package com.interskypad.presenter;

import android.content.Intent;
import android.widget.ImageView;

import com.interskypad.R;
import com.interskypad.view.activity.FirstActivity;
import com.interskypad.view.activity.LoginActivity;
import com.interskypad.view.activity.MainActivity;

import intersky.appbase.Presenter;

public class FirstPresenter implements Presenter {

    public FirstActivity mFirstActivity;

    public FirstPresenter(FirstActivity mFirstActivity) {
        this.mFirstActivity = mFirstActivity;

    }

    @Override
    public void initView() {
        mFirstActivity.setContentView(R.layout.activity_first);
        mFirstActivity.mBtnOnline = (ImageView) mFirstActivity.findViewById(R.id.first_buttom_online);
        mFirstActivity.mBtnOffline = (ImageView) mFirstActivity.findViewById(R.id.first_buttom_offline);
        mFirstActivity.mBtnOnline.setOnClickListener(mFirstActivity.onLineListener);
        mFirstActivity.mBtnOffline.setOnClickListener(mFirstActivity.offLineListener);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

    }

    public void startMain() {
        Intent intent = new Intent(mFirstActivity, MainActivity.class);
        mFirstActivity.startActivity(intent);
    }

    public void startLogin() {
        Intent intent = new Intent(mFirstActivity, LoginActivity.class);
        mFirstActivity.startActivity(intent);
    }
}

