package com.interskypad.presenter;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.interskypad.R;
import com.interskypad.asks.LoginAsks;
import com.interskypad.handler.SettingHandler;
import com.interskypad.view.InterskyPadApplication;
import com.interskypad.view.activity.AboutActivity;
import com.interskypad.view.activity.ServiceListActivity;
import com.interskypad.view.activity.SettingActivity;
import com.interskypad.view.activity.UpdateActivity;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;

public class SettingPresenter implements Presenter {

    public SettingActivity mSettingActivity;
    public TextView mTextView;
    public RelativeLayout serviceSetting;
    public RelativeLayout cacheData;
    public RelativeLayout about;
    public RelativeLayout exit;
    public SettingHandler mSettingHandler;

    public SettingPresenter(SettingActivity mSettingActivity) {
        this.mSettingActivity = mSettingActivity;
        this.mSettingHandler = new SettingHandler(mSettingActivity);

    }

    @Override
    public void initView() {
        mSettingActivity.setContentView(R.layout.activity_setting);
        mSettingActivity.root = mSettingActivity.findViewById(R.id.setting_layer);
        mTextView = mSettingActivity.findViewById(R.id.setting_user);
        serviceSetting = mSettingActivity.findViewById(R.id.setting_service_layer);
        cacheData = mSettingActivity.findViewById(R.id.setting_synchronous_layer);
        about = mSettingActivity.findViewById(R.id.setting_about_layer);
        exit = mSettingActivity.findViewById(R.id.setting_exit_layer);
        if(InterskyPadApplication.mApp.isLogin)
        {
            TextView title = mSettingActivity.findViewById(R.id.setting_user_text);
            title.setText("当前登录用户");
            mTextView.setText(InterskyPadApplication.mApp.mAccount.mAccountId);
            title = mSettingActivity.findViewById(R.id.setting_exit_text);
            title.setText("退出登录");
        }
        else
        {
            TextView title = mSettingActivity.findViewById(R.id.setting_user_text);
            title.setText("您还未登录");
            title = mSettingActivity.findViewById(R.id.setting_exit_text);
            title.setText("登录");
        }

        about.setOnClickListener(aboutListener);
        exit.setOnClickListener(existListener);
        serviceSetting.setOnClickListener(serviceListener);
        cacheData.setOnClickListener(cacheListener);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

    }

    public View.OnClickListener serviceListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            doService();
        }
    };

    public View.OnClickListener cacheListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            doUpdata();
        }
    };

    public View.OnClickListener aboutListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            doAbout();
        }
    };

    public View.OnClickListener existListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mSettingActivity.finish();
            if(InterskyPadApplication.mApp.isLogin)
            LoginAsks.doLogout(mSettingHandler,mSettingActivity);
            else
            {
                Intent intent = new Intent(LoginAsks.ACTION_LOAGIN_OUT);
                mSettingActivity.sendBroadcast(intent);
            }

        }
    };

    public void doService()
    {
        Intent intent = new Intent(mSettingActivity, ServiceListActivity.class);
        mSettingActivity.startActivity(intent);
    }

    public void doUpdata() {
        if(InterskyPadApplication.mApp.isLogin)
        {
            Intent intent = new Intent(mSettingActivity, UpdateActivity.class);
            mSettingActivity.startActivity(intent);
        }
        else
        {
            AppUtils.showMessage(mSettingActivity,"你还未登录，请先登录");
        }

    }

    public void doAbout()
    {
        Intent intent = new Intent(mSettingActivity, AboutActivity.class);
        mSettingActivity.startActivity(intent);
    }
}

