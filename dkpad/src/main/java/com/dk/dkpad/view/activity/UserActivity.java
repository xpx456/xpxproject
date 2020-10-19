package com.dk.dkpad.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.dkpad.entity.Optation;
import com.dk.dkpad.entity.User;
import com.dk.dkpad.presenter.UserPresenter;
import com.dk.dkpad.view.DkPadApplication;

import java.io.File;

import intersky.appbase.PadBaseActivity;
import intersky.echartoption.ArrayData;
import intersky.echartoption.ObjectData;
import intersky.mywidget.VerticalSeekBar;

public class UserActivity extends PadBaseActivity {

    public UserPresenter mUserPresenter = new UserPresenter(this);
    public RelativeLayout root;
    public RelativeLayout all;
    public User user;
    public TextView title;
    public ImageView close;
    public ImageView headicon;
    public EditText namevalue;
    public EditText agevalue;
    public EditText weightvalue;
    public EditText tollvalue;
    public TextView btnsave;
    public TextView btndelete;
    public RelativeLayout btnmale;
    public ImageView male;
    public RelativeLayout btnfemale;
    public ImageView female;
    public WebView chartShow;
    public ObjectData option;
    public ArrayData x;
    public ArrayData y;
    public File head;
    public UserActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mUserPresenter.Destroy();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        mUserPresenter.takePhotoResult(requestCode,resultCode,data);

    }
}
