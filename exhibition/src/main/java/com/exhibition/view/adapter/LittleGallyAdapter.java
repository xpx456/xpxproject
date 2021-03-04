package com.exhibition.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.exhibition.R;
import com.exhibition.entity.Guest;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.MySimpleTarget;
import intersky.apputils.GlideApp;
import intersky.mywidget.CircleImageView;

public class LittleGallyAdapter extends RecyclerView.Adapter{

    private ArrayList<File> photos;
    private Context mContext;
    private LayoutInflater mInflater;
    public int currentid = 0;

    public LittleGallyAdapter(ArrayList<File> photos, Context mContext) {
        this.mContext = mContext;
        this.photos = photos;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public File getItem(int position) {
        return photos.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == currentid)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView;
        if(viewType == 1)
        {
            convertView = mInflater.inflate(R.layout.biger_gally_image, null);
        }
        else
        {
            convertView = mInflater.inflate(R.layout.little_gally_image, null);
        }

        return new ViewHoder(convertView);
    }

    public interface OnItemClickListener{
        void onItemClick(File file, int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    private OnItemClickListener mListener;

    class ViewHoder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHoder(@NonNull View convertView) {
            super(convertView);
            imageView = convertView.findViewById(R.id.photo);

        }
    };

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        File file = getItem(position);
        ViewHoder mview = (ViewHoder) holder;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                    mListener.onItemClick(file,position,holder.itemView);
            }
        });
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        GlideApp.with(mContext).load(file).apply(options).into(new MySimpleTarget(mview.imageView));
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

}
