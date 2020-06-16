package com.bigwiner.android.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.bigwiner.R;
import com.bigwiner.android.presenter.RegisterPresenter;

import intersky.appbase.BaseActivity;
import intersky.mywidget.conturypick.Country;

public class RegisterActivity extends BaseActivity {
    public static final String ACTION_L_CITY_SELECT = "ACTION_L_CITY_SELECT";
    public static final String ACTION_L_AREA_SELECT = "ACTION_L_AREA_SELECT";
    public static final String ACTION_L_TYPE_SELECT = "ACTION_L_TYPE_SELECT";
    public static final String ACTION_AREA = "ACTION_AREA";
    public static final int ACTICITY_RESULT_GET_AREA_CODE = 111;
    public static final int EVENT_REGISTER_SUCCESS = 1000;
    public static final int EVENT_REGISTER_FAIL = 1001;
    public static final int EVENT_GET_CODE_SUCCESS = 1002;
    public static final int EVENT_GET_CODE_FAIL = 1003;
    public static final int EVENT_UPDATA_CODE_SECOND = 301004;
    public static final int EVENT_CHECK_CODE_SUCCESS = 1005;
    public static final int EVENT_CHECK_CODE_FAIL = 1006;
    public static final int EVENT_CHECK_USER_SUCCESS = 1007;
    public static final int EVENT_CHECK_USER_FAIL = 1008;
    public static final int CODE_SECOND = 60;
    public RegisterPresenter mRegisterPresenter = new RegisterPresenter(this);
    public EditText phoneNumber;
    public TextView btnRegister;
    public TextView btnGetCode;
    public TextView title;
    public EditText code;
    public TextView btnsendcode;
    public ImageView back;
    public TextView areaTxt;
    public RelativeLayout btnArea;
    public TextView arename;
    public EditText passWord;
    public TextView error;
    public TextView error2;
    public EditText  passNumber;
    public AppCompatCheckBox compatCheckBox;
    public TextView safe;

    public RelativeLayout citybtn;
    public RelativeLayout typebtn;
    public RelativeLayout areabtn;
    public RelativeLayout mailbtn;
    public EditText mail;
    public TextView error3;
    public TextView city;
    public TextView type;
    public TextView area;

    public String lastPhone = "";
    public static final String PW_PATTERN = "^(?![0-9]+$)(?![a-zA-Z]+$)(?!([^(0-9a-zA-Z)])+$)^.{8,32}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterPresenter.Create();
    }

    @Override
    protected void onDestroy()
    {
        mRegisterPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public View.OnClickListener sendCodeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mRegisterPresenter.getCode();
        }
    };

    public View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mRegisterPresenter.startRegister();
        }
    };

    public View.OnClickListener areaListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mRegisterPresenter.startArea();
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
            if(mRegisterPresenter.checkPassword())
            {
                error.setVisibility(View.INVISIBLE);
                mRegisterPresenter.checkConfirm();
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
            mRegisterPresenter.checkConfirm();
        }
    };

    public View.OnClickListener safeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mRegisterPresenter.showSafe();
        }
    };

    public CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener()
    {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked == false)
            {
                btnRegister.setEnabled(false);
                btnRegister.setBackgroundResource(R.drawable.shape_login_bg_btn_gray);
            }
            else
            {
                btnRegister.setEnabled(true);
                btnRegister.setBackgroundResource(R.drawable.shape_login_bg_btn);
            }
        }
    };

    public View.OnClickListener citySelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mRegisterPresenter.startSelectCity();
        }
    };

    public View.OnClickListener areaSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mRegisterPresenter.startSelectArea();
        }
    };

    public View.OnClickListener typeSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mRegisterPresenter.startSelectType();
        }
    };

}
