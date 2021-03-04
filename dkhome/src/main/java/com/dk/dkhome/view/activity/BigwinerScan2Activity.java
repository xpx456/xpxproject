package com.dk.dkhome.view.activity;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.dk.dkhome.presenter.BigwinerScan2Presenter;

import intersky.scan.view.activity.BaseMipcaCapture2Activity;

/**
 * Created by xpx on 2017/8/18.
 */

public class BigwinerScan2Activity extends BaseMipcaCapture2Activity {

    public BigwinerScan2Presenter mBigwinerScanPresenter = new BigwinerScan2Presenter(this);
    public ListView listView;
    public ImageView back;
    public RelativeLayout light;
    public TextView mTextView;
    public Camera.Parameters parameters;
    public boolean hasClosed = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBigwinerScanPresenter.Create();
        super.mMipcaCapturePresenter.initView();
    }

    @Override
    protected void onDestroy() {
        mBigwinerScanPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public View.OnClickListener lightListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mBigwinerScanPresenter.doLight();
        }
    };
}
