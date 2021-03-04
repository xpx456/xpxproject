package com.restaurant.presenter;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.restaurant.R;
import com.restaurant.handler.VideoHandler;
import com.restaurant.receiver.VideoReceiver;
import com.restaurant.view.DepthPageTransformer;
import com.restaurant.view.FixedSpeedScroller;
import com.restaurant.view.RestaurantApplication;
import com.restaurant.view.SuccessView;
import com.restaurant.view.activity.MainActivity;
import com.restaurant.view.activity.VideoActivity;
import com.restaurant.view.adapter.GallyPageAdapter;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import intersky.appbase.AppActivityManager;
import intersky.appbase.MySimpleTarget;
import intersky.appbase.Presenter;
import intersky.apputils.BitmapCache;
import intersky.apputils.GlideApp;

public class VideoPresenter implements Presenter {

    public VideoActivity mVideoActivity;
    public VideoHandler videoHandler;
    public VideoPresenter(VideoActivity mVideoActivity) {
        this.mVideoActivity = mVideoActivity;
        videoHandler = new VideoHandler(mVideoActivity);
        mVideoActivity.setBaseReceiver(new VideoReceiver(videoHandler));
    }

    @Override
    public void initView() {
        mVideoActivity.flagFillBack = false;
        mVideoActivity.setContentView(R.layout.activity_video);
        mVideoActivity.flagFillBack = false;
        mVideoActivity.viewPager = mVideoActivity.findViewById(R.id.page);
        mVideoActivity.screenBtn = mVideoActivity.findViewById(R.id.screenbtn);
        mVideoActivity.viewPager = mVideoActivity.findViewById(R.id.des_pager);
        mVideoActivity.screenBtn.setOnClickListener(loginListener);
        mVideoActivity.viewPager.setVisibility(View.INVISIBLE);
        mVideoActivity.successView = new SuccessView(mVideoActivity);
        updateView();
        updataGally();
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
        //releasePlayer();
    }


    public MediaPlayer.OnPreparedListener mPrepareListener = new MediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer mp) {
            mVideoActivity.mediaPlayer.start();
        }
    };

    public MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            mVideoActivity.mediaPlayer.seekTo(0);
            mVideoActivity.mediaPlayer.start();

        }
    };

    public View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startLogin();
        }
    };


    private final class SurfaceCallback implements SurfaceHolder.Callback {
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        public void surfaceCreated(SurfaceHolder holder) {
//            if(RestaurantApplication.mApp.video != null)
//            {
//
//                mVideoActivity.mediaPlayer.setDisplay(mVideoActivity.videoView.getHolder());
//                mVideoActivity.mediaPlayer.prepareAsync();
//            }


        }

        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }


    public void setData() {
        try {
//            if(ExhibitionApplication.mApp.video == null)
//            {
//                mVideoActivity.videoView.setVisibility(View.INVISIBLE);
//                //给MediaPlayer设置播放源
//
//            }
//            else
//            {
//                mVideoActivity.mediaPlayer.setDataSource(ExhibitionApplication.mApp.video.getPath());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void releasePlayer() {
        if (mVideoActivity.mediaPlayer != null) {
            mVideoActivity.mediaPlayer.stop();
            mVideoActivity.mediaPlayer.reset();
            mVideoActivity.mediaPlayer.release();
            mVideoActivity.mediaPlayer = null;
        }

    }



    public void updateView() {
        ArrayList<View> views = new ArrayList<View>();
        if (RestaurantApplication.mApp.bg.size() > 0) {
            for (Map.Entry<String ,String> bg : RestaurantApplication.mApp.bg.entrySet()) {
                View view = mVideoActivity.getLayoutInflater().inflate(R.layout.gally_image, null);
                ImageView imageView = view.findViewById(R.id.photo);
                String path = bg.getValue();
//                Glide.with(mVideoActivity).load(new File(path)).into(imageView);
                RequestOptions options = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                GlideApp.with(mVideoActivity).load(path).apply(options).into(new MySimpleTarget(imageView));
                views.add(view);
                view.setOnClickListener(loginListener);
            }
            mVideoActivity.mViewPagerAdapter = new GallyPageAdapter(mVideoActivity,views);
            mVideoActivity.viewPager.setVisibility(View.VISIBLE);
            mVideoActivity.viewPager.setOffscreenPageLimit(3);
            mVideoActivity.viewPager.setPageTransformer(true, new DepthPageTransformer());
            mVideoActivity.viewPager.setAdapter(mVideoActivity.mViewPagerAdapter);
            setViewPagerScrollSpeed();
            mVideoActivity.viewPager.setCurrentItem(0);
        }
    }

    public void startLogin() {
        RestaurantApplication.mApp.resetFirst();
        mVideoActivity.finish();
    }

    public void updataGally()
    {
        if(mVideoActivity.mViewPagerAdapter != null)
        {
            if(mVideoActivity.mViewPagerAdapter.getCount() > 0)
            {
                int current = mVideoActivity.viewPager.getCurrentItem();
                if(current == mVideoActivity.mViewPagerAdapter.getCount()-1)
                {
                    mVideoActivity.viewPager.setCurrentItem(0,false);
                }
                else
                {
                    mVideoActivity.viewPager.setCurrentItem(current+1);
                }
            }
        }

        videoHandler.sendEmptyMessageDelayed(VideoHandler.UPDATA_GALLY_TIME,mVideoActivity.changeTime*3000);
    }


    private void setViewPagerScrollSpeed() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mVideoActivity.viewPager.getContext());
            mScroller.set(mVideoActivity.viewPager, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }

}
