package com.interskypad.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.interskypad.R;
import com.interskypad.entity.Customer;

import java.util.ArrayList;


/**
 * Created by xpx on 2016/10/12.
 */

public class CustomerAdapter extends BaseAdapter {

    public ArrayList<Customer> mContacts;
    private Context mContext;
    private LayoutInflater mInflater;

    public CustomerAdapter(ArrayList<Customer> mContacts, Context mContext)
    {
        this.mContext = mContext;
        this.mContacts = mContacts;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public Customer getItem(int position) {
        return mContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Customer mContact = getItem(position);

        if(mContact.type == 0)
        {
            convertView = mInflater.inflate(R.layout.customer_item, null);
        }
        else
        {
            convertView = mInflater.inflate(R.layout.contact_item_letter, null);
        }
        if(mContact.type == 0)
        {
            TextView mName=(TextView) convertView.findViewById(R.id.nametext);
            mName.setText(mContact.getName());
            RelativeLayout line = convertView.findViewById(R.id.line);
            RelativeLayout layout = convertView.findViewById(R.id.layer);
            if(mContact.isSelect == false)
            {
                layout.setBackgroundColor(Color.parseColor("#00000000"));
            }
            else
            {
                layout.setBackgroundColor(Color.parseColor("#ff6191c1"));
            }
            if(position != mContacts.size()-1)
            {
                line.setVisibility(View.VISIBLE);
            }
            else {
                line.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            TextView mName=(TextView) convertView.findViewById(R.id.item_letter_name);
            mName.setText(mContact.getName());
        }


        return convertView;
    }

    private static class ViewHolder {
        private TextView mName;
    }


}