package com.interskypad.view.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.interskypad.presenter.BigImagePresenter;

import intersky.appbase.PadBaseActivity;

public class BigImageActivity extends PadBaseActivity {

    public BigImagePresenter mBigImagePresenter = new BigImagePresenter(this);
    public RelativeLayout mRelativeLayout;

    public BigImageActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBigImagePresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mBigImagePresenter.Destroy();
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
                if(!(mRelativeLayout.getX() < x && mRelativeLayout.getX()+mRelativeLayout.getWidth() > x
                        && mRelativeLayout.getY() < y && mRelativeLayout.getY()+mRelativeLayout.getHeight() > y))
                {
                    finish();
                }
                break;
        }
        return true;
    }
}
