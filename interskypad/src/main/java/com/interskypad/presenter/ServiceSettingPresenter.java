package com.interskypad.presenter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.interskypad.R;
import com.interskypad.database.DBHelper;
import com.interskypad.view.activity.ServiceSettingActivity;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.Service;
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
        ImageView bask = mServiceSettingActivity.findViewById(R.id.back);
        bask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mServiceSettingActivity.finish();
            }
        });
        mServiceSettingActivity.root = mServiceSettingActivity.findViewById(R.id.activity_server_settings);
        mServiceSettingActivity.eTxtName = mServiceSettingActivity.findViewById(R.id.name_content);
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
            mServiceSettingActivity.eTxtAddress.setText(mService.sAddress);
        }
        else
        {
            mServiceSettingActivity.checkIp.setChecked(false);
            mServiceSettingActivity.chectAgent.setChecked(true);
            mServiceSettingActivity.eTxtAddress.setText(mService.sCode);
        }
        mServiceSettingActivity.eTxtPort.setText(mService.sPort);
    }

    public void doConfirm()
    {
        String port = mServiceSettingActivity.eTxtPort.getText().toString();
        if(port.length() > 0)
        {
            if(Integer.valueOf(port) > 65535 || Integer.valueOf(port) < 0)
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
        mService.sName = mServiceSettingActivity.eTxtName.getText().toString();
        mService.sAddress = mServiceSettingActivity.eTxtAddress.getText().toString();
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
