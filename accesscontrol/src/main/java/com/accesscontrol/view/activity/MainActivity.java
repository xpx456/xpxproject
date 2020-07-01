package com.accesscontrol.view.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.accesscontrol.presenter.MainPresenter;
import com.accesscontrol.view.SuccessView;

import intersky.appbase.PadBaseActivity;

public class MainActivity extends PadBaseActivity {

    public MainPresenter mMainPresenter = new MainPresenter(this);
    public ImageView contact;
    public ImageView setting;
    public SuccessView successView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainPresenter.Create();
    }
}
