package com.dk.dkphone.presenter;


import android.view.View;
import android.webkit.WebSettings;

import com.dk.dkphone.R;
import com.dk.dkphone.view.activity.MoviActivity;

import intersky.appbase.Presenter;


public class MoviPresenter implements Presenter {

    public MoviActivity mMoviActivity;
    public boolean ismale = true;
    public MoviPresenter(MoviActivity MoviActivity) {
        mMoviActivity = MoviActivity;
    }

    @Override
    public void initView() {

        mMoviActivity.flagFillBack = false;
        mMoviActivity.setContentView(R.layout.activity_movi);
        mMoviActivity.root = mMoviActivity.findViewById(R.id.root);
        mMoviActivity.root.setFocusable(true);
        mMoviActivity.root.setFocusableInTouchMode(true);

        mMoviActivity.video.getSettings().setAllowFileAccess(true);
        mMoviActivity.video.getSettings().setJavaScriptEnabled(true);
        mMoviActivity.video.getSettings().setDomStorageEnabled(true);
        mMoviActivity.video.getSettings().setUseWideViewPort(true); // 关键点
        mMoviActivity.video.getSettings().setSupportZoom(true); // 支持缩放
        mMoviActivity.video.getSettings().setLoadWithOverviewMode(true);
        mMoviActivity.video.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mMoviActivity.video.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        mMoviActivity.video.setWebViewClient(mMoviActivity.mWebViewClient);

        mMoviActivity.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoviActivity.finish();
            }
        });
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

    }


}
