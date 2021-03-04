package com.intersky.android.view.fragment;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;

import com.intersky.R;
import com.intersky.android.handler.MainHandler;
import com.intersky.android.presenter.MainPresenter;
import com.intersky.android.view.InterskyApplication;
import com.intersky.android.view.activity.MainActivity;

import intersky.appbase.BaseFragment;
import intersky.appbase.Local.LocalData;
import intersky.apputils.AppUtils;

public class MyFragment extends BaseFragment {

    public MainActivity mMainActivity;
    public TextView mHead;
    public TextView mName;
    public TextView mNowUser;
    public TextView mLogout;
    public RelativeLayout mService;
    public RelativeLayout mClean;
    public RelativeLayout mAbout;
    public Switch mDark;

    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainActivity = (MainActivity) getActivity();
        View mView = inflater.inflate(R.layout.fragment_my, container, false);
        String name = InterskyApplication.mApp.contactManager.mAccount.mRealName;
        if(name.length() == 0)
        {
            name = InterskyApplication.mApp.contactManager.mAccount.mUserName;
        }
        this.mName = (TextView) mView.findViewById(R.id.name);
        if(!InterskyApplication.mApp.contactManager.mAccount.mOrgName.equals("(Root)"))
        {
            if(InterskyApplication.mApp.contactManager.mAccount.mOrgName.length() > 0)
                mName.setText(name+"("+InterskyApplication.mApp.contactManager.mAccount.mOrgName+")");
            else
                mName.setText(name);
        }
        else
        {
            mName.setText(name);
        }
        this.mHead = (TextView) mView.findViewById(R.id.head);
        AppUtils.setContactCycleHead(mHead,name);
        this.mNowUser = (TextView) mView.findViewById(R.id.nowusername);
        this.mNowUser.setText(name);
        this.mService = (RelativeLayout) mView.findViewById(R.id.servicelayer);
        this.mClean = (RelativeLayout) mView.findViewById(R.id.cleanlayer);
        this.mAbout = (RelativeLayout) mView.findViewById(R.id.aboutlayer);
        this.mLogout = (TextView) mView.findViewById(R.id.exit);
        this.mDark = mView.findViewById(R.id.darkbtn);
        initdata();
        this.mDark.setOnCheckedChangeListener(onCheckedChangeListener);
        this.mClean.setOnClickListener(mCleanListener);
        this.mAbout.setOnClickListener(mAboutListener);
        this.mService.setOnClickListener(mServiceListener);
        this.mLogout.setOnClickListener(mLogoutListener);
        return mView;
    }

    public void initdata()
    {
        SharedPreferences sharedPre = mMainActivity.getSharedPreferences(LocalData.SETTING_INFO, 0);
        boolean dark = sharedPre.getBoolean(LocalData.SETTING_DARK,false);
        if(dark == false)
        {
            this.mDark.setChecked(false);
        }
        else
        {
            this.mDark.setChecked(true);
        }
    }

    public void setDark(boolean dark)
    {
        SharedPreferences sharedPre = mMainActivity.getSharedPreferences(LocalData.SETTING_INFO, 0);
        boolean old = sharedPre.getBoolean(LocalData.SETTING_DARK,false);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putBoolean(LocalData.SETTING_DARK, dark);
        e.commit();
        boolean dark1 = InterskyApplication.mApp.initdata();

//        InterskyApplication.mApp.reCreatAll();
        //mMainActivity.mMainPresenter.mMainHandler.sendEmptyMessageDelayed(MainHandler.RESTART,100);
//        if(dark)
//        mMainActivity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        else
//            mMainActivity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
//        restartApplication();
        mMainActivity.mMainPresenter.mMainHandler.sendEmptyMessageDelayed(MainHandler.RESTART,500);
    }


    public CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener()
    {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setDark(isChecked);
        }
    };

    public View.OnClickListener mCleanListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mMainActivity.mMainPresenter.askClean();
        }
    };

    public View.OnClickListener mServiceListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mMainActivity.mMainPresenter.selectService();
        }
    };

    public View.OnClickListener mAboutListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mMainActivity.mMainPresenter.showAbout();

        }
    };

    public View.OnClickListener mLogoutListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mMainActivity.mMainPresenter.doLogout();
        }
    };
}
