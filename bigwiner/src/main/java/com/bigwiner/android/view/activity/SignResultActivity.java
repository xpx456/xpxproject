package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.android.presenter.SignResultPresenter;

import intersky.appbase.BaseActivity;

/**
 * Created by xpx on 2017/8/18.
 */

public class SignResultActivity extends BaseActivity {

    public SignResultPresenter mSignResultPresenter = new SignResultPresenter(this);
    public TextView name;
    public TextView count;
    public TextView txt1;
    public TextView txt3;
    public TextView title;
    public TextView imf;
    public ImageView back;
    public ImageView icon;
    public String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignResultPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSignResultPresenter.Destroy();
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

        }
    };

    public View.OnClickListener wantListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };
}
