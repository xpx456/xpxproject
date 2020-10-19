package com.dk.dkphone.view.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.dkphone.R;

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
            mview.connect = convertView.findViewById(R.id.connect);
            convertView.setTag(mview);
        } else {
            mview = (ViewHoder) convertView.getTag();
        }
        mview.mtitle.setText(device.getName());
        mview.mac.setText(device.getAddress());
        if(connectdevice == null)
        {
            mview.connect.setImageResource(R.drawable.unconnect);
        }
        else
        {
            if(device.getAddress().equals(connectdevice.getAddress()))
            {
                mview.connect.setImageResource(R.drawable.connect);
            }
            else
            {
                mview.connect.setImageResource(R.drawable.unconnect);
            }
        }


        mview.connect.setTag(device);
        return convertView;
    }


    public  class ViewHoder {
        TextView mac;
        TextView mtitle;
        ImageView connect;
    };


}
