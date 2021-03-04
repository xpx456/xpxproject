package com.interskypad.view.activity;

import android.os.Bundle;

import com.interskypad.presenter.AboutPresenter;

import intersky.appbase.PadBaseActivity;

public class AboutActivity extends PadBaseActivity {

    public AboutPresenter mAboutPresenter = new AboutPresenter(this);
    public AboutActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAboutPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mAboutPresenter.Destroy();
        super.onDestroy();
    }
}
