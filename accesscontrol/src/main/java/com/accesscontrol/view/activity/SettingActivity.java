package com.accesscontrol.view.activity;

import android.media.AudioManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.accesscontrol.presenter.SettingPresenter;
import com.accesscontrol.view.AccessControlApplication;
import com.accesscontrol.view.QueryView;

import intersky.mywidget.XpxSpinnerView;


/**
 * Created by xpx on 2017/8/18.
 */

public class SettingActivity extends PadBaseActivity {

    public SettingPresenter mSettingPresenter = new SettingPresenter(this);
    public RelativeLayout root;
    public RelativeLayout input;

    public TextView basePasswordValue;

    public TextView workVolue;
    public TextView rebootvolue;
    public TextView membervolue;

    public TextView netip;
    public TextView netmac;

    public TextView widgetVolue;

    public TextView equipName;
    public TextView equipNumber;

    public TextView servicePort;
    public TextView serviceIp;
    public TextView serviceAppPort;
    public TextView serviceAppIp;

    public TextView wifiSet;
    public TextView netSet;
    public TextView setAudio;
    public TextView nowVersion;
    public TextView updataVersion;
    public TextView btnUpdata;

    public QueryView queryView;

    public SettingActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSettingPresenter.Destroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        AccessControlApplication.mApp.resetFirst();
        return super.dispatchTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent event) {

        //获得触摸的坐标
        float x = event.getX();
        float y = event.getY(); switch (event.getAction())
        {
            //触摸屏幕时刻
            case MotionEvent.ACTION_DOWN:

                break;
            //触摸并移动时刻
            case MotionEvent.ACTION_MOVE:

                break;
            //终止触摸时刻
            case MotionEvent.ACTION_UP:
                if(!(root.getX() < x && root.getX()+root.getWidth() > x
                        && root.getY() < y && root.getY()+root.getHeight() > y))
                {
                    finish();
                }
                break;
        }
        return true;
    }
}
