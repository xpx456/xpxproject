package com.accessmaster.view.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.accessmaster.presenter.RegisterPresenter;
import com.accessmaster.view.AccessMasterApplication;

import intersky.appbase.PadBaseActivity;

public class RegisterActivity extends PadBaseActivity {

    public RegisterPresenter mRegisterPresenter = new RegisterPresenter(this);
    public RelativeLayout root;
    public RelativeLayout input;

    public TextView name;
    public TextView address;
//    public ImageView access;
//    public RelativeLayout btnaccess;
//    public ImageView attdence;
//    public RelativeLayout btnattdence;
    public TextView btnRegister;


    public RegisterActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mRegisterPresenter.Destroy();
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
