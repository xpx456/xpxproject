package com.dk.dkhome.view.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.dk.dkhome.R;

import java.util.ArrayList;

public class DeviceAdapter extends BaseAdapter {

    public BluetoothDevice connectdevice = null;
    public ArrayList<BluetoothDevice> devices;
    public Context context;
    private LayoutInflater mInflater;
    public DeviceAdapter(Context context, ArrayList<BluetoothDevice> devices) {
        this.devices = devices;
        this.context = context;
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
            convertView.setTag(mview);
        } else {
            mview = (ViewHoder) convertView.getTag();
        }
        mview.mtitle.setText(device.getName());
        mview.mac.setText(device.getAddress());
        return convertView;
    }


    public  class ViewHoder {
        TextView mac;
        TextView mtitle;
    };


}
