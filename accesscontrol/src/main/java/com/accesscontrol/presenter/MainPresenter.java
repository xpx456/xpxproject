package com.accesscontrol.presenter;


import android.view.View;


import com.accesscontrol.R;
import com.accesscontrol.handler.MainHandler;
import com.accesscontrol.view.AccessControlApplication;
import com.accesscontrol.view.SuccessView;
import com.accesscontrol.view.activity.MainActivity;

import intersky.appbase.Presenter;
import intersky.apputils.TimeUtils;

public class MainPresenter implements Presenter {

    public MainActivity mMainActivity;
    public MainHandler mainHandler;

    public MainPresenter(MainActivity MainActivity) {
        mMainActivity = MainActivity;
        mainHandler = new MainHandler(MainActivity);
    }

    @Override
    public void initView() {
        mMainActivity.setContentView(R.layout.activity_main);
        mMainActivity.setting.setOnClickListener(settingListener);
        mMainActivity.successView = new SuccessView(mMainActivity);
        AccessControlApplication.mApp.myMqttService.startService(mMainActivity,AccessControlApplication.mApp.clidenid);
        updataTime();
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

    public View.OnClickListener settingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    public void updataTime() {
        mMainActivity.time.setText(TimeUtils.getDateAndTime());
        mainHandler.sendEmptyMessageDelayed(MainHandler.UPDATA_TIME,1000);
    }

    public void creatSuccess() {
        mMainActivity.successView.creatView(mMainActivity.findViewById(R.id.activity_main));
        mainHandler.sendEmptyMessageDelayed(MainHandler.CLOSE_SUCCESS,8000);
    }

    public void closeSuccess()
    {
//        mMainActivity.successView.hid();
    }
}
