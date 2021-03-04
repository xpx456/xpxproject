package com.accessmaster.handler;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;

import androidx.core.app.ActivityCompat;

import com.accessmaster.asks.DeviceAsks;
import com.accessmaster.asks.MqttAsks;
import com.accessmaster.view.AccessMasterApplication;
import com.accessmaster.view.activity.MainActivity;

import intersky.appbase.PermissionCode;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;


//01


public class MainHandler extends Handler {


    public MainActivity theActivity;
    public static final int UPDATA_TIME = 100000;
    public static final int SHOW_SUCCESS_VIEW = 110000;
    public static final int UPDATA_VIEW = 110001;
    public static final int UPDATA_BTN = 110002;
    public static final int EXIST = 110003;
    public static final int BUSY = 110004;
    public static final int CLEAN_REFCORT = 110005;
    public static final int UPDATA_DEVICES = 110006;
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
            case UPDATA_VIEW:
                theActivity.mMainPresenter.updataView();
                break;
            case UPDATA_BTN:
                theActivity.mMainPresenter.updataBtn();
                break;
            case PermissionCode.PERMISSION_REQUEST_AUDIORECORD:
//                if (ActivityCompat.checkSelfPermission(theActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//                {
//                    AppUtils.getPermission(Manifest.permission.CAMERA, theActivity, PermissionCode.PERMISSION_REQUEST_CAMERA_CAMERA, theActivity.mMainPresenter.mainHandler);
//                }
                break;
            case DeviceAsks.EVENT_GET_LIVE:
                theActivity.mMainPresenter.checkMaster((NetObject) msg.obj);
                break;
            case EXIST:
                Intent intent = (Intent) msg.obj;
                String v = intent.getStringExtra("msg");
                AppUtils.showMessage(theActivity,intent.getStringExtra("msg"));
                break;
            case BUSY:
                AppUtils.showMessage(theActivity,"客户端刚刚进行完一次通话，正在释放资源，请稍等一会再试");
                break;
            case UPDATA_DEVICES:
                theActivity.mMainPresenter.updataGetdevices();
                break;

        }

    }
}
