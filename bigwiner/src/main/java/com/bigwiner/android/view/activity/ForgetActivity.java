package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bigwiner.android.presenter.ForgetPresenter;

import intersky.appbase.BaseActivity;

public class ForgetActivity extends BaseActivity {

    public static final int EVENT_FORGET_SUCCESS = 1000;
    public static final int EVENT_FORGET_FAIL = 1001;
    public static final int EVENT_GET_CODE_SUCCESS = 1002;
    public static final int EVENT_GET_CODE_FAIL = 1003;
    public static final int EVENT_UPDATA_CODE_SECOND = 302004;
    public static final int EVENT_CHECK_CODE_SUCCESS = 1005;
    public static final int EVENT_CHECK_CODE_FAIL = 1006;
    public static final int EVENT_CHECK_USER_SUCCESS = 1007;
    public static final int EVENT_CHECK_USER_FAIL = 1008;
    public static final int CODE_SECOND = 60;
    public ForgetPresenter mForgetPresenter = new ForgetPresenter(this);
    public EditText phoneNumber;
    public EditText passWord;
    public EditText passWordConfirm;
    public EditText code;
    public ImageView phoneNumberIcon;
    public ImageView passWordIcon;
    public ImageView codeIcon;
    public ImageView passWordConfirmIcon;
    public ImageView showPassword;
    public ImageView showPasswordConfirm;
    public RelativeLayout phoneLayer;
    public RelativeLayout passwordLayer;
    public RelativeLayout passwordConfirmLayer;
    public RelativeLayout codeLayer;
    public TextView btnForget;
    public TextView btnGetCode;
    public boolean showPassowrd = false;
    public boolean showPassowrdConfirm = false;
    public int second;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mForgetPresenter.Create();
    }

    @Override
    protected void onDestroy()
    {
        mForgetPresenter.Destroy();
        super.onDestroy();
    }


    public View.OnFocusChangeListener phoneNumberChange = new View.OnFocusChangeListener()
    {

        @Override
        public void onFocusChange(View view, boolean b) {
            mForgetPresenter.phoneChange(b);
        }
    };

    public View.OnFocusChangeListener passwardChange = new View.OnFocusChangeListener()
    {

        @Override
        public void onFocusChange(View view, boolean b) {
            mForgetPresenter.passwordChange(b);
        }
    };

    public View.OnFocusChangeListener passwardConfirmChange = new View.OnFocusChangeListener()
    {

        @Override
        public void onFocusChange(View view, boolean b) {
            mForgetPresenter.passwordConfirmChange(b);
        }
    };

    public View.OnFocusChangeListener codeChange = new View.OnFocusChangeListener()
    {

        @Override
        public void onFocusChange(View view, boolean b) {
            mForgetPresenter.codeChange(b);
        }
    };

    public View.OnClickListener showPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mForgetPresenter.showPassword();
        }
    };

    public View.OnClickListener showPasswordConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mForgetPresenter.showPasswordConfirm();
        }
    };

    public View.OnClickListener getCodeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mForgetPresenter.getCode();
        }
    };

    public View.OnClickListener ForgetListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mForgetPresenter.startForget();
        }
    };
}
