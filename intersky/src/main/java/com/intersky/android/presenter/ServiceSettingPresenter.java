package com.intersky.android.presenter;

import android.content.Intent;
import android.widget.CheckBox;

import com.intersky.R;
import com.intersky.android.database.DBHelper;
import com.intersky.android.view.activity.ServiceSettingActivity;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.Service;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class ServiceSettingPresenter implements Presenter {

    public ServiceSettingActivity mServiceSettingActivity;
    public Service mService;
    public ServiceSettingPresenter(ServiceSettingActivity mServiceSettingActivity)
    {
        this.mServiceSettingActivity = mServiceSettingActivity;
    }

    @Override
    public void Create() {
        initView();
        if(mServiceSettingActivity.getIntent().hasExtra("service"))
        {

            initService();
        }
        else
        {
            mService = new Service();
            mService.sRecordId = AppUtils.getguid();
        }
    }

    @Override
    public void initView() {
        mServiceSettingActivity.setContentView(R.layout.activity_server_settings);
        mServiceSettingActivity.eTxtName = mServiceSettingActivity.findViewById(R.id.name_content);
        mServiceSettingActivity.ipname = mServiceSettingActivity.findViewById(R.id.ipAddress_title);
        mServiceSettingActivity.eTxtAddress = mServiceSettingActivity.findViewById(R.id.ipAddress_content);
        mServiceSettingActivity.eTxtPort = mServiceSettingActivity.findViewById(R.id.port_content);
        mServiceSettingActivity.checkIp = mServiceSettingActivity.findViewById(R.id.ipcheck);
        mServiceSettingActivity.chectAgent = mServiceSettingActivity.findViewById(R.id.remoteagentcheck);
        mServiceSettingActivity.btnConfirm = mServiceSettingActivity.findViewById(R.id.confirm);
        mServiceSettingActivity.eTxtName.addTextChangedListener(mServiceSettingActivity.nameAndAddressChange);
        mServiceSettingActivity.eTxtAddress.addTextChangedListener(mServiceSettingActivity.nameAndAddressChange);
        mServiceSettingActivity.checkIp.setOnCheckedChangeListener(mServiceSettingActivity.mIpAndAgentCheckListener);
        mServiceSettingActivity.chectAgent.setOnCheckedChangeListener(mServiceSettingActivity.mIpAndAgentCheckListener);
        mServiceSettingActivity.btnConfirm.setOnClickListener(mServiceSettingActivity.mConfirmListener);
        mServiceSettingActivity.yesCheck = (CheckBox) mServiceSettingActivity.findViewById(R.id.yesCheck);
        mServiceSettingActivity.yesCheck.setOnCheckedChangeListener(mServiceSettingActivity.mHttpsCheckListener);
        mServiceSettingActivity.noCheck = (CheckBox) mServiceSettingActivity.findViewById(R.id.noCheck);
        mServiceSettingActivity.noCheck.setOnCheckedChangeListener(mServiceSettingActivity.mHttpsCheckListener);
        ToolBarHelper.setTitle(mServiceSettingActivity.mActionBar,mServiceSettingActivity.getString(R.string.activity_title_servicesetting));
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

    }

    public void initService()
    {
        mService = mServiceSettingActivity.getIntent().getParcelableExtra("service");
        mServiceSettingActivity.eTxtName.setText(mService.sName);
        if(mService.sType == true)
        {
            mServiceSettingActivity.checkIp.setChecked(true);
            mServiceSettingActivity.chectAgent.setChecked(false);
            mServiceSettingActivity.ipname.setText(mServiceSettingActivity.getString(R.string.servicesetting_ip));
            mServiceSettingActivity.eTxtAddress.setText(mService.sAddress);
        }
        else
        {
            mServiceSettingActivity.checkIp.setChecked(false);
            mServiceSettingActivity.chectAgent.setChecked(true);
            mServiceSettingActivity.ipname.setText(mServiceSettingActivity.getString(R.string.servicesetting_code));
            mServiceSettingActivity.eTxtAddress.setText(mService.sCode);
        }
        if(mService.https == true)
        {
            mServiceSettingActivity.yesCheck.setChecked(true);
            mServiceSettingActivity.noCheck.setChecked(false);
        }
        else
        {
            mServiceSettingActivity.yesCheck.setChecked(false);
            mServiceSettingActivity.noCheck.setChecked(true);
        }

        mServiceSettingActivity.eTxtPort.setText(mService.sPort);
    }

    public void doConfirm()
    {
        String port = mServiceSettingActivity.eTxtPort.getText().toString();
        if(port.length() > 0)
        {
            if(Long.valueOf(port) > 65535 || Long.valueOf(port) < 0)
            {
                AppUtils.showMessage(mServiceSettingActivity,mServiceSettingActivity.getString(R.string.error_msg_port));
                return;
            }
        }
        else
        {
            port = "80";
        }
        mService.sPort = port;
        if(mServiceSettingActivity.checkIp.isChecked())
        mService.sType = true;
        else
            mService.sType = false;
        if(mServiceSettingActivity.yesCheck.isChecked())
            mService.https = true;
        else
            mService.https = false;
        mService.sName = mServiceSettingActivity.eTxtName.getText().toString();
        if(mService.sType == true)
        {
            mService.sAddress = mServiceSettingActivity.eTxtAddress.getText().toString();
            mService.sCode = "";
        }
        else
        {
            mService.sCode = mServiceSettingActivity.eTxtAddress.getText().toString();
            mService.sAddress = "";
        }

        DBHelper.getInstance(mServiceSettingActivity).addServer(mService);
        Intent intent = new Intent();
        intent.setAction(ServiceSettingActivity.ACTION_SERVICE_UPDATA);
        intent.putExtra("service",mService);
        if(mServiceSettingActivity.getIntent().hasExtra("service"))
        {
            intent.putExtra("isnew",false);
        }
        else
        {
            intent.putExtra("isnew",true);
        }

        mServiceSettingActivity.sendBroadcast(intent);
        mServiceSettingActivity.finish();

    }


}
