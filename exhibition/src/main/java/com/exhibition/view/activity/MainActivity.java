package com.exhibition.view.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.exhibition.R;
import com.exhibition.presenter.MainPresenter;
import com.exhibition.view.QueryView;
import com.exhibition.view.RegisterView;

import intersky.appbase.BaseActivity;
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
    public RegisterView registerView;
    public QueryView queryView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainPresenter.Create();
    }

    @Override
    public void gettouch() {
        mMainPresenter.updataTimeout();
    }

}
