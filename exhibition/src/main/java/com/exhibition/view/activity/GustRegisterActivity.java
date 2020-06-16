package com.exhibition.view.activity;

import android.os.Bundle;

import com.exhibition.R;
import com.exhibition.presenter.GustRegisterPresenter;

import intersky.appbase.BaseActivity;

public class GustRegisterActivity extends BaseActivity {

    public GustRegisterPresenter mGustRegisterPresenter = new GustRegisterPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gustregister);
        mGustRegisterPresenter.Create();
    }
}
