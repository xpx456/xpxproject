package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigwiner.android.presenter.ScanResultPresenter;

import intersky.appbase.BaseActivity;

/**
 * Created by xpx on 2017/8/18.
 */

public class ScanResultActivity extends BaseActivity {

    public ScanResultPresenter mScanResultPresenter = new ScanResultPresenter(this);
    public TextView imf;
    public ImageView back;
    public String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScanResultPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mScanResultPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
