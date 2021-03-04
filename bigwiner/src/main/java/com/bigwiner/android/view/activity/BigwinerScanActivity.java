package com.bigwiner.android.view.activity;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.presenter.BigwinerScanPresenter;

import java.security.Policy;

import intersky.appbase.BaseActivity;
import intersky.scan.view.activity.BaseMipcaCaptureActivity;

/**
 * Created by xpx on 2017/8/18.
 */

public class BigwinerScanActivity extends BaseMipcaCaptureActivity {

    public BigwinerScanPresenter mBigwinerScanPresenter = new BigwinerScanPresenter(this);
    public ListView listView;
    public ImageView back;
    public Meeting meeting;
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
