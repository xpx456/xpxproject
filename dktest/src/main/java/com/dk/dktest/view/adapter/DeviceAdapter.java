package com.dk.dktest.view.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.dk.dktest.R;
import com.dk.dktest.view.DkTestApplication;

import java.util.ArrayList;

import xpx.bluetooth.BluetoothSetManager;

public class DeviceAdapter extends BaseAdapter {


    public ArrayList<BluetoothDevice> devices;
    public Context context;
    private LayoutInflater mInflater;
    public View root;
    public DeviceAdapter(Context context, ArrayList<BluetoothDevice> devices,View root) {
        this.devices = devices;
        this.context = context;
        this.root = root;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public BluetoothDevice getItem(int i) {
        return devices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        BluetoothDevice device = getItem(position);
        ViewHoder mview = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_equip, null);
            mview = new ViewHoder();
            mview.mtitle = (TextView) convertView.findViewById(R.id.equip_name);
            mview.mac = (TextView) convertView.findViewById(R.id.equip_mac);
            mview.connect = convertView.findViewById(R.id.connect);
            convertView.setTag(mview);
        } else {
            mview = (ViewHoder) convertView.getTag();
        }
        mview.mtitle.setText(device.getName());
        mview.mac.setText(device.getAddress());
        if(DkTestApplication.mApp.bluetoothSetManager.getDeviceConnect(device.getAddress()) == false)
        {
            mview.connect.setImageResource(R.drawable.unconnect);
        }
        else
        {
            mview.connect.setImageResource(R.drawable.connect);
        }


        mview.connect.setTag(device);
        mview.connect.setOnClickListener(connectListener);
        return convertView;
    }


    public  class ViewHoder {
        TextView mac;
        TextView mtitle;
        ImageView connect;
    };

    public View.OnClickListener connectListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BluetoothDevice bluetoothDevice = (BluetoothDevice) view.getTag();
            if(DkTestApplication.mApp.bluetoothSetManager.getDeviceConnect(bluetoothDevice.getAddress()) == true)
            {
                DkTestApplication.mApp.connectMac = "";
                DkTestApplication.mApp.bluetoothSetManager.connectStop(bluetoothDevice.getAddress());
            }
            else
            {
                DkTestApplication.mApp.connectMac = bluetoothDevice.getAddress();
                DkTestApplication.mApp.bluetoothSetManager.autoConnectDevice(context,root,bluetoothDevice.getAddress());
            }

        }
    };
}
