package com.dk.dkhome.view.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.dk.dkhome.EchartView;
import com.dk.dkhome.presenter.SportDetialPresenter;

import intersky.appbase.BaseActivity;
import intersky.appbase.PadBaseActivity;

public class SportDetialActivity extends BaseActivity {

    public EchartView chart;
    public WebView chart2;
    public SportDetialPresenter mSportDetialPresenter = new SportDetialPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSportDetialPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSportDetialPresenter.Destroy();
        super.onDestroy();
    }
}
