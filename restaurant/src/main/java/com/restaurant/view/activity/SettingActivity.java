package com.restaurant.view.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.restaurant.presenter.SettingPresenter;
import com.restaurant.view.QueryView;
import com.restaurant.view.RestaurantApplication;

import intersky.appbase.PadBaseActivity;
import intersky.mywidget.XpxSpinnerView;

public class SettingActivity extends PadBaseActivity {

    public SettingPresenter mSettingPresenter = new SettingPresenter(this);
    public RelativeLayout root;
    public RelativeLayout input;
    public TextView nowVersion;
    public TextView updataVersion;
    public TextView btnUpdata;
    public TextView rebootvolue;
    public TextView servicePort;
    public TextView serviceIp;
    public TextView serviceAppPort;
    public TextView serviceAppIp;
    public TextView wifiSet;
    public TextView equipName;
    public TextView setAudio;
    public TextView equipNumber;
    public TextView basePasswordValue;
    public TextView netSet;
    public TextView widgetVolue;
    public TextView uploadvalue;
    public TextView workVolue;

    public QueryView queryView;
    public TextView membervolue;

    public TextView netip;
    public TextView netmac;

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
        RestaurantApplication.mApp.resetFirst();
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
