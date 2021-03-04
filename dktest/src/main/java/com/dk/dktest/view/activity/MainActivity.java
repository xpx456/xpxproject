package com.dk.dktest.view.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.dk.dktest.presenter.MainPresenter;
import com.dk.dktest.view.fragment.EquipFragment;
import com.dk.dktest.view.fragment.HistoryFragment;
import com.dk.dktest.view.fragment.HomeFragment;
import com.dk.dktest.view.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import intersky.appbase.BaseActivity;

public class MainActivity extends BaseActivity {
    public static final int HOME_PAGE = 0;
    public static final int EQUIP_PAGE = 1;
    public static final int SETTING_PAGE = 2;
    public static final int HISTORY_PAGE = 3;
    public MainPresenter mMainPresenter = new MainPresenter(this);
    public RelativeLayout shade;
    public RelativeLayout mHome;
    public TextView mHomeTxt;
    public TextView mHomeHit;
    public ImageView mHomeImg;
    public RelativeLayout mEquip;
    public TextView mEquipTxt;
    public TextView mEquipHit;
    public ImageView mEquipImg;
    public RelativeLayout mSetting;
    public TextView mSettingTxt;
    public TextView mSettingHit;
    public ImageView mSettingImg;
    public RelativeLayout mHistory;
    public TextView mHistoryTxt;
    public ImageView mHistoryImg;
    public int lastpage = 0;
    public HistoryFragment historyFragment;
    public EquipFragment equipFragment;
    public SettingFragment settingFragment;
    public HomeFragment homeFragment;
    public List<Fragment> mFragments = new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainPresenter.Create();
    }
}
