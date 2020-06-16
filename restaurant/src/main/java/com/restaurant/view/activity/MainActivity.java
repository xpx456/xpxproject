package com.restaurant.view.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.restaurant.presenter.MainPresenter;
import com.restaurant.view.SuccessView;

import intersky.appbase.BaseActivity;
import intersky.appbase.PadBaseActivity;

public class MainActivity extends PadBaseActivity {

    public MainPresenter mMainPresenter = new MainPresenter(this);
    public TextView time;
    public TextView type;
    public ImageView setting;
    public SuccessView successView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainPresenter.Create();
    }
}
