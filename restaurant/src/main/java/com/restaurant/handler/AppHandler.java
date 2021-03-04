package com.restaurant.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.restaurant.asks.DeviceAsks;
import com.restaurant.asks.DinnerAsks;
import com.restaurant.prase.HttpPrase;
import com.restaurant.view.RestaurantApplication;

import intersky.xpxnet.net.NetObject;


//01


public class AppHandler extends Handler {

    public static final int SET_TIME = 100001;
    public static final int START_MQTT = 9999;
    public static final int INIT_MQTT = 9998;
    public static final int ADD_GUEST = 100002;
    public static final int UPDATA_GUEST = 100003;
    public static final int DELETE_GUEST = 100004;
    public static final int FINGER_SUCCESS = 100005;
    public static final int OPEN_DOOR = 100006;
    public static final int ALWAYS_OPEN_DOOR = 100007;
    public static final int CLOSE_DOOR = 100008;
    public static final int ALWAYS_CLOSE_DOOR = 100009;
    public static final int RESET_PROTECT_TIME = 100010;
    public static final int SHOW_FIRST_TIME = 100011;
    public static final int SDCARD_CONNECT = 100012;
    public static final int SDCARD_REMOVE = 100013;
    public static final int SET_DELY = 1000179;
    public static final int CHECK_UPDATA = 1000180;
    public static final int SET_PASSOWRD = 1000182;
    public static final int INIT_USB_DEVICE = 1000181;
    public static final int CLEAN_GUEST = 1000187;
    public AppHandler() {
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case SET_TIME:
                RestaurantApplication.mApp.settime((Intent) msg.obj);
                break;
            case START_MQTT:
                RestaurantApplication.mApp.startMqtt();
                break;
            case INIT_MQTT:
                RestaurantApplication.mApp.initMqtt();
                break;
            case ADD_GUEST:
            case UPDATA_GUEST:
                RestaurantApplication.mApp.addGuest((Intent) msg.obj);
                break;
            case DELETE_GUEST:
                RestaurantApplication.mApp.deleteGuest((Intent) msg.obj);
                break;
            case CLEAN_GUEST:
                RestaurantApplication.mApp.cleanGuest((Intent) msg.obj);
                break;
            case SHOW_FIRST_TIME:
                RestaurantApplication.mApp.updataProtectTime();
                break;
            case RESET_PROTECT_TIME:
                RestaurantApplication.mApp.resetProtect();
                break;
            case DeviceAsks.EVENT_GET_LOCATION_SUCCESS:
                HttpPrase.praseLocation((NetObject) msg.obj,RestaurantApplication.mApp);
                break;
            case RestaurantApplication.EVENT_INIT_UPDATA:
                //RestaurantApplication.mApp.getUpdate();
                break;
            case SDCARD_REMOVE:
                break;
            case DeviceAsks.EVENT_GET_UPDATA_INFO:
                RestaurantApplication.mApp.updateUrls((NetObject) msg.obj);
                break;
            case SET_DELY:
                RestaurantApplication.mApp.setDely((Intent) msg.obj);
                break;
            case CHECK_UPDATA:
                RestaurantApplication.mApp.checkUpdata();
                break;
            case SET_PASSOWRD:
                RestaurantApplication.mApp.setPassowrd((Intent) msg.obj);
                break;
            case INIT_USB_DEVICE:
                RestaurantApplication.mApp.initDevice();
                break;

        }

    }
}
