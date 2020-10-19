package com.intersky.android.view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.intersky.android.presenter.LoginPresenter;
import com.intersky.android.view.InterskyApplication;


import intersky.mywidget.TextButton;
import intersky.xpxnet.net.Service;


/**
 * Created by xpx on 2017/8/18.
 */

public class LoginActivity extends BaseActivity {

    public static String ACTION_UPDATA_BUDGE = "ACTION_UPDATA_BUDGE";
    public LoginPresenter mLoginPresenter = new LoginPresenter(this);
    public ArrayAdapter<Service> sAdapter;
    public Spinner mSpinner;
    public EditText eTxtAccount;
    public EditText eTxtPassword;
    public TextView btnServiceList;
    public TextView btnLogin;
    public Boolean autoLogin = true;
    public RelativeLayout firstLayer;
    public LoginActivity() {
    }

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

    public TextWatcher accountChange = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(eTxtAccount.getText().toString().length() > 0 && InterskyApplication.mApp.mService != null)
            {
                if(btnLogin.isEnabled() == false)
                {
                    btnLogin.setEnabled(true);

                }

            }
            else
            {
                if(btnLogin.isEnabled() == true)
                {
                    btnLogin.setEnabled(false);
                }
            }

            if(InterskyApplication.mApp.mService == null)
            {
                if(eTxtAccount.getText().toString().equals("flametestaccount7913_4826"))
                {
                    btnLogin.setEnabled(true);
                    InterskyApplication.mApp.mService = new Service();
                    InterskyApplication.mApp.mService.https = false;
                    InterskyApplication.mApp.mService.sType = true;
                    InterskyApplication.mApp.mService.sAddress = "cloud.intersky.com.cn";
                    InterskyApplication.mApp.mService.sPort = "80";
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public View.OnClickListener loginListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mLoginPresenter.doLogin();
        }
    };

    public AdapterView.OnItemSelectedListener sniperItemClick = new AdapterView.OnItemSelectedListener()
    {


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mLoginPresenter.onItemClick(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public View.OnClickListener serviceListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mLoginPresenter.doService();
        }
    };

}
