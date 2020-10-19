package com.exhibition.view.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.exhibition.presenter.MainPresenter;
import com.exhibition.view.BaseSettingView;
import com.exhibition.view.QueryView;
import com.exhibition.view.SafeSettingView;
import com.exhibition.view.SystemSettingView;

import intersky.appbase.PadBaseActivity;

public class MainActivity extends PadBaseActivity {

    public MainPresenter mMainPresenter = new MainPresenter(this);
    public TextView title;
    public TextView btn1;
    public TextView btn2;
    public TextView btn3;
    public TextView btn4;
    public TextView btn5;
    public TextView btn6;
    public ImageView exist;
    public QueryView queryView;
    public BaseSettingView baseSettingView;
    public SafeSettingView safeSettingView;
    public SystemSettingView systemSettingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainPresenter.Create();
    }

    @Override
    public void gettouch() {
        mMainPresenter.updataTimeout();
    }

    @Override
    protected void onDestroy() {
        mMainPresenter.Destroy();
        super.onDestroy();
    }
}
