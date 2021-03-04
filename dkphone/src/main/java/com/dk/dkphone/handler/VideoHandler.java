package com.dk.dkphone.handler;



//01


import android.os.Handler;
import android.os.Message;

import com.dk.dkphone.view.activity.VideoActivity;

public class VideoHandler extends Handler {


    public VideoActivity theActivity;
    public static final int UPDATA_LOGO = 100000;
    public VideoHandler(VideoActivity mLoginActivity) {
        theActivity = mLoginActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {

            case UPDATA_LOGO:
                theActivity.mVideoPresenter.updatalogo();
                break;

        }

    }
}
