package com.restaurant.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iccard.ICCardReaderObserver;
import com.iccard.IcCardManager;
import com.restaurant.presenter.MainPresenter;
import com.restaurant.view.RestaurantApplication;
import com.restaurant.view.SuccessView;

import intersky.appbase.BaseActivity;
import intersky.appbase.PadBaseActivity;
import intersky.apputils.AppUtils;

public class MainActivity extends PadBaseActivity {

    public static final String ACTION_UPDTATA_BTN = "ACTION_UPDTATA_BTN";

    public MainPresenter mMainPresenter = new MainPresenter(this);
    public TextView time;
    public TextView type;
    public ImageView setting;
    public ImageView register;
    public SuccessView successView;
    public TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainPresenter.Create();
    }

    @Override
    protected void onDestroy()
    {
        mMainPresenter.Destroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Intent navIntent = new Intent("com.hyzn.sdk.switchNavBar");
        navIntent .putExtra("value", 0);
        this.sendBroadcast(navIntent);
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case AppUtils.PERMISSION_REQUEST:
                boolean hasPermissionDismiss = false;      //有权限没有通过
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == -1) {
                        hasPermissionDismiss = true;   //发现有未通过权限
                        break;
                    }
                }
                if(hasPermissionDismiss)
                {
                    finish();
                }
                else
                {

                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        RestaurantApplication.mApp.resetFirst();
        return super.dispatchTouchEvent(ev);
    }
}
