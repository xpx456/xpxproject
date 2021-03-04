package com.dk.dkpad.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.dk.dkpad.view.activity.MainActivity;


//01


public class MainHandler extends Handler {


    public MainActivity theActivity;
    public static final int UPDATA_TIME = 100000;
    public static final int SHOW_SUCCESS_VIEW = 110000;
    public static final int UPDATA_VIEW = 110001;
    public static final int INIT_HOME_CHART_VIEW = 110002;
    public static final int UPDATA_OP_VIEW = 110003;
    public static final int UPDATA_HEAD_VIEW = 110004;
    public static final int UPDATA_SET_MY_VIEW = 110005;
    public static final int INIT_SPORT_CHART_VIEW = 110007;
    public static final int INIT_PERSION_CHART_VIEW = 110006;
    public static final int STOP_SCAN = 110008;
    public static final int EVENT_UPDATA = 110009;
    public static final int EVENT_FINISH = 110010;
    public static final int EVENT_ERROR = 110011;
    public static final int ADD_DEVICE = 100002;
    public static final int CLEAN_DEVICE = 100003;
    public static final int UPDATA_DEVICE = 100004;
    public static final int UPDATA_BLUETOOTH = 100005;
    public static final int SCAN_FINISH = 100006;
    public static final int AUTO_CONNECT = 100007;
    public static final int CONNECT_FAIL = 100008;
    public MainHandler(MainActivity mLoginActivity) {
        theActivity = mLoginActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {

            case UPDATA_TIME:
                theActivity.mMainPresenter.updataTime();
                break;
            case SHOW_SUCCESS_VIEW:
                break;
            case INIT_HOME_CHART_VIEW:
                if(theActivity.homeFragment != null)
                {
                    theActivity.homeFragment.initView();
                }
                break;
            case INIT_SPORT_CHART_VIEW:
                if(theActivity.detialSportFragment != null)
                {
                    theActivity.detialSportFragment.initView();
                }
                break;
            case INIT_PERSION_CHART_VIEW:
                if(theActivity.personFragment != null)
                {
                    theActivity.personFragment.initView();
                }
                break;
            case UPDATA_OP_VIEW:
                theActivity.mMainPresenter.updataOptation((Intent) msg.obj);
                break;
            case UPDATA_HEAD_VIEW:
                theActivity.mMainPresenter.updataHead((Intent) msg.obj);
                break;
            case UPDATA_SET_MY_VIEW:
                theActivity.personFragment.setMyView();
                break;
            case EVENT_UPDATA:
                theActivity.mMainPresenter.updata((String[]) msg.obj);
                break;
            case EVENT_FINISH:
                theActivity.mMainPresenter.doFinish((String[]) msg.obj);
                break;
            case EVENT_ERROR:
                theActivity.mMainPresenter.doError((String[]) msg.obj);
                break;
            case ADD_DEVICE:
            case UPDATA_DEVICE:
            case CLEAN_DEVICE:
                if(theActivity.mMainPresenter.queryView != null)
                theActivity.mMainPresenter.queryView.queryListAdapter.notifyDataSetChanged();
                break;
            case UPDATA_BLUETOOTH:
                theActivity.mMainPresenter.updateBluestate((Intent) msg.obj);
                break;
            case AUTO_CONNECT:
                theActivity.mMainPresenter.checkBlue();
                break;
        }

    }
}
