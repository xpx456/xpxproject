package com.intersky.android.presenter;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.intersky.R;
import com.intersky.android.database.DBHelper;
import com.intersky.android.handler.SplashHandler;
import com.intersky.android.view.InterskyApplication;
import com.intersky.android.view.activity.LoginActivity;
import com.intersky.android.view.activity.SafeActivity;
import com.intersky.android.view.activity.SplashActivity;

import intersky.appbase.Local.LocalData;
import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.task.entity.Log;
import intersky.xpxnet.net.Service;
import xpx.com.toolbar.utils.ToolBarHelper;

import static com.alibaba.mtl.appmonitor.AppMonitorDelegate.TAG;
import static com.alibaba.mtl.appmonitor.AppMonitorDelegate.register;

public class SplashPresenter implements Presenter {

    public static final String BG_URL = "/static/images/default@3x.png";
    public SplashActivity mSplashActivity;
    public SplashHandler mSplashHandler;

    public SplashPresenter(SplashActivity mSplashActivity) {
        this.mSplashActivity = mSplashActivity;
        mSplashHandler = new SplashHandler(mSplashActivity);
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        ToolBarHelper.setSutColor(mSplashActivity,Color.parseColor("#00ffffff"));
        mSplashActivity.setContentView(R.layout.activity_splash);
        mSplashActivity.shade = mSplashActivity.findViewById(R.id.shade);
        mSplashActivity.bg = mSplashActivity.findViewById(R.id.bg);
        mSplashActivity.popupWindow = mSplashActivity.findViewById(R.id.viewa);
        Service service = getLoginInfo();
        if(service != null)
        {
            Glide.with(mSplashActivity).load(getUrl(service)).into(mSplashActivity.bg);
        }

        View view = mSplashActivity.popupWindow;
        TextView mtv = (TextView) view.findViewById(R.id.a111);
        SpannableString spanStrStart = new SpannableString(mSplashActivity.getString(R.string.safe_message));
        SpannableString spanStrClick = new SpannableString(mSplashActivity.getString(R.string.icloud_specile));
        spanStrClick.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(mSplashActivity, SafeActivity.class);
                mSplashActivity.startActivity(intent);
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
        SpannableString spanStrEnd = new SpannableString(mSplashActivity.getString(R.string.safe_message_end));
        mtv.append(spanStrStart);
        mtv.append(spanStrClick);
        mtv.append(spanStrEnd);
        mtv.setMovementMethod(LinkMovementMethod.getInstance());
        TextView button1 = view.findViewById(R.id.later);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSplashActivity.popupWindow != null)
                {
                    mSplashActivity.popupWindow.setVisibility(View.INVISIBLE);
                    mSplashActivity.shade.setVisibility(View.INVISIBLE);
                }
                mSplashActivity.finish();
            }
        });
        TextView button2 = view.findViewById(R.id.commit);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSplashActivity.popupWindow != null)
                {
                    mSplashActivity.popupWindow.setVisibility(View.INVISIBLE);
                    mSplashActivity.shade.setVisibility(View.INVISIBLE);
                }
                SharedPreferences sharedPre = mSplashActivity.getSharedPreferences("BASE", 0);
                SharedPreferences.Editor e = sharedPre.edit();
                e.putBoolean("FIRST_USE",false);
                e.commit();
                InterskyApplication.mApp.first = false;
                AppUtils.getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, mSplashActivity, SplashHandler.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE, mSplashHandler);
            }
        });

        if(InterskyApplication.mApp.first == false)
            mSplashHandler.sendEmptyMessageDelayed(SplashHandler.EVENT_START_CHECK,2000);
        else {
            mSplashActivity.shade.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);

        }

    }


    public Service getLoginInfo()
    {
        SharedPreferences sharedPre = mSplashActivity.getSharedPreferences(LocalData.LOGIN_INFO, 0);
        String sServer = sharedPre.getString(LocalData.LOGIN_INFO_SERVICE_RECORDID, "");
        if(sServer.length() != 0)
        {
            Service service = DBHelper.getInstance(mSplashActivity).getServerInfo(sServer);
            if(service != null && service.sAddress.length() > 0 && service.sPort.length() > 0)
            {
                if(checkUrl(service))
                {
                    return service;
                }

            }
        }
        return null;
    }

    public boolean checkUrl(Service service)
    {
        String url  = "";
        if(service.https)
        {
            url = "https://"+service.sAddress+":"+service.sPort;
        }
        else
        {
            url = "http://"+service.sAddress+":"+service.sPort;
        }
        if(url.length() > 12)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public String getUrl(Service service) {
        String url;
        if(service.https)
        {
            url = "https://"+service.sAddress+":"+service.sPort+BG_URL;
        }
        else
        {
            url = "http://"+service.sAddress+":"+service.sPort+BG_URL;
        }
        return url;
    }

    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }


    @Override
    public void Resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        mSplashHandler = null;
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }


    public void startLogin()
    {
        Intent mainIntent = new Intent(mSplashActivity,
                LoginActivity.class);
        if(mSplashActivity.getIntent().getBundleExtra("data") != null){
            mainIntent.putExtra("data",
                    mSplashActivity.getIntent().getBundleExtra("data"));
        }
        if(mSplashActivity.getIntent().hasExtra("type"))
        {
            mainIntent.putExtra("type",mSplashActivity.getIntent().getStringExtra("type"));
            mainIntent.putExtra("detialid",mSplashActivity.getIntent().getStringExtra("detialid"));
            mainIntent.putExtra("json",mSplashActivity.getIntent().getStringExtra("json"));
        }

        mSplashActivity.startActivity(mainIntent);
        mSplashActivity.finish();
    }




}
