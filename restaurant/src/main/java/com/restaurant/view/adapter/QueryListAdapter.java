package com.restaurant.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.restaurant.R;
import com.restaurant.entity.Guest;

import java.util.ArrayList;

import intersky.mywidget.CircleImageView;

public class QueryListAdapter extends RecyclerView.Adapter {

    private ArrayList<Guest> guests;
    private Context mContext;
    private LayoutInflater mInflater;


    public QueryListAdapter(ArrayList<Guest> guests, Context mContext) {
        this.mContext = mContext;
        this.guests = guests;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public Guest getItem(int position) {
        return guests.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.guest_tiem_view, null);
        return new ViewHoder(convertView);
    }

    public interface OnItemClickListener{
        void onItemClick(Guest mGuest, int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    private OnItemClickListener mListener;

    class ViewHoder extends RecyclerView.ViewHolder{
        CircleImageView imageView;
        TextView id;
        TextView name;
        TextView licence;
        TextView lock;
        TextView cancard;
        TextView canfinger;
        LinearLayout bg;
        public ViewHoder(@NonNull View convertView) {
            super(convertView);
            bg = convertView.findViewById(R.id.listbg);
            id = (TextView) convertView.findViewById(R.id.head_id);
            name = (TextView) convertView.findViewById(R.id.head_name);
            licence = (TextView) convertView.findViewById(R.id.head_licence);
            lock = (TextView) convertView.findViewById(R.id.head_lock);
            cancard = (TextView) convertView.findViewById(R.id.head_can_card);
            canfinger = (TextView) convertView.findViewById(R.id.head_can_finger);

        }
    };

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Guest mguest = getItem(position);
        ViewHoder mview = (ViewHoder) holder;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                    mListener.onItemClick(mguest,position,holder.itemView);
            }
        });

        if(position %2 == 0)
        {
            mview.bg.setBackgroundColor(Color.rgb(255,255,255));
        }
        else
        {
            mview.bg.setBackgroundColor(Color.rgb(240,244,254));
        }
        mview.id.setText(mguest.rid);
        mview.name.setText(mguest.name);
        mview.licence.setText(mguest.licence);
        if(mguest.type.equals("0"))
        {
            mview.lock.setText("否");
        }
        else
        {
            mview.lock.setText("是");
        }
        if(mguest.cancard.equals("0"))
        {
            mview.cancard.setText("否");
        }
        else
        {
            mview.cancard.setText("是");
        }
        if(mguest.canfinger.equals("0"))
        {
            mview.canfinger.setText("否");
        }
        else
        {
            mview.canfinger.setText("是");
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return guests.size();
    }
}
