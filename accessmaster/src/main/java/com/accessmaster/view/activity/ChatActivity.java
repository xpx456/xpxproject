package com.accessmaster.view.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.accessmaster.presenter.ChatPresenter;

import org.json.JSONObject;

import intersky.appbase.PadBaseActivity;
import xpx.video.XpxSignalingClient;

public class ChatActivity extends PadBaseActivity{

    public ChatPresenter chatPresenter = new ChatPresenter(this);
    public ImageView btnConnect;
    public ImageView btnOpen;
    public ImageView btnOut;
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

}
