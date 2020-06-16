package com.intersky.strang.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intersky.strang.android.asks.LoginAsks;
import com.intersky.strang.android.presenter.ScanLoginPresenter;

import intersky.appbase.BaseActivity;

/**
 * Created by xpx on 2017/8/18.
 */

public class ScanLoginActivity extends BaseActivity {

    public static final int SAFE_LOGIN_SUCCESS = 10000;
    public static final int SAFE_LOGIN_FAIL = 20000;
    public RelativeLayout nomalLayer;
    public RelativeLayout errorLayer;
    public TextView btnOkLogin;
    public TextView txtError;
    public String result = "";
    public String serial = "";
    public String type = "";
    public ScanLoginPresenter mScanLoginPresenter = new ScanLoginPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScanLoginPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mScanLoginPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mScanLoginPresenter.mScanLoginActivity.waitDialog.show();
            LoginAsks.safelogin(mScanLoginPresenter.mScanLoginHandler,mScanLoginPresenter.mScanLoginActivity,serial,type);
        }
    };
}
