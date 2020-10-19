package com.intersky.android.view.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

import com.intersky.android.presenter.MainPresenter;
import com.intersky.android.tools.IatHelper;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.PermissionCode;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;

/**
 * Created by xpx on 2017/8/18.
 */

public class MainActivity extends BaseActivity {

    public static final String ACTION_UPDATA_CONVERSATION = "ACTION_UPDATA_CONVERSATION";
    public static final String ACTION_UPDATA_IWEB_MESSAGE = "ACTION_UPDATA_IWEB_MESSAGE";

    public static final String ACTION_KIKOUT = "ACTION_KIKOUT";
    public static final int PAGE_CONTACTS = 2;
    public static final int PAGE_CONVERSATION = 0;
    public static final int PAGE_MY = 3;
    public static final int PAGE_WORK = 1;
    public boolean isResum = true;
    public boolean dismissflag= true;
    public PopupWindow popupWindow;
    public ArrayList<Conversation> temp = new ArrayList<Conversation>();
    public String readid  = "";
    public int unreadcount  = 0;
    public int messagePage = 1;
    public MainPresenter mMainPresenter = new MainPresenter(this);
//    public IatHelper iatHelper;
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
        mMainPresenter.Resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMainPresenter.Pause();
        super.onPause();
    }

    public View.OnClickListener showWorkListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMainPresenter.setContent(PAGE_WORK);
        }
    };

    public View.OnClickListener showContactsListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMainPresenter.setContent(PAGE_CONTACTS);
        }
    };

    public View.OnClickListener showCoversationListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMainPresenter.setContent(PAGE_CONVERSATION);
        }
    };

    public View.OnClickListener showMyListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMainPresenter.setContent(PAGE_MY);
        }
    };

    public View.OnClickListener mMoreListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View view) {
            mMainPresenter.showMore();
        }
    };

    public View.OnClickListener mScanListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View view) {
            mMainPresenter.doSafeLogin();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mMainPresenter.onKeyDown(keyCode, event)) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

//    public View.OnClickListener speachListener = new View.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            permissionRepuest = iatHelper.doStart();
//        }
//    };


    public View.OnClickListener mtestAdd = new View.OnClickListener()
    {

        @Override
        public void onClick(View view) {
            mMainPresenter.getReceive();
        }
    };
}
