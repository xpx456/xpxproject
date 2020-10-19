package com.dk.dkhome.presenter;


import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

import com.dk.dkhome.R;
import com.dk.dkhome.handler.MainHandler;
import com.dk.dkhome.view.activity.MainActivity;
import com.dk.dkhome.view.fragment.MyFragment;
import com.dk.dkhome.view.fragment.SportFragment;

import intersky.appbase.FragmentTabAdapter;
import intersky.appbase.Presenter;
import xpx.bluetooth.BluetoothSetManager;
import xpx.com.toolbar.utils.ToolBarHelper;

public class MainPresenter implements Presenter {

    public MainActivity mMainActivity;
    public MainHandler mainHandler;

    public MainPresenter(MainActivity MainActivity) {
        mMainActivity = MainActivity;
        mainHandler = new MainHandler(MainActivity);
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mMainActivity, Color.argb(0, 255, 255, 255));
        mMainActivity.setContentView(R.layout.activity_main);
        mMainActivity.mToolBarHelper.hidToolbar(mMainActivity, (RelativeLayout) mMainActivity.findViewById(R.id.buttomaciton));
        mMainActivity.shade = (RelativeLayout) mMainActivity.findViewById(R.id.shade);
        mMainActivity.mSport = mMainActivity.findViewById(R.id.sport);
        mMainActivity.mSportImg = mMainActivity.findViewById(R.id.sport_image);
        mMainActivity.mSportTxt = mMainActivity.findViewById(R.id.sport_text);
        mMainActivity.mSportHit = mMainActivity.findViewById(R.id.sport_hit);
        mMainActivity.mMy = mMainActivity.findViewById(R.id.my);
        mMainActivity.mMyImg = mMainActivity.findViewById(R.id.my_image);
        mMainActivity.mMyTxt = mMainActivity.findViewById(R.id.my_text);
        mMainActivity.myFragment = new MyFragment();
        mMainActivity.sportFragment = new SportFragment();
        mMainActivity.mFragments.add(mMainActivity.sportFragment);
        mMainActivity.mFragments.add(mMainActivity.myFragment);
        mMainActivity.tabAdapter = new FragmentTabAdapter(mMainActivity, mMainActivity.mFragments, R.id.tab_content);
        mMainActivity.mMy.setOnClickListener(myListener);
        mMainActivity.mSport.setOnClickListener(sportListener);
        setContent(MainActivity.SPORT_PAGE);
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
            case MainActivity.SPORT_PAGE:
                mMainActivity.mSportTxt.setTextColor(Color.rgb(0, 0, 0));
                mMainActivity.mMyTxt.setTextColor(Color.rgb(221, 221, 221));
                break;
            case MainActivity.MY_PAGE:
                mMainActivity.mSportTxt.setTextColor(Color.rgb(221, 221, 221));
                mMainActivity.mMyTxt.setTextColor(Color.rgb(0, 0, 0));
                break;

        }
    }

    public View.OnClickListener sportListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContent(MainActivity.SPORT_PAGE);
        }
    };

    public View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContent(MainActivity.MY_PAGE);
        }
    };
}
