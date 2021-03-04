package com.dk.dkphone.presenter;


import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.dkphone.R;
import com.dk.dkphone.handler.SplashHandler;
import com.dk.dkphone.view.DkPhoneApplication;
import com.dk.dkphone.view.activity.MainActivity;
import com.dk.dkphone.view.activity.SplashActivity;

import java.io.File;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;


public class SplashPresenter implements Presenter {

    public SplashActivity mSplashActivity;
    public SplashHandler mSplashHandler;
    public float speed = 1;
    public TextView title;
    public SplashPresenter(SplashActivity mSplashActivity) {
        this.mSplashActivity = mSplashActivity;
        mSplashHandler = new SplashHandler(mSplashActivity);
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        mSplashActivity.setContentView(R.layout.activity_splash);
        ImageView logo = mSplashActivity.findViewById(R.id.logo);

        title = mSplashActivity.findViewById(R.id.title);

//        mSplashActivity.videoView = mSplashActivity.findViewById(R.id.video);
//        mSplashActivity.mediaPlayer = new MediaPlayer();
//        mSplashActivity.mediaPlayer.setOnPreparedListener(mPrepareListener);
//        mSplashActivity.mediaPlayer.setOnCompletionListener(mOnCompletionListener);
//        setData();
//        mSplashActivity.videoView.getHolder().setKeepScreenOn(true);
//        mSplashActivity.videoView.getHolder().addCallback(new SurfaceCallback());
//        ImageView add = mSplashActivity.findViewById(R.id.add);
//        ImageView des = mSplashActivity.findViewById(R.id.des);
//        title.setText("1倍速");
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                speed+= 0.5;
//                title.setText(String.valueOf(speed)+"倍速");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    mSplashActivity.mediaPlayer.setPlaybackParams(mSplashActivity.mediaPlayer.getPlaybackParams().setSpeed(speed));
//                }
//            }
//        });
//        des.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(speed > 0.5)
//                {
//                    speed-=0.5;
//                }
//                title.setText(String.valueOf(speed)+"倍速");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    mSplashActivity.mediaPlayer.setPlaybackParams(mSplashActivity.mediaPlayer.getPlaybackParams().setSpeed(speed));
//                }
//            }
//        });

        if(DkPhoneApplication.mApp.LOGO_MODE)
        {
            logo.setVisibility(View.VISIBLE);

        }
        else
            logo.setVisibility(View.INVISIBLE);
        AppUtils.getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, mSplashActivity, SplashHandler.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE, mSplashHandler);


    }


    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }


    @Override
    public void Resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        mSplashHandler = null;
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }


    public void startMain()
    {
        Intent mainIntent = new Intent(mSplashActivity,
                MainActivity.class);
        mSplashActivity.startActivity(mainIntent);
        mSplashActivity.finish();
//        startLogin();
    }


    public void setData()
    {
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/dkphone");
        if(file.exists() == false)
        {
            file.mkdirs();
        }
        try {
            File f = new File(file.getPath()+"/"+ DkPhoneApplication.VIDEO_NAME);
            if(f.exists())
            mSplashActivity.mediaPlayer.setDataSource(f.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public MediaPlayer.OnPreparedListener mPrepareListener = new MediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer mp) {
//            mSplashActivity.mediaPlayer.start();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mSplashActivity.mediaPlayer.setPlaybackParams(mSplashActivity.mediaPlayer.getPlaybackParams().setSpeed(1));
            }
        }
    };

    public MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener()
    {

        @Override
        public void onCompletion(MediaPlayer mp) {
            mSplashActivity.mediaPlayer.seekTo(0);
            mSplashActivity.mediaPlayer.start();

        }
    };

    public View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };




    private final class SurfaceCallback implements SurfaceHolder.Callback {
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }
        public void surfaceCreated(SurfaceHolder holder) {
            mSplashActivity.mediaPlayer.setDisplay(mSplashActivity.videoView.getHolder());
            mSplashActivity.mediaPlayer.prepareAsync();


        }
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }
}
