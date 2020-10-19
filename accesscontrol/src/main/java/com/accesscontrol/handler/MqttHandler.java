package com.accesscontrol.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.accesscontrol.service.MyMqttService;


//01


public class MqttHandler extends Handler {

    public static final int START_SERVICE = 110000;
    public MyMqttService mqttService;

    public MqttHandler(MyMqttService mqttService) {
        this.mqttService = mqttService;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case START_SERVICE:
                Intent intent = (Intent) msg.obj;
                mqttService.init(intent.getStringExtra("host"),intent.getStringExtra("clientid"));
                break;
        }

    }
}
