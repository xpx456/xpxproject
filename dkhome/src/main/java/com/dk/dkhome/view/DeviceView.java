package com.dk.dkhome.view;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.icu.text.CaseMap;
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
import com.dk.dkhome.presenter.SportDetialPresenter;
import com.dk.dkhome.utils.TestManager;
import com.dk.dkhome.view.adapter.DeviceAdapter;

import intersky.filetools.PathUtils;
import intersky.select.SelectManager;

public class DeviceView {

    public static final String ACTION_UPDTAT_DEIVECONNECT_IMF = "ACTION_UPDTAT_DEIVECONNECT_IMF";
    public static final String ACTION_UPDTAT_DEVICE_TYPE = "ACTION_UPDTAT_DEVICE_TYPE";
    public static final String ACTION_UPDTAT_DEVICE_TYPE_NAME = "ACTION_UPDTAT_DEVICE_TYPE_NAME";
    public SportDetialPresenter mSportDetialPresenter;
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

    public DeviceView(SportDetialPresenter mSportDetialPresenter) {
        this.mSportDetialPresenter = mSportDetialPresenter;
        init();
    }

    public void init() {
        LayoutInflater mInflater = (LayoutInflater) mSportDetialPresenter.mSportDetialActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.fragment_device,null);
        TextView title = view.findViewById(R.id.name_title);
        device = TestManager.testManager.device;
        if(device.deviceName.length() > 0)
        title.setText(device.deviceName);
        else
            title.setText(device.typeName);
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
        queryListAdapter = new DeviceAdapter(mSportDetialPresenter.mSportDetialActivity, TestManager.testManager.bluetoothSetManager.deviceslist);
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
        rpm.setText(String .format("%d",mSportDetialPresenter.deviceView.device.nowrpm)+ " rpm");
        work.setText(String.valueOf(EquipData.getWork(mSportDetialPresenter.deviceView.device.nowleavel,
                (int) mSportDetialPresenter.deviceView.device.nowrpm,device))+"W");
        switch (device.statetype){
            case Device.DEVICE_NOMAL:
                state.setText(mSportDetialPresenter.mSportDetialActivity
                        .getString(R.string.device_state_nomal));
                break;
            case Device.DEVICE_LEAVECHANGE:
                state.setText(mSportDetialPresenter.mSportDetialActivity
                        .getString(R.string.device_state_change));
                break;
            case Device.DEVICE_ERROR:
                state.setText(mSportDetialPresenter.mSportDetialActivity
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
        mSportDetialPresenter.progressView.creat(mSportDetialPresenter.mSportDetialActivity,
                mSportDetialPresenter.shade,mSportDetialPresenter.mSportDetialActivity.findViewById(R.id.activity_splash),
                mSportDetialPresenter.deviceView.device,mSportDetialPresenter.mSportDetialHandler);
        TestManager.testManager.connectBlueDevice(mSportDetialPresenter.mSportDetialActivity,DeviceView.ACTION_UPDTAT_DEIVECONNECT_IMF,device);
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

            SelectManager.getInstance().startCustomSelectView(mSportDetialPresenter.mSportDetialActivity,
                    mSportDetialPresenter.customSelectAdapter,null,"" ,
                    DeviceView.ACTION_UPDTAT_DEVICE_TYPE,false,false );
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
            SelectManager.getInstance().startSelectView(mSportDetialPresenter.mSportDetialActivity,
                   EquipData.getInstance().selects,"" ,
                    ACTION_UPDTAT_DEVICE_TYPE,false,false );
        }
    };
}
