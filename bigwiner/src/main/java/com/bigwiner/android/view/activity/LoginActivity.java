package com.bigwiner.android.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.android.presenter.LoginPresenter;

import intersky.appbase.BaseActivity;


public class LoginActivity extends BaseActivity {

    public static final String ACTION_REGIST_SUCCESS = "ACTION_REGIST_SUCCESS";
    public static final String ACTION_CHANGE_SUCCESS = "ACTION_CHANGE_SUCCESS";
    public LoginPresenter mLoginPresenter = new LoginPresenter(this);
    public EditText phoneNumber;
    public EditText passWord;
    public ImageView showPassword;
    public RelativeLayout phoneLayer;
    public RelativeLayout passwordLayer;
    public TextView btnLogin;
    public TextView mForget;
    public boolean backflag = false;
    public String type = "";
    public TextView mRegiester;
    public boolean showPassowrd = false;
    public TextView safe;
    public TextView areaTxt;
    public RelativeLayout btnArea;
    public TextView arename;
    public int pagecount = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mLoginPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener showPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mLoginPresenter.showPassword();
        }
    };
    public View.OnClickListener doLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mLoginPresenter.doLogin();
        }
    };
    public View.OnClickListener startRegisterListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mLoginPresenter.startRegister();
        }
    };

    public View.OnClickListener mForgetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLoginPresenter.startForget();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mLoginPresenter.onKeyDown(keyCode, event)) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    public View.OnClickListener safeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLoginPresenter.showSafe();
        }
    };


    @Override
    protected void onNewIntent(Intent intent) {
        super.onResume();
        setIntent(intent);
    }

    public View.OnClickListener areaListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mLoginPresenter.startArea();
        }
    };
}
