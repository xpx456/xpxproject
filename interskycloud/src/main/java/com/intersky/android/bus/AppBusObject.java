package com.intersky.android.bus;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.intersky.android.view.InterskyApplication;
import com.umeng.analytics.MobclickAgent;

import intersky.appbase.bus.BusObject;
import intersky.function.view.activity.WebMessageActivity;
import intersky.xpxnet.net.NetUtils;

/**
 * @author Zhenhua on 2017/12/8.
 * @email zhshan@ctrip.com ^.^
 */

public class AppBusObject extends BusObject {
    public AppBusObject(String host) {
        super(host);
    }

    @Override
    public Object doDataJob(Context context, String bizName, Object... var3) {

        if(TextUtils.equals(bizName, "app/MoPause")) {
            MobclickAgent.onPause(context);
            return null;
        }
        if(TextUtils.equals(bizName, "app/MoResume")) {
            MobclickAgent.onResume(context);
            return null;
        }
        return null;
    }
}
