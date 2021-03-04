package com.accesscontrol.view.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.accesscontrol.entity.Device;
import com.accesscontrol.presenter.ChatPresenter;
import com.accesscontrol.view.AccessControlApplication;

import org.json.JSONObject;

import java.util.HashMap;


import xpx.video.XpxSignalingClient;

public class ChatActivity extends PadBaseActivity{

    public ChatPresenter chatPresenter = new ChatPresenter(this);
    public ImageView btnDisConntact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        chatPresenter.Destroy();
        super.onDestroy();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //AccessControlApplication.mApp.resetFirst();
        return super.dispatchTouchEvent(ev);
    }
}
