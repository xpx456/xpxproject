package com.accesscontrol.presenter;

import android.content.Intent;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.accesscontrol.R;
import com.accesscontrol.handler.VideoHandler;
import com.accesscontrol.receiver.VideoReceiver;
import com.accesscontrol.view.AccessControlApplication;
import com.accesscontrol.view.DepthPageTransformer;
import com.accesscontrol.view.SuccessView;
import com.accesscontrol.view.activity.MainActivity;
import com.accesscontrol.view.activity.VideoActivity;
import com.accesscontrol.view.adapter.GallyPageAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import intersky.appbase.MySimpleTarget;
import intersky.appbase.Presenter;
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
//            if(AccessControlApplication.mApp.video != null)
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
        if (AccessControlApplication.mApp.bg.size() > 0) {
            for (Map.Entry<String ,String> bg : AccessControlApplication.mApp.bg.entrySet()) {
                View view = mVideoActivity.getLayoutInflater().inflate(R.layout.gally_image, null);
                ImageView imageView = view.findViewById(R.id.photo);
                String path = bg.getValue();
                RequestOptions options = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                GlideApp.with(mVideoActivity).load(path).apply(options).into(new MySimpleTarget(imageView));
                views.add(view);
                view.setOnClickListener(loginListener);
            }
            mVideoActivity.viewPager.setVisibility(View.VISIBLE);
            mVideoActivity.mViewPagerAdapter = new GallyPageAdapter(mVideoActivity, views);
            mVideoActivity.viewPager.setOffscreenPageLimit(3);
            mVideoActivity.viewPager.setPageTransformer(true, new DepthPageTransformer());
            mVideoActivity.viewPager.setAdapter(mVideoActivity.mViewPagerAdapter);
            mVideoActivity.viewPager.setCurrentItem(0);
        }
    }

    public void startLogin() {
        AccessControlApplication.mApp.resetFirst();
        Intent intent = new Intent(mVideoActivity, MainActivity.class);
        mVideoActivity.startActivity(intent);
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

}
