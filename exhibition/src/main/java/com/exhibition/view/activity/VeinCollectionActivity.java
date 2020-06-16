package com.exhibition.view.activity;

import android.os.Bundle;

import com.exhibition.R;
import com.exhibition.presenter.VeinCollectionPresenter;

import intersky.appbase.BaseActivity;

public class VeinCollectionActivity extends BaseActivity {

    public VeinCollectionPresenter mVeinCollectionPresenter = new VeinCollectionPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veincollection);
        mVeinCollectionPresenter.Create();
    }
}
