package com.accesscontrol.asks;

import android.content.Context;

import com.accesscontrol.MqttManager;
import com.accesscontrol.entity.AccessRecord;
import com.accesscontrol.view.AccessControlApplication;

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
    public static final String ASK_SEND_ICE = "ASK_SEND_ICE";
    public static final String EXIST = "EXIST";
    public static final String LIVE = "LIVE";
    public static final String LIVE_BACK = "LIVE_BACK";

    public static void uploadRecord(Context context, AccessRecord accessRecord, MqttTask.Public send) {

        String url = MqttManager.TOPIC_PRO;
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



    public static void startContactBack(Context context,String mid,String mname, MqttTask.Public send) {

        String url = MqttManager.TOPIC_HOME;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cid", AccessControlApplication.mApp.clidenid);
            jsonObject.put("cname", AccessControlApplication.mApp.getName());
            jsonObject.put("caddress", "");
            jsonObject.put("mid", mid);
            jsonObject.put("mname", mname);
            jsonObject.put("action", ACTION_ACCEPT_BACK);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void sendDeviceInfo(Context context, MqttTask.Public send) {

        String url = MqttManager.TOPIC_HOME;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cid", AccessControlApplication.mApp.clidenid);
            jsonObject.put("cname", AccessControlApplication.mApp.getName());
            jsonObject.put("caddress", "");
            jsonObject.put("action", ACTION_IMF);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void getMasters(Context context, MqttTask.Public send) {

        String url = MqttManager.TOPIC_HOME;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cid", AccessControlApplication.mApp.clidenid);
            jsonObject.put("cname", AccessControlApplication.mApp.getName());
            jsonObject.put("caddress", "");
            jsonObject.put("action", ACTION_GET_MASTER);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void stopConnect(Context context, MqttTask.Public send) {

        String url = MqttManager.TOPIC_HOME;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cid", AccessControlApplication.mApp.clidenid);
            jsonObject.put("cname", AccessControlApplication.mApp.getName());
            jsonObject.put("caddress", "");
            jsonObject.put("action", ACTION_STOP_CONNECT);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void acceptIce(Context context, MqttTask.Public send) {

        String url = MqttManager.TOPIC_HOME;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cid", AccessControlApplication.mApp.clidenid);
            jsonObject.put("cname", AccessControlApplication.mApp.getName());
            jsonObject.put("caddress", "");
            jsonObject.put("action", ACTION_ACCEPT_ICE);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /////////////////////////////////////////////////////////////
    public static void sendIceAgain(Context context, MqttTask.Public send,String mid,String message)
    {
        String url = mid + MqttManager.TOPIC_HOME;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cid", AccessControlApplication.mApp.clidenid);
            jsonObject.put("cname", AccessControlApplication.mApp.getName());
            jsonObject.put("msg", message);
            jsonObject.put("action", ASK_SEND_ICE);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void clientExist(Context context, MqttTask.Public send,String mid,String message)
    {
        String url = mid + MqttManager.TOPIC_HOME;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cid", AccessControlApplication.mApp.clidenid);
            jsonObject.put("cname", AccessControlApplication.mApp.getName());
            jsonObject.put("msg", message);
            jsonObject.put("action", EXIST);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public static void startContact(Context context, MqttTask.Public send,String mid) {

        String url = mid+MqttManager.TOPIC_HOME;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cid", AccessControlApplication.mApp.clidenid);
            jsonObject.put("mid", mid);
            jsonObject.put("cname", AccessControlApplication.mApp.getName());
            jsonObject.put("caddress", "");
            jsonObject.put("action", ACTION_CONTACT);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public static void startContact(Context context, MqttTask.Public send) {

        String url = MqttManager.TOPIC_HOME;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cid", AccessControlApplication.mApp.clidenid);
            jsonObject.put("cname", AccessControlApplication.mApp.getName());
            jsonObject.put("caddress", "");
            jsonObject.put("action", ACTION_CONTACT);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public static void sendAnswer(Context context, MqttTask.Public send,String mid,String spd)
    {
        String url = mid + MqttManager.TOPIC_HOME;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cid", AccessControlApplication.mApp.clidenid);
            jsonObject.put("cname", AccessControlApplication.mApp.getName());
            jsonObject.put("spd", spd);
            jsonObject.put("action", SEND_ANSWWER);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void sendIceCandidate(Context context, MqttTask.Public send,String mid, IceCandidate iceCandidate) {

        String url = mid+MqttManager.TOPIC_HOME;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", SEND_ICE);
            jsonObject.put("mid", AccessControlApplication.mApp.clidenid);
            jsonObject.put("label", iceCandidate.sdpMLineIndex);
            jsonObject.put("id", iceCandidate.sdpMid);
            jsonObject.put("candidate", iceCandidate.sdp);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(), url, msgid);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void doBuesy(Context context, MqttTask.Public send,String cid,String mid) {
        String url = mid+MqttManager.TOPIC_HOME;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", ACTION_BUESY);
            jsonObject.put("cid", cid);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void liveBack(Context context, MqttTask.Public send,String cid,String mid) {
        String url = mid+MqttManager.TOPIC_HOME;
        String msgid = AppUtils.getguid();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", LIVE_BACK);
            jsonObject.put("cid", cid);
            jsonObject.put("messageId", msgid);
            send.doPublc(jsonObject.toString(),url,msgid);
            //NetTaskManager.getInstance().addNetTask(new MqttTask(url,null,0,context,jsonObject.toString(),msgid,send));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
