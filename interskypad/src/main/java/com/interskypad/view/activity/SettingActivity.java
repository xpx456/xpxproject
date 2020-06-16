package com.interskypad.view.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.interskypad.presenter.SettingPresenter;

import intersky.appbase.PadBaseActivity;

public class SettingActivity extends PadBaseActivity {

    public RelativeLayout root;
    public SettingPresenter mFirstPresenter = new SettingPresenter(this);
    public SettingActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirstPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mFirstPresenter.Destroy();
        super.onDestroy();
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
