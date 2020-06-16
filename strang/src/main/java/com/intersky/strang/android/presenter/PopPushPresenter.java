package com.intersky.strang.android.presenter;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.intersky.strang.R;
import com.intersky.strang.android.entity.IntersakyData;
import com.intersky.strang.android.handler.PopPushHandler;
import com.intersky.strang.android.handler.SplashHandler;
import com.intersky.strang.android.view.StrangApplication;
import com.intersky.strang.android.view.activity.LoginActivity;
import com.intersky.strang.android.view.activity.PopPushActivity;
import com.intersky.strang.android.view.activity.SafeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.conversation.entity.NotificationData;

public class PopPushPresenter implements Presenter {

    public PopPushActivity mPopPuseActivity;
    public PopPushHandler mPopPushHandler;

    public PopPushPresenter(PopPushActivity mSplashActivity) {
        this.mPopPuseActivity = mSplashActivity;
        mPopPushHandler = new PopPushHandler(mSplashActivity);
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        mPopPuseActivity.setContentView(R.layout.activity_splash);
        mPopPuseActivity.shade = mPopPuseActivity.findViewById(R.id.shade);
        mPopPuseActivity.popupWindow = mPopPuseActivity.findViewById(R.id.viewa);
        View view = mPopPuseActivity.popupWindow;
        TextView mtv = (TextView) view.findViewById(R.id.a111);
        SpannableString spanStrStart = new SpannableString(mPopPuseActivity.getString(R.string.safe_message));
        SpannableString spanStrClick = new SpannableString("《畅享云用户协议和隐私声明》");
        spanStrClick.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(mPopPuseActivity, SafeActivity.class);
                mPopPuseActivity.startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE); //设置颜色
                //去掉下划线，默认是带下划线的
                ds.setUnderlineText(false);
                //设置字体背景
//				ds.bgColor = Color.parseColor("#FF0000");
            }
        }, 0, spanStrClick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString spanStrEnd = new SpannableString(mPopPuseActivity.getString(R.string.safe_message_end));
        mtv.append(spanStrStart);
        mtv.append(spanStrClick);
        mtv.append(spanStrEnd);
        mtv.setMovementMethod(LinkMovementMethod.getInstance());
        TextView button1 = view.findViewById(R.id.later);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPopPuseActivity.popupWindow != null)
                {
                    mPopPuseActivity.popupWindow.setVisibility(View.INVISIBLE);
                    mPopPuseActivity.shade.setVisibility(View.INVISIBLE);
                }
                mPopPuseActivity.finish();
            }
        });
        TextView button2 = view.findViewById(R.id.commit);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPopPuseActivity.popupWindow != null)
                {
                    mPopPuseActivity.popupWindow.setVisibility(View.INVISIBLE);
                    mPopPuseActivity.shade.setVisibility(View.INVISIBLE);
                }
                SharedPreferences sharedPre = mPopPuseActivity.getSharedPreferences("BASE", 0);
                SharedPreferences.Editor e = sharedPre.edit();
                e.putBoolean("FIRST_USE",false);
                e.commit();
                StrangApplication.mApp.first = false;
                AppUtils.getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, mPopPuseActivity, SplashHandler.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE, mPopPushHandler);
            }
        });

        if(StrangApplication.mApp.first == false)
            AppUtils.getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, mPopPuseActivity, SplashHandler.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE, mPopPushHandler);
        else {
            mPopPuseActivity.shade.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }


    @Override
    public void Resume() {
        // TODO Auto-generated method stub
//		MobclickAgent.onResume(mSplashActivity);
    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub
//		MobclickAgent.onPause(mSplashActivity);
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        mPopPushHandler = null;
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }


    public void startLogin()
    {


        Intent mainIntent = new Intent(mPopPuseActivity,
                LoginActivity.class);
        String type= "";
        String id = "";
        String json = "";
        if(mPopPuseActivity.map != null){
             json = mPopPuseActivity.map.get("data");
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject msg = jsonObject.getJSONObject("message");

                if (msg.has("source_type")) {
                    if (msg.getString("source_type").toLowerCase().contains("iweb[api]")) {
                        Intent intent = new Intent();
                        intent.putExtra("json", jsonObject.toString());
                    }
                    else if (msg.getString("source_type").toLowerCase().contains("iweb")||msg.getString("source_type").toLowerCase().contains("icloud")) {
                        String token = "";
                        if (msg.getString("source_type").toLowerCase().contains("iweb[system]") && msg.getInt("module") == 100) {
                            token = msg.getString("module_id");
                        }
                        else if (msg.getString("source_type").toLowerCase().equals("iweb[im]") || msg.getString("source_type").toLowerCase().equals("iweb[mail]")
                                || msg.getString("source_type").toLowerCase().equals("iweb[workflow]") || msg.getString("source_type").toLowerCase().equals("icloud[workflow]")
                                || msg.getString("source_type").toLowerCase().equals("icloud[mail]")|| msg.getString("source_type").toLowerCase().equals("icloud[im]")) {
                            NotificationData notificationData = null;
                            if(msg.getString("source_type").toLowerCase().equals("iweb[im]") || msg.getString("source_type").toLowerCase().equals("icloud[im]")) {
                                id = msg.getString("source_id");
                                type = IntersakyData.CONVERSATION_TYPE_MESSAGE;
                            }
                            else if(msg.getString("source_type").toLowerCase().equals("iweb[workflow]") || msg.getString("source_type").toLowerCase().equals("icloud[workflow]"))
                            {
                                id = msg.getString("module_id");
                                type = IntersakyData.CONVERSATION_TYPE_IWEB_APPROVE;
                            }
                            else if(msg.getString("source_type").toLowerCase().equals("iweb[mail]")|| msg.getString("source_type").toLowerCase().equals("icloud[mail]"))
                            {
                                id = msg.getString("module_id");
                                type = IntersakyData.CONVERSATION_TYPE_IWEB_MAIL;
                            }
                        }
                    }
                    else if (msg.getString("source_type").equals("oa")) {
                        NotificationData notificationData = null;
                        type = msg.getString("module");
                        if(type.toLowerCase().contains("report"))
                        {
                            id = msg.getString("module_id");
                            type = IntersakyData.CONVERSATION_TYPE_REPORT;
                        }
                        else if(type.toLowerCase().contains("leave"))
                        {
                            id = msg.getString("module_id");
                            type = IntersakyData.CONVERSATION_TYPE_LEAVE;
                        }
                        else if(type.toLowerCase().contains("vote"))
                        {
                            id = msg.getString("module_id");
                            type = IntersakyData.CONVERSATION_TYPE_VOTE;
                        }
                        else if(type.toLowerCase().contains("notice"))
                        {
                            id = msg.getString("module_id");
                            type = IntersakyData.CONVERSATION_TYPE_NOTICE;
                        }
                        else if(type.toLowerCase().contains("task"))
                        {
                            id = msg.getString("module_id");
                            type = IntersakyData.CONVERSATION_TYPE_TASK;
                        }
                        else
                        {
                            id = msg.getString("module_id");
                            type = IntersakyData.CONVERSATION_TYPE_GROP_MESSAGE;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mainIntent.putExtra("data",
                    mPopPuseActivity.map.get("data"));
        }
        if(type.length() > 0)
        {
            mainIntent.putExtra("type",type);
            mainIntent.putExtra("detialid",id);
            mainIntent.putExtra("json",json);
        }
        //AppUtils.showMessage(mSplashActivity,"id="+id+"type="+type);
        mPopPuseActivity.startActivity(mainIntent);
        mPopPuseActivity.finish();
    }




}
