package com.interskypad.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.interskypad.presenter.FirstPresenter;

import intersky.appbase.PadBaseActivity;

public class FirstActivity extends PadBaseActivity {

    public ImageView mBtnOnline;
    public ImageView mBtnOffline;
    public FirstPresenter mFirstPresenter = new FirstPresenter(this);
    public FirstActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirstPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mFirstPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener offLineListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mFirstPresenter.startMain();
        }
    };

    public View.OnClickListener onLineListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mFirstPresenter.startLogin();
        }
    };
}
