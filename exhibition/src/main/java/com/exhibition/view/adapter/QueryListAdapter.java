package com.exhibition.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exhibition.R;
import com.exhibition.entity.Guest;

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
        TextView time;
        TextView name;
        TextView type;
        TextView sex;
        TextView count;
        TextView car;
        TextView card;
        RelativeLayout bg;
        public ViewHoder(@NonNull View convertView) {
            super(convertView);
            bg = convertView.findViewById(R.id.listbg);
            time = (TextView) convertView.findViewById(R.id.head_time);
            name = (TextView) convertView.findViewById(R.id.head_time);
            type = (TextView) convertView.findViewById(R.id.head_time);
            sex = (TextView) convertView.findViewById(R.id.head_time);
            count = (TextView) convertView.findViewById(R.id.head_time);
            car = (TextView) convertView.findViewById(R.id.head_time);
            card = (TextView) convertView.findViewById(R.id.head_time);

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
            mview.bg.setBackgroundColor(0xf0f4fe);
        }
        else
        {
            mview.bg.setBackgroundColor(0xffffff);
        }
        mview.time.setText(mguest.time);
        mview.name.setText(mguest.name);
        mview.type.setText(mguest.type);
        mview.sex.setText(mguest.sex);
        mview.count.setText(mguest.count);
        mview.car.setText(mguest.car);
        mview.card.setText(mguest.card);
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
