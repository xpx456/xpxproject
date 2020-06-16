package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.android.entity.Source;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.presenter.SourceDetialPresenter;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.mywidget.CircleImageView;
import intersky.select.entity.Select;

/**
 * Created by xpx on 2017/8/18.
 */

public class SourceDetialActivity extends BaseActivity {

    public SourceDetialPresenter mSourceDetialPresenter = new SourceDetialPresenter(this);
    public SourceData sourceData;
    public ImageView back;
    public String cid = "";
    public TextView name;
    public TextView collecttxt;
    public TextView publictime;
    public TextView collect;
    public TextView view;
    public TextView port;
    public ImageView mAddimg;
    public TextView type;
    public TextView area;
    public TextView memo;
    public TextView start;
    public TextView end;
    public TextView username;
    public TextView position;
    public TextView comapny;
    public CircleImageView head;
    public RelativeLayout btnAdd;
    public RelativeLayout shade;
    public RelativeLayout contact;
    public RelativeLayout collectbtn;
    public PopupWindow popupWindow;
    public ImageView shareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSourceDetialPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSourceDetialPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public View.OnClickListener collectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSourceDetialPresenter.doCollect();
        }
    };

    public View.OnClickListener addFriendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSourceDetialPresenter.doAddfriend();
        }
    };

    public View.OnClickListener delFriendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSourceDetialPresenter.doDelfriend();
        }
    };

    public View.OnClickListener showContactListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSourceDetialPresenter.showContact();
        }
    };
}
