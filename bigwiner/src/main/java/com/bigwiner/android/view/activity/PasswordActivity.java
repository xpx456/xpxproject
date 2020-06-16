package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.android.presenter.PasswordPresenter;

import intersky.appbase.BaseActivity;


public class PasswordActivity extends BaseActivity {

    public static final int EVENT_Password_SUCCESS = 1000;
    public static final int EVENT_Password_FAIL = 1001;
    public PasswordPresenter mPasswordPresenter = new PasswordPresenter(this);
    public EditText oldNumber;
    public EditText phoneNumber;
    public EditText passWord;
    public ImageView back;
    public RelativeLayout phoneLayer;
    public RelativeLayout passwordLayer;
    public TextView btnPassword;
    public TextView mRegiester;
    public TextView error;
    public TextView error2;
    public TextView btnSubmit;
    public String phone = "";
    public String code = "";

    public static final String PW_PATTERN = "^(?![0-9]+$)(?![a-zA-Z]+$)(?!([^(0-9a-zA-Z)])+$)^.{8,32}$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPasswordPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mPasswordPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener doPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPasswordPresenter.doPassword();
        }
    };

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public TextWatcher onTxtChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(mPasswordPresenter.checkPassword())
            {
                error.setVisibility(View.INVISIBLE);
                mPasswordPresenter.checkConfirm();
            }
            else
            {
                error.setVisibility(View.VISIBLE);
            }

        }
    };

    public TextWatcher onTxtChangeListener2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mPasswordPresenter.checkConfirm();
        }
    };

}
