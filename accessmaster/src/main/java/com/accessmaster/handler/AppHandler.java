package com.accessmaster.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.accessmaster.asks.DeviceAsks;
import com.accessmaster.asks.MqttAsks;
import com.accessmaster.prase.HttpPrase;
import com.accessmaster.view.AccessMasterApplication;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;


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
    public static final int SHOW_FIRST_TIME = 100017;
    public static final int OPEN_CONTACT = 100011;
    public static final int MQTT_CONNECTED = 100012;
    public static final int ADD_DEVICE = 100013;
    public static final int SEND_MASTER = 100014;
    public static final int SET_DELY = 1000179;
    public static final int CHECK_UPDATA = 1000180;
    public static final int SET_PASSOWRD = 1000182;
    public static final int SET_CHAT = 100183;
    public static final int REMOVE_DEVICE = 100016;
    public static final int LIVE_BACK = 100186;
    public static final int CHECK_DEVICE = 110005;
    public static final int CHECK_SIMPLE_DEVICE = 110006;
    public static final int SET_CHAT_SHOW = 1000184;
    public static final int SET_CHAT_RES = 1000185;
    public static final int SEN_MQTT= 1000188;
    public AppHandler() {
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case SET_TIME:
                AccessMasterApplication.mApp.settime((Intent) msg.obj);
                break;
            case START_MQTT:
                AccessMasterApplication.mApp.startMqtt();
                break;
            case INIT_MQTT:
                AccessMasterApplication.mApp.initMqtt();
                break;
            case MQTT_CONNECTED:
                AccessMasterApplication.mApp.mqttStarted();
                break;
            case ADD_DEVICE:
                AccessMasterApplication.mApp.addDevice((Intent) msg.obj);
                break;
            case REMOVE_DEVICE:
                AccessMasterApplication.mApp.removeDevice((Intent) msg.obj);
                break;
            case ADD_GUEST:
            case UPDATA_GUEST:
                AccessMasterApplication.mApp.addGuest((Intent) msg.obj);
                break;
            case DELETE_GUEST:
                AccessMasterApplication.mApp.deleteGuest((Intent) msg.obj);
                break;
            case SHOW_FIRST_TIME:
                AccessMasterApplication.mApp.updataProtectTime();
                break;
            case RESET_PROTECT_TIME:
                AccessMasterApplication.mApp.resetProtect();
                break;
            case OPEN_CONTACT:
                AccessMasterApplication.mApp.showContact((Intent) msg.obj);
                break;
            case SEND_MASTER:
                MqttAsks.sendMaster(AccessMasterApplication.mApp,AccessMasterApplication.mApp.aPublic);
                break;
            case DeviceAsks.EVENT_GET_LOCATION_SUCCESS:
                HttpPrase.praseLocation((NetObject) msg.obj,AccessMasterApplication.mApp);
                break;
            case DeviceAsks.EVENT_GET_UPDATA_INFO:
                AccessMasterApplication.mApp.updateUrls((NetObject) msg.obj);
                break;
            case SET_DELY:
                AccessMasterApplication.mApp.setDely((Intent) msg.obj);
                break;
            case CHECK_UPDATA:
                AccessMasterApplication.mApp.checkUpdata();
                break;
            case SET_PASSOWRD:
                AccessMasterApplication.mApp.setPassowrd((Intent) msg.obj);
                break;
            case SET_CHAT:
                AccessMasterApplication.mApp.setshow((Boolean) msg.obj);
                break;
            case NetUtils.NO_NET_WORK:
            case NetUtils.NO_INTERFACE:
                NetObject netObject = (NetObject) msg.obj;
                if(netObject.result.contains("ent/feature/getFile"))
                {
                    if(AccessMasterApplication.mApp.appActivityManager.getCurrentActivity() != null)
                    AppUtils.showMessage(AccessMasterApplication.mApp.appActivityManager.getCurrentActivity(),"调用更新接口失败");
                }
                break;
            case SET_CHAT_SHOW:
                AccessMasterApplication.mApp.isshow = (boolean) msg.obj;
                break;
            case SET_CHAT_RES:
                AccessMasterApplication.mApp.chatresource = (boolean) msg.obj;
                break;
            case LIVE_BACK:
                AccessMasterApplication.mApp.liveBack((Intent) msg.obj);
                break;
            case CHECK_DEVICE:
                AccessMasterApplication.mApp.checkClienList();
                break;
            case CHECK_SIMPLE_DEVICE:
                AccessMasterApplication.mApp.isDeviceLive((String) msg.obj);
                break;

        }

    }
}
