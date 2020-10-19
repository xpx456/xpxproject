package com.accessmaster.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.accessmaster.R;
import com.accessmaster.entity.Device;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    public String id = "";
    public ArrayList<Device> devices = new ArrayList<Device>();
    public Context context;
    private LayoutInflater mInflater;
    public GridAdapter(Context context) {
        this.context = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Device getItem(int i) {
        return devices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Device device = getItem(position);
        ViewHoder mview = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.gride_item, null);
            mview = new ViewHoder();
            mview.mtitle = (TextView) convertView.findViewById(R.id.title);
            mview.imageView = (ImageView) convertView.findViewById(R.id.imgage);
            convertView.setTag(mview);
        } else {
            mview = (ViewHoder) convertView.getTag();
        }
        mview.mtitle.setText(device.cname);
        return convertView;
    }


    public  class ViewHoder {
        ImageView imageView;
        TextView mtitle;
    };
}
