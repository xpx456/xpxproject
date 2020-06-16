package com.bigwiner.android.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.presenter.SourceCreatPresenter;
import com.bigwiner.android.view.BigwinerApplication;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.select.entity.Select;

/**
 * Created by xpx on 2017/8/18.
 */

public class SourceCreatActivity extends BaseActivity {


    public static final String ACTION_PORT_SELECT = "ACTION_PORT_SELECT";
    public static final String ACTION_AREA_SELECT = "ACTION_AREA_SELECT";
    public static final String ACTION_TYPE_SELECT = "ACTION_TYPE_SELECT";

    public SourceCreatPresenter mSourceCreatPresenter = new SourceCreatPresenter(this);
    public ImageView back;
    public String headid;
    public String cid = "";
    public RelativeLayout portbtn;
    public RelativeLayout typebtn;
    public RelativeLayout areabtn;
    public EditText name;
    public TextView port;
    public TextView type;
    public TextView area;
    public EditText memo;
    public TextView start;
    public TextView end;
    public TextView btnSubmit;
    public TextView day;
    public RelativeLayout shade;
    public PopupWindow popupWindow;
    public SourceData sourceData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSourceCreatPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSourceCreatPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


    public View.OnClickListener portSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSourceCreatPresenter.startSelectPort();
        }
    };


    public View.OnClickListener areaSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSourceCreatPresenter.startSelectArea();
        }
    };

    public View.OnClickListener typeSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSourceCreatPresenter.startSelectType();
        }
    };

    public View.OnClickListener startListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSourceCreatPresenter.setStart();
        }
    };

    public View.OnClickListener endListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSourceCreatPresenter.setEnd();
        }
    };

    public View.OnClickListener submintListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSourceCreatPresenter.doSubmit();
        }
    };
}
