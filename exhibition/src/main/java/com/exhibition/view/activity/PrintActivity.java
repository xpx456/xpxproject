package com.exhibition.view.activity;

import android.os.Bundle;

import com.exhibition.R;
import com.exhibition.presenter.PrintPresenter;

import intersky.appbase.BaseActivity;

public class PrintActivity extends BaseActivity {

    public PrintPresenter mPrintPresenter = new PrintPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        mPrintPresenter.Create();
    }
}

