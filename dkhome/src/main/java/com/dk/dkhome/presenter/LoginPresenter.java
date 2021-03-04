package com.dk.dkhome.presenter;


import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;

import com.dk.dkhome.R;
import com.dk.dkhome.view.activity.LoginActivity;

import java.util.List;

import intersky.appbase.Presenter;

import static android.content.Context.ACTIVITY_SERVICE;


public class LoginPresenter implements Presenter {

    public LoginActivity mLoginActivity;
    public LoginPresenter(LoginActivity mLoginActivity) {
        this.mLoginActivity = mLoginActivity;
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        mLoginActivity.setContentView(R.layout.activity_splash);

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
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }


    public void startLogin()
    {
        mLoginActivity.finish();
    }


    private boolean isExistLoginActivity(Class<?> activity) {
        Intent intent = new Intent(mLoginActivity, activity);
        ComponentName cmpName = intent.resolveActivity(mLoginActivity.getPackageManager());
        boolean flag = false;
        if (cmpName != null) {// 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) mLoginActivity.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);//获取从栈顶开始往下查找的10个activity
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) {// 说明它已经启动了
                    flag = true;
                    break;//跳出循环，优化效率
                }
            }
        }
        return flag;//true 存在 falese 不存在
    }

}
