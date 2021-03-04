package com.accessmaster.asks;

import android.content.Context;
import android.os.Handler;

import com.accessmaster.entity.Device;
import com.accessmaster.view.AccessMasterApplication;

import org.json.JSONException;
import org.json.JSONObject;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.NetTask;
import intersky.xpxnet.net.nettask.PostJsonNetTask;

public class DeviceAsks {

    public static final String PATH_REGISTER = "ent/ent/eqptInfo/uploadEqptRegister";
    public static final String PATH_GET_DEVICE = "ent/ent/eqptInfo/getEqptByNo";
    public static final String PATH_GET_LOCATIONS = "ent/ent/eqptInfo/getLocationTree";
    public static final String PATH_GET_UPDATE = "ent/feature/getFile";
    public static final String PATH_GET_LIVE = "ent/ent/eqptInfo/checkEqptIsOn";
    public static final int EVENT_REGISTER_SUCCESS = 200001;
    public static final int EVENT_GET_LOCATION_SUCCESS = 200002;
    public static final int EVENT_GET_DEVICE_SUCCESS = 200003;
    public static final int EVENT_GET_UPDATA_INFO = 200004;
    public static final int EVENT_GET_LIVE = 200005;

    public static void getLiveMaster(Context context, Handler handler,Device device) {
        String urlString = NetUtils.getInstance().praseUrl(AccessMasterApplication.mApp.appservice,PATH_GET_LIVE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("clientId", device.cid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String postBody = jsonObject.toString();
        PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, handler, EVENT_GET_LIVE, context, postBody,device);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getUpdataInfo(Context context, Handler handler,String type) {
        String urlString = NetUtils.getInstance().praseUrl(AccessMasterApplication.mApp.appservice,PATH_GET_UPDATE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("terminalTypeCode", type);
            jsonObject.put("resourceTypeCode", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        urlString += "?terminalTypeCode="+type+"&resourceTypeCode=";
        NetTask mPostNetTask = new NetTask(urlString, handler, EVENT_GET_UPDATA_INFO, context);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getLocation(Context context, Handler handler)
    {
        String urlString = NetUtils.getInstance().praseUrl(AccessMasterApplication.mApp.appservice,PATH_GET_LOCATIONS);
        NetTask mPostNetTask = new NetTask(urlString, handler, EVENT_GET_LOCATION_SUCCESS, context);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }


    public static void registerDevice(Context context, Handler handler, Device device,String ip)
    {
        String urlString = NetUtils.getInstance().praseUrl(AccessMasterApplication.mApp.appservice,PATH_REGISTER);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("eqptLocationId", device.addressid);
            jsonObject.put("entEqptName", device.cname);
            jsonObject.put("entEqptNo", device.cid);
            jsonObject.put("terminalTypeCode","03");
            jsonObject.put("entEqptIP", ip);
            jsonObject.put("entEqptMac", AppUtils.getLocalMacAddressFromIp(context,ip));
            jsonObject.put("isEntEqpt", "0");
            jsonObject.put("isAttendanceEqpt", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String postBody = jsonObject.toString();
        PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, handler, EVENT_REGISTER_SUCCESS, context, postBody);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getDeviceInfo(Context context, Handler handler,String cid)
    {
        String urlString = NetUtils.getInstance().praseUrl(AccessMasterApplication.mApp.appservice,PATH_GET_DEVICE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("entEqptNo", cid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String postBody = jsonObject.toString();
        PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, handler, EVENT_GET_DEVICE_SUCCESS, context, postBody);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }
}
