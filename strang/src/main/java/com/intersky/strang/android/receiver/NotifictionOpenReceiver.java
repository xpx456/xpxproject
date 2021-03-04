package com.intersky.strang.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.intersky.strang.android.view.activity.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import intersky.apputils.AppUtils;

public class NotifictionOpenReceiver extends BroadcastReceiver {

    public static final String ACTION_NOTIFICTION_OPEN_ACTIVITY = "ACTION_NOTIFICTION_OPEN_ACTIVITY";

    @Override
    public void onReceive(Context context, Intent intent) {
        doActicity(context,intent);
    }


    public void doActicity(Context context, Intent intent) {
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("json"));
            JSONObject msg = jsonObject.getJSONObject("message");
//            String type = msg.getString("module");
            String module = intent.getStringExtra("module");
            String id = msg.getString("module_id");
            if(msg.getString("source_type").toLowerCase().equals("iweb[im]") || msg.getString("source_type").toLowerCase().equals("icloud[im]"))
                 id = msg.getString("source_id");

            if(AppUtils.isAppAlive(context, "com.intersky")){
                Intent intent1 = new Intent();
                Intent intent2 = new Intent();
                intent1.setClassName("com.intersky","LoginActivity");
                intent2.setClassName("com.intersky","MainActivity");
                if(intent1.resolveActivity(context.getPackageManager()) == null)
                {
                    Intent start = new Intent(context, SplashActivity.class).setAction(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    start.putExtra("type",module);
                    start.putExtra("detialid",id);
                    start.putExtra("json",intent.getStringExtra("json"));
                    context.startActivity(start);
                }
                else if(intent2.resolveActivity(context.getPackageManager()) == null)
                {

                }
                else
                {
                    Intent start = new Intent(ACTION_NOTIFICTION_OPEN_ACTIVITY);
                    start.putExtra("type",module);
                    start.putExtra("detialid",id);
                    start.putExtra("json",intent.getStringExtra("json"));
                    context.sendBroadcast(start);
                }
            }
            else
            {
                Intent start = new Intent(context, SplashActivity.class).setAction(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                start.putExtra("type",module);
                start.putExtra("detialid",id);
                start.putExtra("json",intent.getStringExtra("json"));
                context.startActivity(start);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
