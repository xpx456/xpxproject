package com.exhibition.view.activity;

import android.os.Bundle;

import com.exhibition.R;
import com.exhibition.presenter.GustDetialPresenter;

import intersky.appbase.BaseActivity;

public class GustDetialActivity extends BaseActivity {

    public GustDetialPresenter mGustDetialPresenter = new GustDetialPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gustdetial);
        mGustDetialPresenter.Create();
    }
}
