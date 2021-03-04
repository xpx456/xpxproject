package com.test;

import android.app.Activity;
import android.os.Bundle;

import intersky.appbase.PadBaseActivity;
import intersky.mywidget.flipview.FlipView;


public class MainActivity extends PadBaseActivity {


    public MainPresenter mainPresenter = new MainPresenter(this);
    public FlipView mFlipView;
    public FlipAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainPresenter.Create();

    }


}
