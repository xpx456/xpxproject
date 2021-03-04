package com.dk.dkpad.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dk.dkpad.R;
import com.dk.dkpad.entity.User;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.MySimpleTarget;
import intersky.mywidget.CircleImageView;

public class PkUserAdapter extends BaseAdapter {

    public ArrayList<User> users = new ArrayList<>();
    public Context context;
    private LayoutInflater mInflater;
    public PkUserAdapter(Context context, ArrayList<User> users)
    {
        this.context = context;
        this.users = users;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return users.size()-1;
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
            convertView = mInflater.inflate(R.layout.user_list_item, null);
            mview = new ViewHoder();
            mview.head = (CircleImageView) convertView.findViewById(R.id.headicon);
            mview.head2 = (ImageView) convertView.findViewById(R.id.headicon2);
            mview.name = (TextView) convertView.findViewById(R.id.headname);
            convertView.setTag(mview);
        } else {
            mview = (ViewHoder) convertView.getTag();
        }

        if(user.name.length() > 0)
        {
            mview.name.setText(user.name);
            mview.head.setVisibility(View.VISIBLE);
            mview.head2.setVisibility(View.INVISIBLE);
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
        }
        else
        {
            mview.name.setText(context.getString(R.string.newadd));
            mview.head.setVisibility(View.INVISIBLE);
            mview.head2.setVisibility(View.VISIBLE);
            mview.head2.setImageResource(R.drawable.newuser);
        }


        return convertView;
    }

    public class ViewHoder {
        CircleImageView head;
        ImageView head2;
        TextView name;
    };
}
