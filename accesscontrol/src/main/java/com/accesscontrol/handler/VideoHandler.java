package com.accesscontrol.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.accesscontrol.R;
import com.accesscontrol.view.AccessControlApplication;
import com.accesscontrol.view.activity.VideoActivity;


//01


public class VideoHandler extends Handler {


    public VideoActivity theActivity;
    public static final int SHOW_SUCCESS_VIEW = 110000;
    public static final int UPDATA_GALLY_TIME = 110001;
    public static final int UPDATA_GALLY = 110002;
    public static final int CLOSE_VIDEO = 110003;
    public VideoHandler(VideoActivity mLoginActivity) {
        theActivity = mLoginActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {

            case SHOW_SUCCESS_VIEW:
                AccessControlApplication.mApp.showSuccessView((Intent) msg.obj,theActivity.successView,
                        theActivity.findViewById(R.id.activity_video));
                break;
            case UPDATA_GALLY_TIME:
                theActivity.mVideoPresenter.updataGally();
                break;
            case UPDATA_GALLY:
                theActivity.mVideoPresenter.updateView();
                break;
            case CLOSE_VIDEO:
                theActivity.finish();
                break;
        }

    }
}
