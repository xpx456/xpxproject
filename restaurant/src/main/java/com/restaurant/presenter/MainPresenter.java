package com.restaurant.presenter;


import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.restaurant.R;
import com.restaurant.handler.MainHandler;
import com.restaurant.view.RestaurantApplication;
import com.restaurant.view.SuccessView;
import com.restaurant.view.activity.MainActivity;

import java.net.PortUnreachableException;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
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
        mMainActivity.time = mMainActivity.findViewById(R.id.timevalue);
        mMainActivity.type = mMainActivity.findViewById(R.id.typevalue);
        mMainActivity.setting = mMainActivity.findViewById(R.id.setting);
        mMainActivity.setting.setOnClickListener(settingListener);
        mMainActivity.successView = new SuccessView(mMainActivity);
        RestaurantApplication.mApp.myMqttService.startService(mMainActivity,RestaurantApplication.mApp.clidenid);
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
