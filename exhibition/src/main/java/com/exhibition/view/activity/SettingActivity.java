package com.exhibition.view.activity;

import android.os.Bundle;

import com.exhibition.R;
import com.exhibition.presenter.SettingPresenter;

import intersky.appbase.BaseActivity;

public class SettingActivity extends BaseActivity {

    public SettingPresenter mSettingPresenter = new SettingPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mSettingPresenter.Create();
    }
}
