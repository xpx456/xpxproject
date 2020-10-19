package com.accesscontrol.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.accesscontrol.handler.AppHandler;
import com.accesscontrol.presenter.MainPresenter;
import com.accesscontrol.view.AccessControlApplication;
import com.accesscontrol.view.SuccessView;
import com.iccard.ICCardReaderObserver;

public class MainActivity extends PadBaseActivity {

    public static final String ACTION_UPDTATA_BTN = "ACTION_UPDTATA_BTN";
    public MainPresenter mMainPresenter = new MainPresenter(this);
    public ImageView contact;
    public ImageView register;
    public ImageView setting;
    public SuccessView successView;

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
    protected void onStart() {
        mMainPresenter.Start();
        super.onStart();
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent("android.intent.always.hideNaviBar");
        intent.putExtra("always",true);//true为一直隐藏，false为取消一直隐藏
        Message msg = new Message();
        msg.obj = false;
        msg.what = AppHandler.SET_CHAT_SHOW;
        if(AccessControlApplication.mApp.appHandler != null)
        AccessControlApplication.mApp.appHandler.sendMessage(msg);
        this.sendBroadcast(intent);
        super.onResume();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        AccessControlApplication.mApp.resetFirst();
        return super.dispatchTouchEvent(ev);
    }


}
