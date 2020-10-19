package com.restaurant.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.restaurant.asks.DinnerAsks;
import com.restaurant.view.RestaurantApplication;
import com.restaurant.view.activity.MainActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;

//01


public class MainHandler extends Handler {


    public MainActivity theActivity;
    public static final int UPDATA_TIME = 110000;
    public static final int CLOSE_SUCCESS = 110001;
    public static final int UPLOAD_DATA = 110002;
    public static final int FINGER_FAIL = 110003;
    public static final int UPDATA_BTN = 110005;
    public static final int UPDATA_SDCARD = 110024;
    public static final int UPDATA_SDCARD_FINISH = 110025;
    public static final int INIT_DEVICE = 11020;
    public static final int FIND_CARD_DEVICE = 110026;
    public static final int UN_FIND_CARD_DEVICE = 110027;
    public MainHandler(MainActivity mLoginActivity) {
        theActivity = mLoginActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case UPDATA_TIME:
                theActivity.mMainPresenter.updataTime();
                break;
            case CLOSE_SUCCESS:
                theActivity.mMainPresenter.closeSuccess();
                break;
            case AppHandler.FINGER_SUCCESS:
                theActivity.mMainPresenter.accessSuccess((Intent) msg.obj);
                break;
            case DinnerAsks.EVENT_UPLOAD_DINNER:
                theActivity.waitDialog.hide();
                theActivity.mMainPresenter.sendSuccess((NetObject) msg.obj);
                break;
            case UPLOAD_DATA:
                String[] data = (String[]) msg.obj;
                theActivity.mMainPresenter.senduplod(data[0],data[1]);
                break;
            case FINGER_FAIL:
                theActivity.mMainPresenter.accessFail();
                break;
            case UPDATA_BTN:
                theActivity.mMainPresenter.updataBtn();
                break;
//            case UPDATA_SDCARD:
//                AppUtils.showMessage(theActivity,"正在更新u盘数据到本地，请稍后");
//                break;
//            case UPDATA_SDCARD_FINISH:
//                AppUtils.showMessage(theActivity,"正在更新完毕");
//                break;
            case INIT_DEVICE:
                theActivity.mMainPresenter.checkInitDevice();
                break;
            case FIND_CARD_DEVICE:
                theActivity.mMainPresenter.showMessage();
                break;
            case UN_FIND_CARD_DEVICE:
                theActivity.mMainPresenter.showMessageUN();
                break;
        }

    }
}
