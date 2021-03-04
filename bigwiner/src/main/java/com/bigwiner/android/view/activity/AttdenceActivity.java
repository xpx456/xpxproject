package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.entity.JoinData;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.presenter.AttdencePresenter;
import com.bigwiner.android.view.adapter.ConversationAdapter;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Conversation;

/**
 * Created by xpx on 2017/8/18.
 */

public class AttdenceActivity extends BaseActivity {

    public static final String ACTION_UPDATA_SOURCE_SELCET = "ACTION_UPDATA_SOURCE_SELCET";
    public AttdencePresenter mAttdencePresenter = new AttdencePresenter(this);
    public ListView listView;
    public ImageView back;
    public RelativeLayout wantBtn;
    public RelativeLayout myBtn;
    public TextView btnSubmit;
    public Meeting meeting;
    public TextView wantvalue;
    public TextView myvalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAttdencePresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mAttdencePresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAttdencePresenter.startSelect(0);
        }
    };

    public View.OnClickListener wantListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAttdencePresenter.startSelect(1);
        }
    };

    public View.OnClickListener submintListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAttdencePresenter.doJoinin();
        }
    };
}
