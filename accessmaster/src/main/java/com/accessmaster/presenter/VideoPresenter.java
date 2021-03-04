package com.accessmaster.presenter;

import android.content.Intent;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.accessmaster.R;
import com.accessmaster.handler.VideoHandler;
import com.accessmaster.receiver.VideoReceiver;
import com.accessmaster.view.AccessMasterApplication;
import com.accessmaster.view.DepthPageTransformer;
import com.accessmaster.view.SuccessView;
import com.accessmaster.view.activity.MainActivity;
import com.accessmaster.view.activity.VideoActivity;
import com.accessmaster.view.adapter.GallyPageAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import intersky.appbase.Presenter;

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
//        updateView();
//        updataGally();
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

    private void updateView() {
        if (AccessMasterApplication.mApp.bg.size() > 0) {
            for (int i = 0; i < AccessMasterApplication.mApp.bg.size(); i++) {
                View view = mVideoActivity.getLayoutInflater().inflate(R.layout.gally_image, null);
                ImageView imageView = view.findViewById(R.id.photo);
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.bg);
                Glide.with(mVideoActivity).load(AccessMasterApplication.mApp.bg.get(i)).apply(options).into(imageView);
                mVideoActivity.views.add(view);

            }
            mVideoActivity.mViewPagerAdapter = new GallyPageAdapter(mVideoActivity, mVideoActivity.views);
            mVideoActivity.viewPager.setOffscreenPageLimit(3);
            mVideoActivity.viewPager.setPageTransformer(true, new DepthPageTransformer());
            mVideoActivity.viewPager.setAdapter(mVideoActivity.mViewPagerAdapter);
            mVideoActivity.viewPager.setCurrentItem(0);
        }
    }

    public void startLogin() {
        AccessMasterApplication.mApp.resetFirst();
        Intent intent = new Intent(mVideoActivity, MainActivity.class);
        mVideoActivity.startActivity(intent);
        mVideoActivity.finish();
    }

    public void updataGally()
    {
        if(mVideoActivity.views.size() > 0)
        {
            int current = mVideoActivity.viewPager.getCurrentItem();
            if(current == mVideoActivity.views.size()-1)
            {
                mVideoActivity.viewPager.setCurrentItem(0);
            }
            else
            {
                mVideoActivity.viewPager.setCurrentItem(current+1);
            }
        }
        videoHandler.sendEmptyMessageDelayed(VideoHandler.UPDATA_GALLY_TIME,mVideoActivity.changeTime*1000);
    }

}
