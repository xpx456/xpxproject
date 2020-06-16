package com.exhibition.view.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import com.exhibition.presenter.VideoPresenter;

import intersky.appbase.BaseActivity;
import intersky.appbase.PadBaseActivity;


public class VideoActivity extends PadBaseActivity {

    public VideoPresenter mVideoPresenter = new VideoPresenter(this);
    public SurfaceView videoView;
    public MediaPlayer mediaPlayer;
    public View screenBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoPresenter.Create();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        int a= 10;

    }


}
