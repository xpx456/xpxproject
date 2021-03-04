package com.dk.dkhome.asks;


import android.content.Context;
import android.os.Handler;
import android.util.Log;


import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.ResposeResult;

import intersky.xpxnet.net.nettask.NetTask;
import okhttp3.RequestBody;

public class CourseAsks {

    public static final String PATH_SEND_VEIN_IMAGE = "http://www.intersky.com.cn/app/android.version/courseList.txt";

    public static final int EVENT_GETCOURSE_SUCCESS = 200001;

    public static void getCourse(Context context, Handler handler) {
//        String url = "http://"+service.sAddress+":"+service.sPort+"/" + PATH_SEND_VEIN_IMAGE;
        String urlString = PATH_SEND_VEIN_IMAGE;
        NetTask mPostNetTask = new NetTask(urlString, handler, EVENT_GETCOURSE_SUCCESS, context);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }


}
