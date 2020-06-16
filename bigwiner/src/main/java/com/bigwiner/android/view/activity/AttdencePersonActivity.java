package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.presenter.AttdencePersonPresenter;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Contacts;

/**
 * Created by xpx on 2017/8/18.
 */

public class AttdencePersonActivity extends BaseActivity {

    public AttdencePersonPresenter mAttdencePersonPresenter = new AttdencePersonPresenter(this);
    public ListView listView;
    public ImageView back;
    public RelativeLayout wantBtn;
    public RelativeLayout myBtn;
    public RelativeLayout personBtn;
    public RelativeLayout chatBtn;
    public Meeting meeting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAttdencePersonPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mAttdencePersonPresenter.Destroy();
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
            mAttdencePersonPresenter.startSelect(ContactsListActivity.TYPE_MY);
        }
    };

    public View.OnClickListener wantListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAttdencePersonPresenter.startSelect(ContactsListActivity.TYPE_WANT);
        }
    };

    public View.OnClickListener personListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAttdencePersonPresenter.startSelect(ContactsListActivity.TYPE_ALL);
        }
    };

    public View.OnClickListener chatListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAttdencePersonPresenter.showContactList();
        }
    };
}
