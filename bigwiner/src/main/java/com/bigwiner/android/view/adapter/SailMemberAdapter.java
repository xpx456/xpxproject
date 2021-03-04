package com.bigwiner.android.view.adapter;

import android.content.Context;
import android.graphics.Color;
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

import com.bigwiner.R;
import com.bigwiner.android.ViewHelp;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.entity.Complaint;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.entity.SailMember;
import com.bigwiner.android.view.BigwinerApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;

import java.util.ArrayList;

import intersky.appbase.AppActivityManager;
import intersky.appbase.MySimpleTarget;
import intersky.appbase.entity.Contacts;
import intersky.apputils.GlideApp;
import intersky.apputils.TimeUtils;
import intersky.mywidget.CircleImageView;


/**
 * Created by xpx on 2016/10/12.
 */

public class SailMemberAdapter extends RecyclerView.Adapter {

    private ArrayList<SailMember> mContacts;
    private Context mContext;
    private LayoutInflater mInflater;
    private int type = 0;
    public Handler handler;
    public Meeting meeting;

    public SailMemberAdapter(ArrayList<SailMember> mContacts, Context mContext) {
        this.mContext = mContext;
        this.mContacts = mContacts;
        this.type = 0;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public SailMember getItem(int position) {
        return mContacts.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.sailmember, null);
        return new ViewHoder(convertView);
    }

    public interface OnItemClickListener{
        void onItemClick(SailMember mSailMember, int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    private OnItemClickListener mListener;

    class ViewHoder extends RecyclerView.ViewHolder{
        CircleImageView imageView;
        TextView mtitle;
        TextView mtitle2;
        TextView confrim;
        View t1;
        ArrayList<ImageView> stars = new ArrayList<ImageView>();
        TextView money;

        public ViewHoder(@NonNull View convertView) {
            super(convertView);
            mtitle = (TextView) convertView.findViewById(R.id.conversation_title);
            mtitle2 = (TextView) convertView.findViewById(R.id.des);
            imageView = (CircleImageView) convertView.findViewById(R.id.conversation_img);
            confrim = convertView.findViewById(R.id.cinfirmsubject);
            money = (TextView) convertView.findViewById(R.id.moneysubject);
            t1 = convertView.findViewById(R.id.t1);
            stars.add((ImageView) convertView.findViewById(R.id.star1));
            stars.add((ImageView) convertView.findViewById(R.id.star2));
            stars.add((ImageView) convertView.findViewById(R.id.star3));
            stars.add((ImageView) convertView.findViewById(R.id.star4));
            stars.add((ImageView) convertView.findViewById(R.id.star5));
        }
    };

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SailMember mContact = getItem(position);
        ViewHoder mview = (ViewHoder) holder;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                    mListener.onItemClick(mContact,position,holder.itemView);
            }
        });
        ViewHelp.praseLeaves(mview.stars,mContact.leavel);
        mview.mtitle.setText(mContact.cname);
        mview.mtitle2.setText(mContact.address);
        mview.confrim.setText(mContext.getString(R.string.company_issail)+String.valueOf(mContact.joinyear)+"年");
        if(position == 0)
        {
            mview.t1.setVisibility(View.VISIBLE);
        }
        else
        {
            mview.t1.setVisibility(View.GONE);
        }
        if(!mContact.money.equals("none") && mContact.money.length() > 0)
            mview.money.setText(String.valueOf(mContact.money));
        else
            mview.money.setText("0");
//        BigwinerApplication.mApp.setContactHead(mContext,mContact.logo,mview.imageView);
        RequestOptions options = new RequestOptions()
                .placeholder(com.bigwiner.R.drawable.contact_detial_head).diskCacheStrategy(DiskCacheStrategy.ALL)
                .override((int) (50* AppActivityManager.getAppActivityManager(mContext).mScreenDefine.density));
        String url = ContactsAsks.getCompanyIconUrl(mContact.cid,BigwinerApplication.mApp.contactManager.updataKey);
        GlideApp.with(mContext).load(ContactsAsks.getCompanyIconUrl(mContact.cid,BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(mview.imageView));
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

}
