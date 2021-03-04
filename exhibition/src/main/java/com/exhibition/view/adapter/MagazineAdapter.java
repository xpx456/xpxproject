package com.exhibition.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.exhibition.R;
import com.exhibition.entity.Magezine;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.MySimpleTarget;
import intersky.apputils.AppUtils;
import intersky.apputils.GlideApp;
import intersky.apputils.StringUtils;

public class MagazineAdapter extends BaseAdapter {

    public ArrayList<Magezine> files;
    public Context mContext;
    private LayoutInflater mInflater;
    public MagazineAdapter(Context mContext,ArrayList<Magezine> files) {
        this.mContext = mContext;
        this.files = files;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Magezine getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Magezine file = getItem(position);

        ViewHoder mview;
        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.path_item, null);
            mview = new ViewHoder();
            mview.mtitle = (TextView) convertView.findViewById(R.id.path_title);
            mview.bg = (ImageView) convertView.findViewById(R.id.bg);
            convertView.setTag(mview);
        }
        else
        {
            mview = (ViewHoder) convertView.getTag();
        }
        mview.mtitle.setText(file.name);
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        GlideApp.with(mContext).load(file.first).apply(options).into(new MySimpleTarget(mview.bg));
        return convertView;
    }

    public class ViewHoder {
        TextView mtitle;
        ImageView bg;

    }

}
