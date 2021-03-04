package com.restaurant.asks;

import android.content.Context;
import android.os.Handler;

import com.restaurant.entity.Guest;
import com.restaurant.view.RestaurantApplication;

import org.json.JSONException;
import org.json.JSONObject;

import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostJsonNetTask;

public class DinnerAsks {

    public static final String UPLOAD_DINNER = "ent/dinner/uploadDinnerData";
    public static final int EVENT_UPLOAD_DINNER = 10000;
    public static void sendDinner(Context mContext, Handler mHandler, String id, Guest mode) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(RestaurantApplication.mApp.appservice,UPLOAD_DINNER);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("entEqptNo", RestaurantApplication.mApp.clidenid);
            jsonObject.put("empAuthInfoId", id);
            jsonObject.put("authModeCode", mode.mode);
            jsonObject.put("dinningTime", TimeUtils.getDateAndTime());
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, EVENT_UPLOAD_DINNER, mContext, postBody,mode);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
