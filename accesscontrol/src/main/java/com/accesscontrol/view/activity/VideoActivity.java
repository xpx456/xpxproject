package com.accesscontrol.view.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;


import androidx.viewpager.widget.ViewPager;

import com.accesscontrol.presenter.VideoPresenter;
import com.accesscontrol.view.SuccessView;
import com.accesscontrol.view.adapter.GallyPageAdapter;

import java.util.ArrayList;



public class VideoActivity extends PadBaseActivity {

    public VideoPresenter mVideoPresenter = new VideoPresenter(this);
    public ViewPager viewPager;
    public SurfaceView videoView;
    public MediaPlayer mediaPlayer;
    public View screenBtn;
    public GallyPageAdapter mViewPagerAdapter;
    public SuccessView successView;
    public int changeTime = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoPresenter.Create();
    }

    @Override
    protected void onNewIntent(Intent intent) {

    }

}
