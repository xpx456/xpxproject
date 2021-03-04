package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.presenter.SailApplyPresenter;

import intersky.appbase.BaseActivity;

/**
 * Created by xpx on 2017/8/18.
 */

public class SailApplyActivity extends BaseActivity {

    public SailApplyPresenter mSailApplyPresenter = new SailApplyPresenter(this);
    public ListView listView;
    public ImageView back;
    public TextView cname;
    public TextView caddress;
    public EditText contact;
    public EditText phone;
    public EditText business;
    public TextView commit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSailApplyPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSailApplyPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


    public View.OnClickListener applyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSailApplyPresenter.doapply() ;
        }
    };


}
