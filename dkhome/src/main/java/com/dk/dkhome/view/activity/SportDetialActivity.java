package com.dk.dkhome.view.activity;

import android.os.Bundle;

import com.dk.dkhome.presenter.SportDetialPresenter;


public class SportDetialActivity extends BaseActivity {

    public SportDetialPresenter mSportDetialPresenter = new SportDetialPresenter(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSportDetialPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSportDetialPresenter.Destroy();
        mSportDetialPresenter.sportView.onDestory();
        super.onDestroy();
    }
}
