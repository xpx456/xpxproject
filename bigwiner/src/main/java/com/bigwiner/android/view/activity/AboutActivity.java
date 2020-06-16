package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.presenter.AboutPresenter;

import intersky.appbase.BaseActivity;

/**
 * Created by xpx on 2017/8/18.
 */

public class AboutActivity extends BaseActivity {

    public AboutPresenter mAboutPresenter = new AboutPresenter(this);
    public ImageView back;
    public TextView safe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAboutPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mAboutPresenter.Destroy();
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
            mAboutPresenter.showSafe();
        }
    };
}
