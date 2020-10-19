package com.accessmaster.view.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.accessmaster.presenter.SettingPresenter;
import com.accessmaster.view.AccessMasterApplication;

import intersky.appbase.PadBaseActivity;

public class SettingActivity extends PadBaseActivity {

    public SettingPresenter mSettingPresenter = new SettingPresenter(this);
    public RelativeLayout root;
    public RelativeLayout input;
    public TextView nowVersion;
    public TextView updataVersion;
    public TextView btnUpdata;
    public TextView workVolue;
    public TextView servicePort;
    public TextView serviceIp;
    public TextView netip;
    public TextView netmac;
    public TextView serviceAppPort;
    public TextView serviceAppIp;


    public TextView wifiSet;

    public TextView equipNumber;
    public TextView equipName;
    public TextView basePasswordValue;

    public TextView widgetVolue;
    public TextView netSet;
    public TextView setAudio;

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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        AccessMasterApplication.mApp.resetFirst();
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
