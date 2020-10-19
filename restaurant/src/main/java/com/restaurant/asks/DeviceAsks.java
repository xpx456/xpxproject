package com.restaurant.asks;

import android.content.Context;
import android.os.Handler;


import com.restaurant.entity.Device;
import com.restaurant.view.RestaurantApplication;

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

    public static final int EVENT_REGISTER_SUCCESS = 200001;
    public static final int EVENT_GET_LOCATION_SUCCESS = 200002;
    public static final int EVENT_GET_DEVICE_SUCCESS = 200003;
    public static final int EVENT_GET_UPDATA_INFO = 200004;

    public static void getUpdataInfo(Context context, Handler handler) {
        String urlString = NetUtils.getInstance().praseUrl(RestaurantApplication.mApp.appservice,PATH_GET_UPDATE);
        JSONObject jsonObject = new JSONObject();
        urlString += "?terminalTypeCode=01"+"&resourceTypeCode=";
        NetTask mPostNetTask = new NetTask(urlString, handler, EVENT_GET_UPDATA_INFO, context);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }


    public static void getLocation(Context context, Handler handler)
    {
        String urlString = NetUtils.getInstance().praseUrl(RestaurantApplication.mApp.appservice,PATH_GET_LOCATIONS);
        NetTask mPostNetTask = new NetTask(urlString, handler, EVENT_GET_LOCATION_SUCCESS, context);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }


    public static void registerDevice(Context context, Handler handler, Device device,String ip)
    {
        String urlString = NetUtils.getInstance().praseUrl(RestaurantApplication.mApp.appservice,PATH_REGISTER);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("eqptLocationId", device.addressid);
            jsonObject.put("entEqptName", device.cname);
            jsonObject.put("entEqptNo", device.cid);
            jsonObject.put("terminalTypeCode","01");
            jsonObject.put("entEqptIP", ip);
            jsonObject.put("entEqptMac", AppUtils.getLocalMacAddressFromIp(context,ip));
            jsonObject.put("isEntEqpt", device.isaccess);
            jsonObject.put("isAttendanceEqpt", device.isattence);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String postBody = jsonObject.toString();
        PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, handler, EVENT_REGISTER_SUCCESS, context, postBody);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getDeviceInfo(Context context, Handler handler,String cid)
    {
        String urlString = NetUtils.getInstance().praseUrl(RestaurantApplication.mApp.appservice,PATH_GET_DEVICE);
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
