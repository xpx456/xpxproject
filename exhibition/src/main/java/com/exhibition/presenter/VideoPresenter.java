package com.exhibition.presenter;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.View;

import com.exhibition.R;
import com.exhibition.view.ExhibitionApplication;
import com.exhibition.view.activity.LoginActivity;
import com.exhibition.view.activity.VideoActivity;
import com.finger.FingerManger;

import java.io.IOException;

import intersky.appbase.Presenter;

public class VideoPresenter implements Presenter {

    public VideoActivity mVideoActivity;


    public VideoPresenter(VideoActivity mVideoActivity) {
        this.mVideoActivity = mVideoActivity;
    }

    @Override
    public void initView() {
        mVideoActivity.setContentView(R.layout.activity_video);
        ExhibitionApplication.mApp.videoshow = true;
        mVideoActivity.flagFillBack = false;
        mVideoActivity.videoView = mVideoActivity.findViewById(R.id.video);
        mVideoActivity.screenBtn = mVideoActivity.findViewById(R.id.screenbtn);
        mVideoActivity.mediaPlayer = new MediaPlayer();
        mVideoActivity.mediaPlayer.setOnPreparedListener(mPrepareListener);
        mVideoActivity.mediaPlayer.setOnCompletionListener(mOnCompletionListener);
        mVideoActivity.screenBtn.setOnClickListener(loginListener);
        setData();
        mVideoActivity.videoView.getHolder().setKeepScreenOn(true);
        mVideoActivity.videoView.getHolder().addCallback(new SurfaceCallback());
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
        releasePlayer();
        ExhibitionApplication.mApp.fingerManger.destory();
    }

    public MediaPlayer.OnPreparedListener mPrepareListener = new MediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer mp) {
            mVideoActivity.mediaPlayer.start();
        }
    };

    public MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener()
    {

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
            if(ExhibitionApplication.mApp.video != null)
            {

                mVideoActivity.mediaPlayer.setDisplay(mVideoActivity.videoView.getHolder());
                mVideoActivity.mediaPlayer.prepareAsync();
            }


        }
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }


    public void setData()
    {
        try {
            if(ExhibitionApplication.mApp.video == null)
            {
//                AssetManager am = mVideoActivity.getAssets();
//                try {
//                    AssetFileDescriptor afd= am.openFd("splash.mp4");
//                    mVideoActivity.mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                mVideoActivity.videoView.setVisibility(View.INVISIBLE);
                //给MediaPlayer设置播放源

            }
            else
            {
                mVideoActivity.mediaPlayer.setDataSource(ExhibitionApplication.mApp.video.getPath());
            }
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

    public void startLogin() {
        ExhibitionApplication.mApp.setTimeMax();
        ExhibitionApplication.mApp.setVideoHid();
        mVideoActivity.mediaPlayer.pause();
        Intent intent = new Intent(mVideoActivity, LoginActivity.class);
        mVideoActivity.startActivity(intent);
        mVideoActivity.finish();
    }

}
