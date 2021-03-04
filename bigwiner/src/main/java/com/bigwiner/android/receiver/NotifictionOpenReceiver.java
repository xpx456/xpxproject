package com.bigwiner.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.SplashActivity;

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
            if(intent.hasExtra("json"))
            {
                JSONObject jsonObject = new JSONObject(intent.getStringExtra("json"));
                JSONObject msg = jsonObject.getJSONObject("message");
                String type = jsonObject.getString("module");
                String id = "";
                if(AppUtils.isAppAlive(context, "com.bigwiner")){
                    Intent intent1 = new Intent();
                    Intent intent2 = new Intent();
                    intent1.setClassName("com.bigwiner","LoginActivity");
                    intent2.setClassName("com.bigwiner","MainActivity");
                    if(intent1.resolveActivity(context.getPackageManager()) == null)
                    {
                        Intent start = new Intent(context, SplashActivity.class).setAction(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        start.putExtra("typemodule",type);
                        if(type.toLowerCase().contains("membermanager"))
                        {
                            id = msg.getString("source_id");
                            start.putExtra("type","msg_message");
                        }
                        else if(type.toLowerCase().contains("newsmanage"))
                        {
                            id = msg.getString("module_id");
                            start.putExtra("type","msg_news");
                        }
                        else if(type.toLowerCase().contains("conferencemanage"))
                        {
                            id = msg.getString("module_id");
                            start.putExtra("type","msg_meeting");
                        }
                        else if(type.toLowerCase().contains("resourcesinfo"))
                        {
                            id = msg.getString("module_id");
                            start.putExtra("type","msg_resource");
                        }
                        start.putExtra("detialid",id);
                        context.startActivity(start);
                    }
                    else if(intent2.resolveActivity(context.getPackageManager()) == null)
                    {

                    }
                    else
                    {
                        if(type.toLowerCase().contains("membermanager")) {

                            Intent start = new Intent(ACTION_NOTIFICTION_OPEN_ACTIVITY);
                            start.putExtra("type","msg_message");
                            id = msg.getString("source_id");
                            start.putExtra("detialid",id);
                            start.putExtra("typemodule",type);
                            start.setPackage(BigwinerApplication.mApp.getPackageName());
                            context.sendBroadcast(start);
                        }
                        else if(type.toLowerCase().contains("newsmanage"))
                        {
                            Intent start = new Intent(ACTION_NOTIFICTION_OPEN_ACTIVITY);
                            start.putExtra("type","msg_news");
                            id = jsonObject.getString("module_id");
                            start.putExtra("detialid",id);
                            start.putExtra("typemodule",type);
                            start.setPackage(BigwinerApplication.mApp.getPackageName());
                            context.sendBroadcast(start);
                        }
                        else if(type.toLowerCase().contains("conferencemanage"))
                        {
                            Intent start = new Intent(ACTION_NOTIFICTION_OPEN_ACTIVITY);
                            start.putExtra("type","msg_meeting");
                            id = jsonObject.getString("module_id");
                            start.putExtra("detialid",id);
                            start.putExtra("typemodule",type);
                            start.setPackage(BigwinerApplication.mApp.getPackageName());
                            context.sendBroadcast(start);
                        }
                        else if(type.toLowerCase().contains("resourcesinfo"))
                        {
                            Intent start = new Intent(ACTION_NOTIFICTION_OPEN_ACTIVITY);
                            start.putExtra("type","msg_resource");
                            id = jsonObject.getString("module_id");
                            start.putExtra("detialid",id);
                            start.putExtra("typemodule",type);
                            start.setPackage(BigwinerApplication.mApp.getPackageName());
                            context.sendBroadcast(start);
                        }
                    }
                }
                else
                {
                    Intent start = new Intent(context, SplashActivity.class).setAction(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    start.putExtra("typemodule",type);
                    if(type.toLowerCase().contains("membermanager"))
                    {
                        id = msg.getString("source_id");
                        start.putExtra("type","msg_message");
                    }
                    else if(type.toLowerCase().contains("newsmanage"))
                    {
                        id = jsonObject.getString("module_id");
                        start.putExtra("type","msg_news");
                    }
                    else if(type.toLowerCase().contains("conferencemanage"))
                    {
                        id = jsonObject.getString("module_id");
                        start.putExtra("type","msg_meeting");
                    }
                    else if(type.toLowerCase().contains("resourcesinfo"))
                    {
                        id = jsonObject.getString("module_id");
                        start.putExtra("type","msg_resource");
                    }
                    start.putExtra("detialid",id);
                    context.startActivity(start);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
