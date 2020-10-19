package com.accesscontrol.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.accesscontrol.asks.DeviceAsks;
import com.accesscontrol.asks.MqttAsks;
import com.accesscontrol.prase.HttpPrase;
import com.accesscontrol.view.AccessControlApplication;

import intersky.xpxnet.net.NetObject;


//01


public class AppHandler extends Handler {

    public static final int SET_TIME = 100001;
    public static final int START_MQTT = 9999;
    public static final int INIT_MQTT = 9998;
    public static final int ADD_GUEST = 100002;
    public static final int UPDATA_GUEST = 100003;
    public static final int DELETE_GUEST = 100004;
    public static final int OPEN_DOOR = 100006;
    public static final int ALWAYS_OPEN_DOOR = 100007;
    public static final int CLOSE_DOOR = 100008;
    public static final int ALWAYS_CLOSE_DOOR = 100009;
    public static final int RESET_PROTECT_TIME = 100010;
    public static final int SHOW_VIEW = 100011;
    public static final int MQTT_CONNECTED = 100012;
    public static final int ADD_MASTER = 100013;
    public static final int SEND_IMF = 100014;
    public static final int CLOSE_ALL_LIGHT = 100015;
    public static final int FINGER_FAIL = 1000016;
    public static final int SHOW_FIRST_TIME = 100017;
    public static final int STOP_PROTECT = 1000178;
    public static final int SET_DELY = 1000179;
    public static final int CHECK_UPDATA = 1000180;
    public static final int INIT_USB_DEVICE = 1000181;
    public static final int SET_PASSOWRD = 1000182;
    public static final int STOP_SHOW_FIRST = 1000183;
    public static final int SET_CHAT_SHOW = 1000184;
    public static final int SET_CHAT_RES = 1000185;
    public static final int BACK_LIVE = 1000186;
    public AppHandler() {
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case SET_TIME:
                AccessControlApplication.mApp.settime((Intent) msg.obj);
                break;
            case START_MQTT:
                AccessControlApplication.mApp.startMqtt();
                break;
            case INIT_MQTT:
                AccessControlApplication.mApp.initMqtt();
                break;
            case MQTT_CONNECTED:
                AccessControlApplication.mApp.mqttStarted();
                break;
            case ADD_GUEST:
            case UPDATA_GUEST:
                AccessControlApplication.mApp.addGuest((Intent) msg.obj);
                break;
            case DELETE_GUEST:
                AccessControlApplication.mApp.deleteGuest((Intent) msg.obj);
                break;
            case OPEN_DOOR:
                AccessControlApplication.mApp.openDoor();
                break;
            case CLOSE_DOOR:
                AccessControlApplication.mApp.closeDoor();
                break;
            case ALWAYS_OPEN_DOOR:
                AccessControlApplication.mApp.alwaysOpen((Intent) msg.obj);
                break;
            case ALWAYS_CLOSE_DOOR:
                AccessControlApplication.mApp.alwaysClose((Intent) msg.obj);
                break;
            case SHOW_FIRST_TIME:
                AccessControlApplication.mApp.updataProtectTime();
                break;
            case RESET_PROTECT_TIME:
                AccessControlApplication.mApp.resetProtect();
                break;
            case SHOW_VIEW:
                AccessControlApplication.mApp.showView((Intent) msg.obj);
                break;
            case DeviceAsks.EVENT_GET_LOCATION_SUCCESS:
                HttpPrase.praseLocation((NetObject) msg.obj,AccessControlApplication.mApp);
                break;
            case SEND_IMF:
                if(AccessControlApplication.mApp.canConnect())
                MqttAsks.sendDeviceInfo(AccessControlApplication.mApp,AccessControlApplication.mApp.aPublic);
                break;
            case CLOSE_ALL_LIGHT:
                AccessControlApplication.mApp.closeLight(msg);
                break;
            case SET_DELY:
                AccessControlApplication.mApp.setDely((Intent) msg.obj);
                break;
            case DeviceAsks.EVENT_GET_UPDATA_INFO:
                AccessControlApplication.mApp.updateUrls((NetObject) msg.obj);
                break;
            case CHECK_UPDATA:
                AccessControlApplication.mApp.checkUpdata();
                break;
            case DeviceAsks.EVENT_GET_DEVICE_SUCCESS:
                AccessControlApplication.mApp.updataDevice(HttpPrase.praseDevice((NetObject) msg.obj,AccessControlApplication.mApp));
                break;
            case INIT_USB_DEVICE:
                AccessControlApplication.mApp.initDevice();
                break;
            case SET_PASSOWRD:
                AccessControlApplication.mApp.setPassowrd((Intent) msg.obj);
                break;
            case STOP_SHOW_FIRST:
                AccessControlApplication.mApp.stopShowFirst((Boolean) msg.obj);
                break;
            case SET_CHAT_SHOW:
                AccessControlApplication.mApp.isshow = (boolean) msg.obj;
                break;
            case SET_CHAT_RES:
                AccessControlApplication.mApp.chatresource = (boolean) msg.obj;
                break;
            case BACK_LIVE:
                AccessControlApplication.mApp.liveBack((Intent) msg.obj);
                break;
        }


    }
}
