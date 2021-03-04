package com.dk.dkhome.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.dkhome.R;

import java.util.ArrayList;

import intersky.select.entity.CustomSelect;
import intersky.select.view.adapter.CustomSelectAdapter;


public class DeviceSelectAdapter extends CustomSelectAdapter {


    public DeviceSelectAdapter(Context context, ArrayList<CustomSelect> mSelectMores) {

        super(context, mSelectMores);
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mSelectMores.size();
    }

    @Override
    public CustomSelect getItem(int position)
    {
        // TODO Auto-generated method stub
        return mSelectMores.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        CustomSelect mSelectMoreModel = mSelectMores.get(position);
        ViewHolder mview = null;
        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.item_equip_type, null);
            mview = new ViewHolder();
            mview.title = convertView.findViewById(R.id.equip_name);
            mview.icon = convertView.findViewById(R.id.equip);
            mview.imageView = convertView.findViewById(R.id.select);
            convertView.setTag(mview);
        }
        else
        {
            mview = (ViewHolder) convertView.getTag();
        }
        mview.title.setText(mSelectMoreModel.mName);
        if(mSelectMoreModel.mId.equals("1"))
        {
            mview.icon.setImageResource(R.drawable.elliptical);
        }
        else if(mSelectMoreModel.mId.equals("2"))
        {
            mview.icon.setImageResource(R.drawable.rowing);
        }
        else if(mSelectMoreModel.mId.equals("3"))
        {
            mview.icon.setImageResource(R.drawable.exercise);
        }
        else if(mSelectMoreModel.mId.equals("4"))
        {
            mview.icon.setImageResource(R.drawable.spinning);
        }

        if(mSelectMoreModel.iselect)
        {
            mview.imageView.setImageResource(R.drawable.selectmial);
            mview.title.setTextColor(Color.parseColor("#1EA1F3"));
        }
        else
        {
            mview.imageView.setImageResource(R.drawable.image_null);
            mview.title.setTextColor(Color.parseColor("#23272E"));
        }
        return convertView;
    }

    class ViewHolder {
        private TextView title;
        private ImageView imageView;
        private ImageView icon;
    }

}
