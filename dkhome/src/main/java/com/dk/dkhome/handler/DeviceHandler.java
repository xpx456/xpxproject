package com.dk.dkhome.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.dk.dkhome.R;
import com.dk.dkhome.view.DkhomeApplication;
import com.dk.dkhome.view.ProgressView;
import com.dk.dkhome.view.activity.DeviceActivity;
import com.dk.dkhome.view.activity.SportDetialActivity;

import intersky.appbase.PermissionCode;


//01


public class DeviceHandler extends Handler {


    public DeviceActivity theActivity;
    public static final int UPDTATA_DEVICE_STATE = 100402;
    public static final int UPDTATA_DATA = 100403;
    public static final int SET_SPORT_TYPE = 100404;
    public static final int UPDATA_DEVICE_LIST = 100405;
    public static final int CONNECT_FINISH = 100406;
    public static final int SCAN_FINISH = 104007;
    public static final int SET_SPORT_TYPE_NAME = 100408;
    public DeviceHandler(DeviceActivity deviceActivity) {
        theActivity = deviceActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {

            case PermissionCode.PERMISSION_REQUEST_CAMERA_CAMERA:
                DkhomeApplication.mApp.startScan(theActivity,  "",this.theActivity.getString(R.string.home_scan));
                break;
            case ProgressView.UPDTAT_PERSENT:
                theActivity.mDevicePresenter.progressView.updata();
                break;
            case UPDTATA_DEVICE_STATE:
                theActivity.mDevicePresenter.updataDeviceState();
                break;
            case UPDTATA_DATA:
                theActivity.mDevicePresenter.updataData((String[]) msg.obj);
                break;
            case SET_SPORT_TYPE:
                theActivity.mDevicePresenter.deviceView.setType((Intent) msg.obj);
                break;
            case UPDATA_DEVICE_LIST:
                theActivity.mDevicePresenter.deviceView.updataDeviceList();
                break;
            case CONNECT_FINISH:
                theActivity.mDevicePresenter.progressView.success();
                break;
            case SCAN_FINISH:
                theActivity.mDevicePresenter.scanFinish((Intent) msg.obj);
                break;
            case SET_SPORT_TYPE_NAME:
                theActivity.mDevicePresenter.deviceView.setTypeName((Intent) msg.obj);
                break;
        }

    }
}
