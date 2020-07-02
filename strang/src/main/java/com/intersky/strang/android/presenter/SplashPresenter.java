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
import com.intersky.strang.android.handler.SplashHandler;
import com.intersky.strang.android.view.StrangApplication;
import com.intersky.strang.android.view.activity.LoginActivity;
import com.intersky.strang.android.view.activity.SafeActivity;
import com.intersky.strang.android.view.activity.SplashActivity;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;

public class SplashPresenter implements Presenter {

    public SplashActivity mSplashActivity;
    public SplashHandler mSplashHandler;

    public SplashPresenter(SplashActivity mSplashActivity) {
        this.mSplashActivity = mSplashActivity;
        mSplashHandler = new SplashHandler(mSplashActivity);
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        mSplashActivity.setContentView(R.layout.activity_splash);
        mSplashActivity.shade = mSplashActivity.findViewById(R.id.shade);
        mSplashActivity.popupWindow = mSplashActivity.findViewById(R.id.viewa);
        View view = mSplashActivity.popupWindow;
        TextView mtv = (TextView) view.findViewById(R.id.a111);
        SpannableString spanStrStart = new SpannableString(mSplashActivity.getString(R.string.safe_message));
        SpannableString spanStrClick = new SpannableString("《外贸快车CRM用户协议和隐私声明》");
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
                StrangApplication.mApp.first = false;
                AppUtils.getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, mSplashActivity, SplashHandler.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE, mSplashHandler);
            }
        });

        if(StrangApplication.mApp.first == false)
            AppUtils.getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, mSplashActivity, SplashHandler.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE, mSplashHandler);
        else {
            mSplashActivity.shade.setVisibility(View.VISIBLE);
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
