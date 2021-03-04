package com.dk.dkhome.entity;

public  class BlueEvent {

    public static final int UPDATE_STEP_UI_MSG = 0;
    public static final int UPDATE_SLEEP_UI_MSG = 1;
    public static final int DISCONNECT_MSG = 18;
    public static final int CONNECTED_MSG = 19;
    public static final int UPDATA_REAL_RATE_MSG = 20;
    public static final int RATE_SYNC_FINISH_MSG = 21;
    public static final int OPEN_CHANNEL_OK_MSG = 22;
    public static final int CLOSE_CHANNEL_OK_MSG = 23;
    public static final int TEST_CHANNEL_OK_MSG = 24;
    public static final int OFFLINE_SWIM_SYNC_OK_MSG = 25;
    public static final int UPDATA_REAL_BLOOD_PRESSURE_MSG = 29;
    public static final int OFFLINE_BLOOD_PRESSURE_SYNC_OK_MSG = 30;
    public static final int SERVER_CALL_BACK_OK_MSG = 31;
    public static final int OFFLINE_SKIP_SYNC_OK_MSG = 32;
    public static final int test_mag1 = 35;
    public static final int test_mag2 = 36;
    public static final int OFFLINE_STEP_SYNC_OK_MSG = 37;
    public static final int UPDATE_SPORTS_TIME_DETAILS_MSG = 38;
    public static final int NEW_DAY_MSG = 3;
    public static final int UNIVERSAL_INTERFACE_SDK_TO_BLE_SUCCESS_MSG = 39;//sdk发送数据到ble完成，并且校验成功，返回状态
    public static final int UNIVERSAL_INTERFACE_SDK_TO_BLE_FAIL_MSG = 40;   //sdk发送数据到ble完成，但是校验失败，返回状态
    public static final int UNIVERSAL_INTERFACE_BLE_TO_SDK_SUCCESS_MSG = 41;//ble发送数据到sdk完成，并且校验成功，返回数据
    public static final int UNIVERSAL_INTERFACE_BLE_TO_SDK_FAIL_MSG = 42;   //ble发送数据到sdk完成，但是校验失败，返回状态


    public static final int SHOW_SET_PASSWORD_MSG = 26;
    public static final int SHOW_INPUT_PASSWORD_MSG = 27;
    public static final int SHOW_INPUT_PASSWORD_AGAIN_MSG = 28;

    public static final int RATE_OF_24_HOUR_SYNC_FINISH_MSG = 43;


}
