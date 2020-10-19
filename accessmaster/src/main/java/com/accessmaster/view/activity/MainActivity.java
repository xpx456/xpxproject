package com.accessmaster.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.accessmaster.handler.AppHandler;
import com.accessmaster.presenter.MainPresenter;
import com.accessmaster.view.AccessMasterApplication;
import com.accessmaster.view.adapter.GrigPageAdapter;

import java.util.ArrayList;

import intersky.appbase.Actions;
import intersky.appbase.PadBaseActivity;
import intersky.appbase.PermissionCode;
import intersky.apputils.AppUtils;

public class MainActivity extends PadBaseActivity {

    public static final String ACTION_UPDATA_MAIN_GRIDE = "ACTION_UPDATA_MAIN_GRIDE";
    public static final String ACTION_UPDTATA_BTN = "ACTION_UPDTATA_BTN";
    public MainPresenter mMainPresenter = new MainPresenter(this);
    public ImageView setting;
    public ImageView register;
    public GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mMainPresenter.Destroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        AccessMasterApplication.mApp.manager.ZYSystemBar(0);
        Message msg = new Message();
        msg.obj = false;
        msg.what = AppHandler.SET_CHAT_SHOW;
        if(AccessMasterApplication.mApp.appHandler != null)
            AccessMasterApplication.mApp.appHandler.sendMessage(msg);
        super.onResume();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        AccessMasterApplication.mApp.resetFirst();
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionCode.PERMISSION_REQUEST_AUDIORECORD:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        AppUtils.showMessage(mMainPresenter.mMainActivity,"请先获取麦克风权限");
                    }
                } else {
                    AppUtils.showMessage(mMainPresenter.mMainActivity,"请先获取麦克风权限");
                }
                break;
        }

    }
}
