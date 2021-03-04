package com.accessmaster.asks;

import android.content.Context;

import com.accessmaster.MqttManager;
import com.accessmaster.entity.AccessRecord;
import com.accessmaster.entity.Device;
import com.accessmaster.view.AccessMasterApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.nettask.MqttTask;

public class MqttAsks {

    public static final String ACTION_CONTACT = "ACTION_CONTACT";
    public static final String ACTION_IMF = "ACTION_IMF";
    public static final String ACTION_SHOWVIEW = "ACTION_SHOWVIEW";
    public static final String ACTION_ACCEPT = "ACTION_ACCEPT";
    public static final String ACTION_REFOUS = "ACTION_REFOUS";
    public static final String ACTION_OPEN = "ACTION_OPEN";
    public static final String ACTION_EXIST = "ACTION_EXIST";
    public static final String ACTION_ACCEPT_BACK = "ACTION_ACCEPT_BACK";
    public static final String ACTION_GET_MASTER = "ACTION_GET_MASTER";
    public static final String ACTION_SEND_MASTER = "ACTION_SEND_MASTER";
    public static final String ACTION_STOP_CONNECT = "ACTION_STOP_CONNECT";
    public static final String ACTION_ACCEPT_ICE = "ACTION_ACCEPT_ICE";
    public static final String ACTION_BUESY = "ACTION_BUESY";


    public static final String SEND_OFFER = "SEND_OFFER";
    public static final String SEND_ANSWWER = "SEND_ANSWWER";
    public static final String SEND_ICE = "SEND_ICE";
    public static final String EXIST = "EXIST";
    public static final String ASK_SEND_ICE = "ASK_SEND_ICE";
    public static final String LIVE = "LIVE";
    public static final String LIVE_BACK = "LIVE_BACK";


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

    public static void getDevices(Context context, MqttTask.Public send) {
        String url = MqttManager.TOPIC_GUEST;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", ACTION_IMF);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void sendMaster(Context context, MqttTask.Public send) {
        String url = MqttManager.TOPIC_GUEST;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", ACTION_SEND_MASTER);
            jsonObject.put("cid", AccessMasterApplication.mApp.clidenid);
            jsonObject.put("cname",AccessMasterApplication.mApp.getName());
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void connectAccept(Context context, MqttTask.Public send,String cid) {
        String url = cid+MqttManager.TOPIC_GUEST;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", ACTION_ACCEPT);
            jsonObject.put("cid", AccessMasterApplication.mApp.clidenid);
            jsonObject.put("cname", AccessMasterApplication.mApp.getName());
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void connectRefous(Context context, MqttTask.Public send,String cid) {
        String url = cid+MqttManager.TOPIC_GUEST;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", ACTION_REFOUS);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void openDoor(Context context, MqttTask.Public send,String cid) {
        String url = cid+MqttManager.TOPIC_GUEST;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", ACTION_OPEN);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doExist(Context context, MqttTask.Public send,String cid,String mid,boolean hasconnect) {
        String url = cid+MqttManager.TOPIC_GUEST;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", ACTION_EXIST);
            jsonObject.put("mid", mid);
            jsonObject.put("messageId", msgid);
            jsonObject.put("hasconnect", hasconnect);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doBuesy(Context context, MqttTask.Public send,String cid,String mid,boolean hasconnect) {
        String url = cid+MqttManager.TOPIC_GUEST;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", ACTION_BUESY);
            jsonObject.put("mid", mid);
            jsonObject.put("messageId", msgid);
            jsonObject.put("hasconnect", hasconnect);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    ///////////////////////////////////////////////////////
    public static void sendIceAgain(Context context, MqttTask.Public send,String cid)
    {
        String url = cid + MqttManager.TOPIC_GUEST;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mid", AccessMasterApplication.mApp.clidenid);
            jsonObject.put("cname", AccessMasterApplication.mApp.getName());
            jsonObject.put("action", ASK_SEND_ICE);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void showView(Context context, MqttTask.Public send,String cid) {
        String url = cid+MqttManager.TOPIC_GUEST;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", ACTION_SHOWVIEW);
            jsonObject.put("mid", AccessMasterApplication.mApp.clidenid);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);

            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void sendOffer(Context context, MqttTask.Public send,String cid,String spd) {

        String url = cid+MqttManager.TOPIC_GUEST;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", SEND_OFFER);
            jsonObject.put("mid", AccessMasterApplication.mApp.clidenid);
            jsonObject.put("spd", spd);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(), url, msgid);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }




    public static void sendIceCandidate(Context context, MqttTask.Public send,String cid, IceCandidate iceCandidate) {

        String url = cid+MqttManager.TOPIC_GUEST;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", SEND_ICE);
            jsonObject.put("mid", AccessMasterApplication.mApp.clidenid);
            jsonObject.put("label", iceCandidate.sdpMLineIndex);
            jsonObject.put("id", iceCandidate.sdpMid);
            jsonObject.put("candidate", iceCandidate.sdp);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(), url, msgid);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void askLive(Context context, MqttTask.Public send,String cid) {

        String url = cid+MqttManager.TOPIC_GUEST;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", LIVE);
            jsonObject.put("mid", AccessMasterApplication.mApp.clidenid);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(), url, msgid);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
