package com.dk.dkhome.view;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dk.dkhome.R;
import com.dk.dkhome.entity.Device;
import com.dk.dkhome.entity.EquipData;
import com.dk.dkhome.presenter.DevicePresenter;
import com.dk.dkhome.presenter.SportDetialPresenter;
import com.dk.dkhome.utils.TestManager;
import com.dk.dkhome.view.adapter.DeviceAdapter;

import intersky.select.SelectManager;

public class MainDeviceView {

    public static final String ACTION_UPDTAT_DEIVECONNECT_IMF = "ACTION_UPDTAT_DEIVECONNECT_IMF";
    public static final String ACTION_UPDTAT_DEVICE_TYPE = "ACTION_UPDTAT_DEVICE_TYPE";
    public DevicePresenter devicePresenter;
    public View view;
    public Device device = new Device();
    public ListView deviceList;
    public ImageView btnReflash;
    public DeviceAdapter queryListAdapter;
    public RelativeLayout deviceConnect;
    public ScrollView deviceSet;
    public RelativeLayout typeSet;
    public TextView state;
    public TextView rpm;
    public TextView work;
    public TextView devicetype;
    public ImageView leave24;
    public ImageView leave32;
    public TextView btnDisconnect;
    public MainDeviceView(DevicePresenter devicePresenter) {
        this.devicePresenter = devicePresenter;
        init();
    }

    public void init() {
        LayoutInflater mInflater = (LayoutInflater) devicePresenter.mDeviceActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.fragment_device,null);
        TextView title = view.findViewById(R.id.name_title);
        device = TestManager.testManager.device;
        if(device.deviceName.length() > 0)
            title.setText(device.deviceName);
        else
            title.setText(device.typeName);
        device = TestManager.testManager.device;
        deviceList = view.findViewById(R.id.equiplist);
        btnReflash = view.findViewById(R.id.reflash);
        deviceConnect = view.findViewById(R.id.deviceconnect);
        deviceSet = view.findViewById(R.id.deviceset);
        typeSet = view.findViewById(R.id.pset);
        devicetype = view.findViewById(R.id.select1title);
        state = view.findViewById(R.id.devicestatevalue);
        rpm = view.findViewById(R.id.rpmvalue);
        work = view.findViewById(R.id.workvalue);
        leave24 = view.findViewById(R.id.lselect1);
        leave32 = view.findViewById(R.id.lselect2);
        btnDisconnect = view.findViewById(R.id.btn_disconnect);
        queryListAdapter = new DeviceAdapter(devicePresenter.mDeviceActivity, TestManager.testManager.bluetoothSetManager.deviceslist);
        deviceList.setAdapter(queryListAdapter);
        btnReflash.setOnClickListener(reflashListener);
        deviceList.setOnItemClickListener(onItemClickListener);
        btnDisconnect.setOnClickListener(disConnectListener);
        typeSet.setOnClickListener(selectDeviceTypeListener);
        leave24.setOnClickListener(se24Listener);
        leave32.setOnClickListener(se32Listener);
        leave24.setImageResource(R.drawable.select);
        leave32.setImageResource(R.drawable.selects);
        updataView();
    }

    public void updataView()
    {
        if(TestManager.testManager.state == false)
        {
            deviceSet.setVisibility(View.INVISIBLE);
            deviceConnect.setVisibility(View.VISIBLE);
        }
        else
        {
            deviceSet.setVisibility(View.VISIBLE);
            deviceConnect.setVisibility(View.INVISIBLE);
        }
    }

    public void doUpdata() {
        rpm.setText(String .format("%d",device.nowrpm)+ " rpm");
        work.setText(String.valueOf(EquipData.getWork(device.nowleavel,
                (int) device.nowrpm,device))+"W");
        switch (device.statetype){
            case Device.DEVICE_NOMAL:
                state.setText(devicePresenter.mDeviceActivity
                        .getString(R.string.device_state_nomal));
                break;
            case Device.DEVICE_LEAVECHANGE:
                state.setText(devicePresenter.mDeviceActivity
                        .getString(R.string.device_state_change));
                break;
            case Device.DEVICE_ERROR:
                state.setText(devicePresenter.mDeviceActivity
                        .getString(R.string.device_state_error));
                break;
        }
    }

    public void setType(Intent intent) {
        device.deviceForm = Integer.valueOf(SelectManager.getInstance().mSignal.mId);
        devicetype.setText(SelectManager.getInstance().mSignal.mName);
    }

    public void setTypeName(Intent intent) {
        device.typeName = SelectManager.getInstance().mCustomSignal.mName;
        devicePresenter.progressView.creat(devicePresenter.mDeviceActivity,
                devicePresenter.shade,devicePresenter.mDeviceActivity.findViewById(R.id.activity_splash),
                devicePresenter.deviceView.device,devicePresenter.deviceHandler);
        TestManager.testManager.connectBlueDevice(devicePresenter.mDeviceActivity,DeviceView.ACTION_UPDTAT_DEIVECONNECT_IMF,device);
    }

    public void updataDeviceList() {
        queryListAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener reflashListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TestManager.testManager.bluetoothSetManager.scanLeDevice();
            queryListAdapter.notifyDataSetChanged();
        }
    };

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BluetoothDevice bluetoothDevice = (BluetoothDevice) parent.getAdapter().getItem(position);
            device.deviceName = bluetoothDevice.getName();
            device.deviceMac = bluetoothDevice.getAddress();
            devicePresenter.progressView.creat(devicePresenter.mDeviceActivity,
                    devicePresenter.shade,devicePresenter.mDeviceActivity.findViewById(R.id.activity_splash),
                    device,devicePresenter.deviceHandler);
            TestManager.testManager.connectBlueDevice(devicePresenter.mDeviceActivity,ACTION_UPDTAT_DEIVECONNECT_IMF,device);
        }
    };

    private View.OnClickListener se24Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(device.maxleave == 32)
            {
                leave24.setImageResource(R.drawable.selects);
                leave32.setImageResource(R.drawable.select);
                device.maxleave = 24;
            }
        }
    };

    private View.OnClickListener se32Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(device.maxleave == 24)
            {
                leave24.setImageResource(R.drawable.select);
                leave32.setImageResource(R.drawable.selects);
                device.maxleave = 32;
            }
        }
    };

    private View.OnClickListener disConnectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DkhomeApplication.mApp.testManager.stopConnect(device);
        }
    };

    private View.OnClickListener selectDeviceTypeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SelectManager.getInstance().startSelectView(devicePresenter.mDeviceActivity,
                   EquipData.getInstance().selects,"" ,
                    ACTION_UPDTAT_DEVICE_TYPE,false,false );
        }
    };
}
