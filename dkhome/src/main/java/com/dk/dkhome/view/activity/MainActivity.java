package com.dk.dkhome.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.dk.dkhome.handler.SplashHandler;
import com.dk.dkhome.presenter.MainPresenter;
import com.dk.dkhome.view.fragment.MyFragment;
import com.dk.dkhome.view.fragment.SportFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import intersky.appbase.BaseActivity;
import intersky.apputils.AppUtils;
import xpx.bluetooth.BluetoothSetManager;

public class MainActivity extends BaseActivity {
    public static final int SPORT_PAGE = 0;
    public static final int MY_PAGE = 1;
    public MainPresenter mMainPresenter = new MainPresenter(this);
    public RelativeLayout shade;
    public RelativeLayout mSport;
    public TextView mSportTxt;
    public TextView mSportHit;
    public ImageView mSportImg;
    public RelativeLayout mMy;
    public TextView mMyTxt;
    public ImageView mMyImg;
    public int lastpage = 0;
    public MyFragment myFragment;
    public SportFragment sportFragment;
    public List<Fragment> mFragments = new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainPresenter.Create();
    }


}
