package com.dk.dktest.presenter;


import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

import com.dk.dktest.R;
import com.dk.dktest.handler.MainHandler;
import com.dk.dktest.receive.MainReceiver;
import com.dk.dktest.view.DkTestApplication;
import com.dk.dktest.view.activity.MainActivity;
import com.dk.dktest.view.fragment.EquipFragment;
import com.dk.dktest.view.fragment.HistoryFragment;
import com.dk.dktest.view.fragment.HomeFragment;
import com.dk.dktest.view.fragment.SettingFragment;

import intersky.appbase.FragmentTabAdapter;
import intersky.appbase.Presenter;
import xpx.com.toolbar.utils.ToolBarHelper;

public class MainPresenter implements Presenter {

    public MainActivity mMainActivity;
    public MainHandler mainHandler;

    public MainPresenter(MainActivity MainActivity) {
        mMainActivity = MainActivity;
        mainHandler = new MainHandler(MainActivity);
        mMainActivity.setBaseReceiver(new MainReceiver(mainHandler));
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mMainActivity, Color.argb(0, 255, 255, 255));
        mMainActivity.setContentView(R.layout.activity_main);
        mMainActivity.mToolBarHelper.hidToolbar(mMainActivity, (RelativeLayout) mMainActivity.findViewById(R.id.buttomaciton));
        mMainActivity.shade = (RelativeLayout) mMainActivity.findViewById(R.id.shade);
        mMainActivity.mHome = mMainActivity.findViewById(R.id.home);
        mMainActivity.mHomeImg = mMainActivity.findViewById(R.id.home_image);
        mMainActivity.mHomeTxt = mMainActivity.findViewById(R.id.home_text);
        mMainActivity.mHomeHit = mMainActivity.findViewById(R.id.home_hit);
        mMainActivity.mEquip = mMainActivity.findViewById(R.id.equip);
        mMainActivity.mEquipImg = mMainActivity.findViewById(R.id.equip_image);
        mMainActivity.mEquipTxt = mMainActivity.findViewById(R.id.equip_text);
        mMainActivity.mSetting = mMainActivity.findViewById(R.id.setting);
        mMainActivity.mSettingImg = mMainActivity.findViewById(R.id.setting_image);
        mMainActivity.mSettingTxt = mMainActivity.findViewById(R.id.setting_text);
        mMainActivity.mHistory = mMainActivity.findViewById(R.id.history);
        mMainActivity.mHistoryImg = mMainActivity.findViewById(R.id.history_image);
        mMainActivity.mHistoryTxt = mMainActivity.findViewById(R.id.history_text);
        mMainActivity.homeFragment = new HomeFragment();
        mMainActivity.equipFragment = new EquipFragment();
        mMainActivity.settingFragment = new SettingFragment();
        mMainActivity.historyFragment = new HistoryFragment();
        mMainActivity.mFragments.add(mMainActivity.homeFragment);
        mMainActivity.mFragments.add(mMainActivity.equipFragment);
        mMainActivity.mFragments.add(mMainActivity.settingFragment);
        mMainActivity.mFragments.add(mMainActivity.historyFragment);
        mMainActivity.tabAdapter = new FragmentTabAdapter(mMainActivity, mMainActivity.mFragments, R.id.tab_content);
        mMainActivity.mHistory.setOnClickListener(historyListener);
        mMainActivity.mHome.setOnClickListener(homeListener);
        mMainActivity.mEquip.setOnClickListener(equipListener);
        mMainActivity.mSetting.setOnClickListener(settingListener);
        DkTestApplication.mApp.bluetoothSetManager.scanLeDevice();
        setContent(MainActivity.HOME_PAGE);
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


    public void setContent(int page) {
        mMainActivity.lastpage = mMainActivity.tabAdapter.getCurrentTab();
        mMainActivity.tabAdapter.onCheckedChanged(page);
        switch (page) {
            case MainActivity.HOME_PAGE:
                mMainActivity.mHomeTxt.setTextColor(Color.rgb(216, 30, 6));
                mMainActivity.mEquipTxt.setTextColor(Color.rgb(51, 51, 15));
                mMainActivity.mSettingTxt.setTextColor(Color.rgb(51, 51, 15));
                mMainActivity.mHistoryTxt.setTextColor(Color.rgb(51, 51, 51));
                mMainActivity.mHomeImg.setImageResource(R.drawable.home2);
                mMainActivity.mEquipImg.setImageResource(R.drawable.equip);
                mMainActivity.mSettingImg.setImageResource(R.drawable.setting);
                mMainActivity.mHistoryImg.setImageResource(R.drawable.history);
                break;
            case MainActivity.EQUIP_PAGE:
                mMainActivity.mHomeTxt.setTextColor(Color.rgb(51, 51, 51));
                mMainActivity.mEquipTxt.setTextColor(Color.rgb(216, 30, 6));
                mMainActivity.mSettingTxt.setTextColor(Color.rgb(51, 51, 15));
                mMainActivity.mHistoryTxt.setTextColor(Color.rgb(51, 51, 51));
                mMainActivity.mHomeImg.setImageResource(R.drawable.home);
                mMainActivity.mEquipImg.setImageResource(R.drawable.equip2);
                mMainActivity.mSettingImg.setImageResource(R.drawable.setting);
                mMainActivity.mHistoryImg.setImageResource(R.drawable.history);
                break;
            case MainActivity.SETTING_PAGE:
                mMainActivity.mHomeTxt.setTextColor(Color.rgb(51, 51, 51));
                mMainActivity.mEquipTxt.setTextColor(Color.rgb(51, 51, 15));
                mMainActivity.mSettingTxt.setTextColor(Color.rgb(216, 30, 6));
                mMainActivity.mHistoryTxt.setTextColor(Color.rgb(51, 51, 51));
                mMainActivity.mHomeImg.setImageResource(R.drawable.home);
                mMainActivity.mEquipImg.setImageResource(R.drawable.equip);
                mMainActivity.mSettingImg.setImageResource(R.drawable.setting2);
                mMainActivity.mHistoryImg.setImageResource(R.drawable.history);
                break;
            case MainActivity.HISTORY_PAGE:
                mMainActivity.mHomeTxt.setTextColor(Color.rgb(51, 51, 15));
                mMainActivity.mEquipTxt.setTextColor(Color.rgb(51, 51, 15));
                mMainActivity.mSettingTxt.setTextColor(Color.rgb(51, 51, 15));
                mMainActivity.mHistoryTxt.setTextColor(Color.rgb(216, 30, 6));
                mMainActivity.mHomeImg.setImageResource(R.drawable.home);
                mMainActivity.mEquipImg.setImageResource(R.drawable.equip);
                mMainActivity.mSettingImg.setImageResource(R.drawable.setting);
                mMainActivity.mHistoryImg.setImageResource(R.drawable.history2);
                break;

        }
    }

    public View.OnClickListener homeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContent(MainActivity.HOME_PAGE);
        }
    };

    public View.OnClickListener historyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContent(MainActivity.HISTORY_PAGE);
        }
    };

    public View.OnClickListener settingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContent(MainActivity.SETTING_PAGE);
        }
    };

    public View.OnClickListener equipListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContent(MainActivity.EQUIP_PAGE);
        }
    };
}
