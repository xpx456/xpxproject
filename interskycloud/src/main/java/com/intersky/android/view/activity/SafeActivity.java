package com.intersky.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.intersky.android.presenter.SafePresenter;


/**
 * Created by xpx on 2017/8/18.
 */

public class SafeActivity extends BaseActivity {
    public WebView webView;
    public SafePresenter mSafePresenter = new SafePresenter(this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSafePresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSafePresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public View.OnClickListener safeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSafePresenter.showSafe();
        }
    };
}
