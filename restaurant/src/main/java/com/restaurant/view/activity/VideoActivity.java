package com.restaurant.view.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.restaurant.presenter.VideoPresenter;
import com.restaurant.view.SuccessView;
import com.restaurant.view.adapter.GallyPageAdapter;

import java.util.ArrayList;

import intersky.appbase.PadBaseActivity;


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
