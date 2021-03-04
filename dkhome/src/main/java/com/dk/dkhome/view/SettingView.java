package com.dk.dkhome.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.dk.dkhome.R;
import com.dk.dkhome.presenter.MainPresenter;
import com.dk.dkhome.view.activity.AboutActivity;

import intersky.appbase.Local.LocalData;

public class SettingView {

    public MainPresenter mMainPresenter;
    public View view;
    public RelativeLayout mAbout;
    public Switch mDark;
    public TextView mLogout;
    public SettingView(MainPresenter mMainPresenter) {
        this.mMainPresenter = mMainPresenter;
        init();
    }

    public void init() {
        LayoutInflater mInflater = (LayoutInflater) mMainPresenter.mMainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.fragment_setting,null);

        this.mAbout = (RelativeLayout) view.findViewById(R.id.aboutlayer);
        this.mDark = view.findViewById(R.id.darkbtn);
        this.mLogout = (TextView) view.findViewById(R.id.exit);
        this.mDark.setOnCheckedChangeListener(onCheckedChangeListener);
        this.mAbout.setOnClickListener(mAboutListener);
        this.mLogout.setOnClickListener(mLogoutListener);
    }

    public View.OnClickListener mAboutListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainPresenter.mMainActivity, AboutActivity.class);
            mMainPresenter.mMainActivity.startActivity(intent);

        }
    };

    public View.OnClickListener mLogoutListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            System.exit(0);
        }
    };

    public CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener()
    {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferences sharedPre = mMainPresenter.mMainActivity.getSharedPreferences(LocalData.SETTING_INFO, 0);
            boolean old = sharedPre.getBoolean(LocalData.SETTING_DARK,false);
            SharedPreferences.Editor e = sharedPre.edit();
            e.putBoolean(LocalData.SETTING_DARK, isChecked);
            e.commit();
            boolean dark1 = DkhomeApplication.mApp.initdata();
        }
    };


}
