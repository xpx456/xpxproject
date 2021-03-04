package com.dk.dkphone.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.dkphone.R;
import com.dk.dkphone.entity.Optation;

import java.util.ArrayList;

public class OptationAdapter extends BaseAdapter {

    public ArrayList<Optation> optations = new ArrayList<>();
    public Context context;
    private LayoutInflater mInflater;
    public OptationAdapter(Context context, ArrayList<Optation> optations)
    {
        this.context = context;
        this.optations = optations;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return optations.size();
    }

    @Override
    public Optation getItem(int i) {
        return optations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Optation optation = getItem(position);
        ViewHoder mview = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.optation_list_item, null);
            mview = new ViewHoder();
            mview.head = (ImageView) convertView.findViewById(R.id.optationicon);
            mview.name = (TextView) convertView.findViewById(R.id.headname);
            convertView.setTag(mview);
        } else {
            mview = (ViewHoder) convertView.getTag();
        }
        if(optation.name.length() > 0)
        {
            mview.name.setText(optation.name);
            mview.head.setImageResource(R.drawable.powerset);
        }
        else
        {
            mview.name.setText(context.getString(R.string.newadd));
            mview.head.setImageResource(R.drawable.newoptation);
        }

        return convertView;
    }

    public class ViewHoder {
        ImageView head;
        TextView name;
    };
}
