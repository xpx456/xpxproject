package com.intersky.android.view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.intersky.R;
import com.intersky.android.presenter.ServiceSettingPresenter;

import intersky.appbase.BaseActivity;
import intersky.mywidget.TextButton;

/**
 * Created by xpx on 2017/8/18.
 */

public class ServiceSettingActivity extends BaseActivity {

    public static final String ACTION_SERVICE_UPDATA = "ACTION_SERVICE_UPDATA";
    public ServiceSettingPresenter mServiceSettingPresenter = new ServiceSettingPresenter(this);
    public EditText eTxtName;
    public EditText eTxtAddress;
    public EditText eTxtPort;
    public TextView ipname;
    public CheckBox checkIp;
    public CheckBox chectAgent;
    public TextButton btnConfirm;
    public CheckBox yesCheck;
    public CheckBox noCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mServiceSettingPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mServiceSettingPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener mConfirmListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mServiceSettingPresenter.doConfirm();
        }
    };

    public TextWatcher nameAndAddressChange = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(eTxtName.getText().toString().length() > 0 && eTxtAddress.getText().toString().length() > 0)
            {
                if(btnConfirm.isEnabled() == false)
                {
                    btnConfirm.setEnabled(true);
                }

            }
            else
            {
                if(btnConfirm.isEnabled() == true)
                {
                    btnConfirm.setEnabled(false);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    public CompoundButton.OnCheckedChangeListener mIpAndAgentCheckListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(buttonView.getId() == R.id.ipcheck)
            {
                if(isChecked)
                {
                    if(chectAgent.isChecked() == true)
                    {
                        chectAgent.setChecked(false);
                        ipname.setText(mServiceSettingPresenter.mServiceSettingActivity.getString(R.string.servicesetting_ip));
                    }

                }
                else
                {
                    if(chectAgent.isChecked() == false)
                    {
                        chectAgent.setChecked(true);
                        ipname.setText(mServiceSettingPresenter.mServiceSettingActivity.getString(R.string.servicesetting_code));
                    }

                }
            }
            else
            {
                if(isChecked)
                {
                    if(checkIp.isChecked() == true)
                    {
                        checkIp.setChecked(false);
                        ipname.setText(mServiceSettingPresenter.mServiceSettingActivity.getString(R.string.servicesetting_code));
                    }

                }
                else
                {
                    if(checkIp.isChecked() == false)
                    {
                        checkIp.setChecked(true);
                        ipname.setText(mServiceSettingPresenter.mServiceSettingActivity.getString(R.string.servicesetting_ip));
                    }

                }
            }

        }
    };


    public CompoundButton.OnCheckedChangeListener mHttpsCheckListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(buttonView.getId() == R.id.yesCheck)
            {
                if(isChecked)
                {
                    if(noCheck.isChecked() == true)
                        noCheck.setChecked(false);
                }
                else
                {
                    if(noCheck.isChecked() == false)
                        noCheck.setChecked(true);
                }
            }
            else
            {
                if(isChecked)
                {
                    if(yesCheck.isChecked() == true)
                        yesCheck.setChecked(false);
                }
                else
                {
                    if(yesCheck.isChecked() == false)
                        yesCheck.setChecked(true);
                }
            }

        }
    };
}
