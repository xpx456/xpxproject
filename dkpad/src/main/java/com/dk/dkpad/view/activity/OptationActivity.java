package com.dk.dkpad.view.activity;

import android.media.VolumeShaper;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.dk.dkpad.entity.Optation;
import com.dk.dkpad.presenter.OptationPresenter;
import com.dk.dkpad.view.DkPadApplication;

import intersky.appbase.PadBaseActivity;
import intersky.mywidget.VerticalSeekBar;

public class OptationActivity extends PadBaseActivity {

    public OptationPresenter mOptationPresenter = new OptationPresenter(this);
    public RelativeLayout root;
    public RelativeLayout all;
    public TextView title;
    public TextView time;
    public LinearLayout operlayer;
    public LinearLayout lineLayer;
    public ImageView close;
    public ImageView delete;
    public ImageView btnAdd;
    public ImageView btnDes;
    public ImageView btnSave;
    public Optation optation;

    public OptationActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOptationPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mOptationPresenter.Destroy();
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        DkPadApplication.mApp.appSetProtectTime(DkPadApplication.mApp.maxProtectSecond);
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
