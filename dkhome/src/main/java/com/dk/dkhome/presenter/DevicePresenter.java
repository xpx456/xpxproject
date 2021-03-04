package com.dk.dkhome.presenter;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.dkhome.R;
import com.dk.dkhome.entity.Device;
import com.dk.dkhome.entity.EquipData;
import com.dk.dkhome.entity.User;
import com.dk.dkhome.entity.UserWeight;
import com.dk.dkhome.handler.DeviceHandler;
import com.dk.dkhome.handler.SportDetialHandler;
import com.dk.dkhome.receiver.DeviceReceiver;
import com.dk.dkhome.utils.TestManager;
import com.dk.dkhome.view.DeviceView;
import com.dk.dkhome.view.DkhomeApplication;
import com.dk.dkhome.view.MainDeviceView;
import com.dk.dkhome.view.ProgressView;
import com.dk.dkhome.view.activity.GoalActivity;
import com.dk.dkhome.view.activity.DeviceActivity;
import com.dk.dkhome.view.activity.RegisterActivity;

import org.json.JSONException;

import java.util.Map;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.echartoption.ArrayData;
import intersky.echartoption.ObjectData;
import intersky.json.XpxJSONObject;
import intersky.select.SelectManager;
import intersky.select.view.adapter.CustomSelectAdapter;


public class DevicePresenter implements Presenter {
    private static final int DECIMAL_DIGITS = 1;

    public DeviceActivity mDeviceActivity;
    public MainDeviceView deviceView;
    public ProgressView progressView;
    public RelativeLayout shade;
    public DeviceHandler deviceHandler;
    public CustomSelectAdapter customSelectAdapter;
    public DevicePresenter(DeviceActivity mDeviceActivity) {
        this.mDeviceActivity = mDeviceActivity;
        deviceHandler = new DeviceHandler(mDeviceActivity);
        mDeviceActivity.setBaseReceiver(new DeviceReceiver(deviceHandler));
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub\
        mDeviceActivity.setContentView(R.layout.activity_device);
        ImageView back = mDeviceActivity.findViewById(R.id.back);
        back.setOnClickListener(mDeviceActivity.mBackListener);
        shade = mDeviceActivity.findViewById(R.id.shade);
        customSelectAdapter = new CustomSelectAdapter(mDeviceActivity, EquipData.equipData.names);
        deviceView = new MainDeviceView(this);
        LinearLayout linearLayout = mDeviceActivity.findViewById(R.id.content);
        linearLayout.addView(deviceView.view);
        DkhomeApplication.mApp.testManager.sendData.add(sendData);
    }


    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }


    @Override
    public void Resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        DkhomeApplication.mApp.testManager.sendData.remove(sendData);
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }

    public void updataData(String[] data) {

        deviceView.device.updataData(data);
        deviceView.doUpdata();
    }

    public void updataDeviceState()
    {
        deviceView.updataView();
    }

    public void scanFinish(Intent intent) {
        Bundle bundle = intent.getExtras();
        String json = bundle.getString("result");
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            String address = jsonObject.getString("deviceAddress");
            String name = jsonObject.getString("deviceName");
            deviceView.device.deviceName = name;
            deviceView.device.deviceMac = address;
            SelectManager.getInstance().startCustomSelectView(mDeviceActivity,
                    customSelectAdapter,null,"" ,
                    DeviceView.ACTION_UPDTAT_DEVICE_TYPE,false,false );


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private TestManager.SendData sendData = new TestManager.SendData() {
        @Override
        public void sendData(String[] data) {
            Message msg = new Message();
            msg.what = DeviceHandler.UPDTATA_DATA;
            msg.obj = data;
            if(deviceHandler != null)
                deviceHandler.sendMessage(msg);
        }
    };
}
