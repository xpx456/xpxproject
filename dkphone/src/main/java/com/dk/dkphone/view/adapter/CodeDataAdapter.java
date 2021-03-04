package com.dk.dkphone.view.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dk.dkphone.R;

import java.util.ArrayList;

public class CodeDataAdapter extends BaseAdapter {

    public BluetoothDevice connectdevice = null;
    public ArrayList<String> datas;
    public Context context;
    private LayoutInflater mInflater;
    public CodeDataAdapter(Context context, ArrayList<String> devices) {
        this.datas = devices;
        this.context = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public String getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        String data = getItem(position);
        ViewHoder mview = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.code_data, null);
            mview = new ViewHoder();
            mview.mtitle = (TextView) convertView.findViewById(R.id.equip_name);
            convertView.setTag(mview);
        } else {
            mview = (ViewHoder) convertView.getTag();
        }
        mview.mtitle.setText(data);
        return convertView;
    }


    public  class ViewHoder {
        TextView mtitle;
    };


}
