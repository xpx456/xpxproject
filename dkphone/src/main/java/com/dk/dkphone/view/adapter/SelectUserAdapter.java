package com.dk.dkphone.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dk.dkphone.R;
import com.dk.dkphone.entity.User;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.MySimpleTarget;
import intersky.mywidget.CircleImageView;

public class SelectUserAdapter extends BaseAdapter {

    public ArrayList<User> users = new ArrayList<>();
    public Context context;
    private LayoutInflater mInflater;
    public SelectUserAdapter(Context context, ArrayList<User> users)
    {
        this.context = context;
        this.users = users;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        User user = getItem(position);
        ViewHoder mview = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.user_pk_list_item, null);
            mview = new ViewHoder();
            mview.head = (CircleImageView) convertView.findViewById(R.id.headicon);
            mview.select = (ImageView) convertView.findViewById(R.id.select);
            mview.name = (TextView) convertView.findViewById(R.id.headname);
            convertView.setTag(mview);
        } else {
            mview = (ViewHoder) convertView.getTag();
        }

        mview.name.setText(user.name);
        mview.head.setVisibility(View.VISIBLE);
        if(user.sex.equals(context.getString(R.string.male)))
        {
            if(user.headpath.length() > 0)
            {
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.default_user);
                Glide.with(context).load(new File(user.headpath)).apply(options).into(new MySimpleTarget(mview.head));
            }
            else
            {
                mview.head.setImageResource(R.drawable.default_user);
            }

        }
        else
        {
            if(user.headpath.length() > 0)
            {
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.default_wuser);
                Glide.with(context).load(new File(user.headpath)).apply(options).into(new MySimpleTarget(mview.head));
            }
            else
            {
                mview.head.setImageResource(R.drawable.default_wuser);
            }
        }



        return convertView;
    }

    public class ViewHoder {
        CircleImageView head;
        ImageView select;
        TextView name;
    };
}
