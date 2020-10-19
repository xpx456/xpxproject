package com.restaurant.asks;

import android.content.Context;

import com.restaurant.MqttManager;
import com.restaurant.entity.AccessRecord;

import org.json.JSONException;
import org.json.JSONObject;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.nettask.MqttTask;

public class MqttAsks {
    public static void uploadRecord(Context context, AccessRecord accessRecord, MqttTask.Public send) {

        String url = MqttManager.TOPIC;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("entEqptNo",accessRecord.entEqptNo);
            jsonObject.put("empAuthInfoId",accessRecord.empAuthInfoId);
            jsonObject.put("accessRecordTime",accessRecord.accessRecordTime);
            jsonObject.put("authModeCode",accessRecord.authModeCode);
            jsonObject.put("messageId", msgid);
            jsonObject.put("routeCmd",MqttManager.PATH_UPLOD_ACCESS_RECORD);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
